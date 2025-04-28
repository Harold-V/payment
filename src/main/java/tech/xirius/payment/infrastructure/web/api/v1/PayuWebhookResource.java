package tech.xirius.payment.infrastructure.web.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/webhook/payu")
@RequiredArgsConstructor
public class PayuWebhookResource {

    private final PaymentRepositoryPort paymentRepository;
    private final PaymentMetadataRepositoryPort metadataRepository;
    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort walletTransactionRepository;

    @PostMapping("/notification")
    public ResponseEntity<Void> handleNotification(@RequestParam Map<String, String> payload) {
        log.info("Notificación recibida: {}", payload);

        try {
            String referenceCode = payload.get("referenceSale");
            String transactionState = payload.get("polTransactionState");
            String responseCode = payload.get("polResponseCode");

            if (referenceCode == null || transactionState == null || responseCode == null) {
                log.warn("Faltan campos obligatorios en la notificación");
                return ResponseEntity.badRequest().build();
            }

            paymentRepository.findByReferenceCode(referenceCode).ifPresent(payment -> {
                log.info("Pago encontrado: {} con estado actual: {}", payment.getReferenceCode(), payment.getStatus());

                // Siempre actualiza el estado del payment
                String newStatus = mapStatus(transactionState, responseCode);
                payment.setStatus(newStatus);
                paymentRepository.save(payment);
                log.info("Estado de Payment actualizado a: {}", newStatus);

                metadataRepository.findById(payment.getId()).ifPresent(meta -> {
                    log.info("Metadata encontrada para Payment {}", payment.getId());

                    String userId = extraerUserIdDesdeJson(meta.getJsonMetadata());
                    if (userId != null) {
                        log.info("userId extraído: {}", userId);

                        Wallet wallet = walletRepository.findByUserId(userId)
                                .orElse(new Wallet(UUID.randomUUID(), userId,
                                        new Money(BigDecimal.ZERO, Currency.COP)));

                        walletTransactionRepository.findByPaymentId(payment.getId())
                                .orElseGet(() -> {
                                    // Solo si NO EXISTE aún la transacción, la creamos en estado "PENDING"
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
                                    log.info("WalletTransaction creada en estado PENDING para el Payment {}",
                                            payment.getId());
                                    return tx;
                                });

                        // Si el pago fue aprobado y aún no recargamos la wallet
                        if ("APPROVED".equalsIgnoreCase(newStatus)) {
                            wallet.recharge(new Money(payment.getAmount(), Currency.COP));
                            walletRepository.save(wallet);
                            log.info("Wallet recargada para usuario: {}", userId);
                        }
                    }
                });
            });

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error procesando notificación de PayU", e);
            return ResponseEntity.badRequest().build();
        }
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
            log.error("Error extrayendo userId del JSON", e);
            return null;
        }
    }
}
