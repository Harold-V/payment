package tech.xirius.payment.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.application.port.in.CreatePaymentUseCase;
import tech.xirius.payment.application.port.in.GetPaymentStatusUseCase;
import tech.xirius.payment.application.port.in.ProcessPaymentUseCase;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.infrastructure.web.dto.PaymentRequest;
import tech.xirius.payment.infrastructure.web.dto.PaymentResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final GetPaymentStatusUseCase getPaymentStatusUseCase;

    public PaymentController(
            CreatePaymentUseCase createPaymentUseCase,
            ProcessPaymentUseCase processPaymentUseCase,
            GetPaymentStatusUseCase getPaymentStatusUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.getPaymentStatusUseCase = getPaymentStatusUseCase;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createAndProcessPayment(@RequestBody PaymentRequest request) {
        Money amount = Money.of(request.amount(), request.currency());
        Payment payment = processPaymentUseCase.processPayment(
                request.userId(),
                amount,
                request.paymentMethod(),
                request.provider());

        return ResponseEntity.ok(toDto(payment));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable UUID paymentId) {
        Payment payment = getPaymentStatusUseCase.getPaymentStatus(paymentId);
        return ResponseEntity.ok(toDto(payment));
    }

    private PaymentResponse toDto(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount().getAmount(),
                payment.getAmount().getCurrency(),
                payment.getPaymentMethod(),
                payment.getProvider(),
                payment.getStatus(),
                payment.getTimestamp());
    }
}
