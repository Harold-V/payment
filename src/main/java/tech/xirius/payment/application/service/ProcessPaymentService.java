package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.ProcessPaymentUseCase;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.domain.model.*;
import tech.xirius.payment.domain.service.PaymentService;

import java.util.UUID;

@Service
public class ProcessPaymentService implements ProcessPaymentUseCase {

    private final PaymentService paymentService;
    private final PaymentGatewayPort paymentGatewayPort;

    public ProcessPaymentService(PaymentService paymentService, PaymentGatewayPort paymentGatewayPort) {
        this.paymentService = paymentService;
        this.paymentGatewayPort = paymentGatewayPort;
    }

    @Override
    public Payment processPayment(String userId, Money amount, PaymentMethod paymentMethod, PaymentProvider provider) {
        // Create a new payment
        Payment payment = Payment.createPayment(userId, amount, paymentMethod, provider);
        payment = paymentService.createPayment(payment);

        // Process payment through the payment gateway
        Payment processedPayment = paymentGatewayPort.processPayment(payment);

        return processedPayment;
    }

    @Override
    public Payment getPaymentStatus(UUID paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        // Check with the payment gateway for the latest status
        PaymentStatus latestStatus = paymentGatewayPort.checkPaymentStatus(payment);

        // Update the payment status if it has changed
        if (payment.getStatus() != latestStatus) {
            payment = paymentService.updatePaymentStatus(paymentId, latestStatus);
        }

        return payment;
    }
}