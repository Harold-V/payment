package tech.xirius.payment.infrastructure.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.domain.repository.PaymentRepositoryPort;

import java.util.Map;

@RestController
@RequestMapping("/webhook/payu")
@RequiredArgsConstructor
public class PayuWebhookController {

    private final PaymentRepositoryPort paymentRepository;

    @PostMapping("/notification")
    public ResponseEntity<Void> handleNotification(@RequestParam Map<String, String> payload) {
        // Extraer valores
        String referenceCode = payload.get("reference_sale");
        String transactionState = payload.get("state_pol");

        if (referenceCode == null || transactionState == null) {
            return ResponseEntity.badRequest().build();
        }

        // Buscar la transacción
        paymentRepository.findByReferenceCode(referenceCode)
                .ifPresent(payment -> {
                    payment.setStatus(mapStateCode(transactionState));
                    paymentRepository.save(payment);
                });

        return ResponseEntity.ok().build();
    }

    private String mapStateCode(String code) {
        return switch (code) {
            case "4" -> "APPROVED";
            case "6" -> "DECLINED";
            case "7" -> "PENDING";
            default -> "UNKNOWN";
        };
    }
}
