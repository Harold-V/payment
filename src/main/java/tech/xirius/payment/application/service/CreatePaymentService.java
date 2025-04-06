package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.CreatePaymentUseCase;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentMethod;
import tech.xirius.payment.domain.model.PaymentProvider;
import tech.xirius.payment.domain.service.PaymentService;

@Service
public class CreatePaymentService implements CreatePaymentUseCase {

    private final PaymentService paymentService;

    public CreatePaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Payment createPayment(String userId, Money amount, PaymentMethod paymentMethod, PaymentProvider provider) {
        Payment payment = Payment.createPayment(userId, amount, paymentMethod, provider);
        return paymentService.createPayment(payment);
    }
}