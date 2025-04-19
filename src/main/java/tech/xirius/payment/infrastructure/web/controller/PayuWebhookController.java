package tech.xirius.payment.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.*;

import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/webhook/payu")
@RequiredArgsConstructor
public class PayuWebhookController {

    private final PaymentRepositoryPort paymentRepository;
    private final PaymentMetadataRepositoryPort metadataRepository;
    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort walletTransactionRepository;

    @PostMapping("/notification")
    public ResponseEntity<Void> handleNotification(@RequestParam Map<String, String> payload) {
        String referenceCode = payload.get("reference_sale");
        String transactionState = payload.get("polTransactionState");
        String responseCode = payload.get("polResponseCode");

        if (referenceCode == null || transactionState == null || responseCode == null) {
            return ResponseEntity.badRequest().build();
        }

        paymentRepository.findByReferenceCode(referenceCode).ifPresent(payment -> {
            String newStatus = mapStatus(transactionState, responseCode);
            payment.setStatus(newStatus);
            paymentRepository.save(payment);

            metadataRepository.findById(payment.getId()).ifPresent(meta -> {
                String userId = extraerUserIdDesdeJson(meta.getJsonMetadata());
                if (userId != null) {
                    Wallet wallet = walletRepository.findByUserId(userId)
                            .orElse(new Wallet(UUID.randomUUID(), userId, new Money(BigDecimal.ZERO, Currency.COP)));

                    BigDecimal previousBalance = wallet.getBalance().getAmount();
                    UUID walletId = wallet.getId();

                    // Crear siempre la transacción
                    WalletTransaction tx = WalletTransaction.recharge(walletId, payment.getAmount(), previousBalance);
                    walletTransactionRepository.save(tx);

                    // Solo si está aprobado se actualiza el balance
                    if ("APPROVED".equalsIgnoreCase(newStatus)) {
                        wallet.recharge(new Money(payment.getAmount(), Currency.COP));
                        walletRepository.save(wallet);
                    }
                }
            });
        });

        return ResponseEntity.ok().build();
    }

    private String mapStatus(String transactionState, String responseCode) {
        return switch (transactionState) {
            case "4" -> "APPROVED";
            case "6" -> switch (responseCode) {
                case "5" -> "FAILED";
                case "4" -> "REJECTED";
                default -> "FAILED";
            };
            case "12", "14" -> "PENDING";
            default -> "UNKNOWN";
        };
    }

    private String extraerUserIdDesdeJson(String jsonMetadata) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(jsonMetadata, new TypeReference<>() {
            });

            Object trxObj = map.get("transaction");
            if (!(trxObj instanceof Map<?, ?> trxMap))
                return null;

            Object extraObj = trxMap.get("extraParameters");
            if (!(extraObj instanceof Map<?, ?> extraParams))
                return null;

            Object userId = extraParams.get("PSE_REFERENCE3");
            return userId != null ? userId.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

}
