package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.GetPaymentStatusUseCase;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.service.PaymentService;

import java.util.UUID;

@Service
public class PaymentStatusService implements GetPaymentStatusUseCase {

    private final PaymentService paymentService;

    public PaymentStatusService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Payment getPaymentStatus(UUID paymentId) {
        return paymentService.getPaymentById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
    }
}