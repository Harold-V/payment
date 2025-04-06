package tech.xirius.payment.application.port.in;

import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentMethod;
import tech.xirius.payment.domain.model.PaymentProvider;
import tech.xirius.payment.domain.model.Money;

import java.util.UUID;

public interface ProcessPaymentUseCase {
    Payment processPayment(String userId, Money amount, PaymentMethod paymentMethod, PaymentProvider provider);

    Payment getPaymentStatus(UUID paymentId);
}