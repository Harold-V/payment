package tech.xirius.payment.infrastructure.web.controller;

import tech.xirius.payment.domain.repository.PaymentMetadataRepositoryPort;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.domain.repository.PaymentRepositoryPort;

@RestController
@RequestMapping("/webhook/payu")
@RequiredArgsConstructor
public class PayuWebhookController {

    private final PaymentRepositoryPort paymentRepository;
    private final PaymentMetadataRepositoryPort metadataRepository;
    private final RechargeWalletUseCase recargarWalletUseCase;

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

            if ("APPROVED".equalsIgnoreCase(newStatus)) {
                metadataRepository.findById(payment.getId()).ifPresent(meta -> {
                    String userId = extraerUserIdDesdeJson(meta.getJsonMetadata());
                    if (userId != null) {
                        recargarWalletUseCase.recharge(userId, payment.getAmount());
                    }
                });
            }
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
            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.readValue(jsonMetadata, Map.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> transaction = (Map<String, Object>) map.get("transaction");
            if (transaction == null)
                return null;

            @SuppressWarnings("unchecked")
            Map<String, Object> extraParams = (Map<String, Object>) transaction.get("extraParameters");
            if (extraParams == null)
                return null;

            return (String) extraParams.get("PSE_REFERENCE3"); // Aquí guardaste el userId
        } catch (Exception e) {
            return null;
        }
    }
}
