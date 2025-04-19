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
import java.time.ZonedDateTime;
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
        System.out.println("Notificación recibida: " + payload);

        String referenceCode = payload.get("reference_sale");
        String transactionState = payload.get("polTransactionState");
        String responseCode = payload.get("polResponseCode");

        if (referenceCode == null || transactionState == null || responseCode == null) {
            System.out.println("Falta algún campo en la notificación");
            return ResponseEntity.badRequest().build();
        }

        paymentRepository.findByReferenceCode(referenceCode).ifPresent(payment -> {
            System.out.println(
                    "Pago encontrado: " + payment.getReferenceCode() + " con estado actual: " + payment.getStatus());

            String newStatus = mapStatus(transactionState, responseCode);
            payment.setStatus(newStatus);
            paymentRepository.save(payment);
            System.out.println("Estado actualizado a: " + newStatus);

            metadataRepository.findById(payment.getId()).ifPresent(meta -> {
                System.out.println("Metadata encontrada");
                System.out.println("JSON Metadata: " + meta.getJsonMetadata());

                String userId = extraerUserIdDesdeJson(meta.getJsonMetadata());
                System.out.println("userId extraído: " + userId);

                if (userId != null) {
                    Wallet wallet = walletRepository.findByUserId(userId)
                            .orElse(new Wallet(UUID.randomUUID(), userId, new Money(BigDecimal.ZERO, Currency.COP)));

                    UUID walletId = wallet.getId();
                    BigDecimal previousBalance = wallet.getBalance().getAmount();

                    WalletTransaction tx = new WalletTransaction(
                            UUID.randomUUID(),
                            walletId,
                            payment.getId(),
                            payment.getAmount(),
                            "RECHARGE",
                            previousBalance,
                            previousBalance.add(payment.getAmount()),
                            ZonedDateTime.now());
                    walletTransactionRepository.save(tx);

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

            Object userId = map.get("userId");
            return userId != null ? userId.toString() : null;

        } catch (Exception e) {
            return null;
        }
    }
}
