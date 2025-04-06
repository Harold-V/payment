package tech.xirius.payment.application.port.in;

import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentMethod;
import tech.xirius.payment.domain.model.PaymentProvider;

public interface CreatePaymentUseCase {
    Payment createPayment(String userId, Money amount, PaymentMethod paymentMethod, PaymentProvider provider);
}