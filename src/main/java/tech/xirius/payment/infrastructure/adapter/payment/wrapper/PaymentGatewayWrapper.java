package tech.xirius.payment.infrastructure.adapter.payment.wrapper;

import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentStatus;

public interface PaymentGatewayWrapper {
    Payment processPayment(Payment payment);

    PaymentStatus getStatus(Payment payment);
}
