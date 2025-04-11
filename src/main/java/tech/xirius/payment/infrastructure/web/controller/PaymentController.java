package tech.xirius.payment.infrastructure.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.xirius.payment.application.port.in.ProcessPsePaymentUseCase;
import tech.xirius.payment.infrastructure.adapter.payment.wrapper.impl.PayuWrapper;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PayuWrapper payuWrapper;
    private final ProcessPsePaymentUseCase processPsePaymentUseCase;

    @PostMapping("/pse")
    public ResponseEntity<Map<String, Object>> procesarPagoPse(@RequestBody PsePaymentRequest request) {
        Map<String, Object> respuesta = processPsePaymentUseCase.procesarPagoPse(request);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/banks")
    public ResponseEntity<List<Map<String, Object>>> getAvailableBanks() {
        List<Map<String, Object>> banks = payuWrapper.getAvailableBanks();
        return ResponseEntity.ok(banks);
    }
}
