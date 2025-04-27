package tech.xirius.payment.infrastructure.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.application.port.in.ProcessPsePaymentUseCase;
import tech.xirius.payment.infrastructure.adapter.payment.wrapper.impl.PayuWrapper;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.List;
import java.util.Map;

/**
 * Recurso REST para procesar pagos a través de PayU.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PayuWrapper payuWrapper;
    private final ProcessPsePaymentUseCase processPsePaymentUseCase;

    @PostMapping("/pse")
    public ResponseEntity<Map<String, Object>> processPsePayment(@RequestBody PsePaymentRequest request) {
        log.info("Procesando pago PSE para usuario: {}", request.getUserId());
        Map<String, Object> response = processPsePaymentUseCase.processPsePayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/banks")
    public ResponseEntity<List<Map<String, Object>>> getAvailableBanks() {
        log.info("Obteniendo bancos disponibles para PSE");
        List<Map<String, Object>> banks = payuWrapper.getAvailableBanks();
        return ResponseEntity.ok(banks);
    }
}
