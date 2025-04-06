package tech.xirius.payment.application.port.out;

import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentStatus;

public interface PaymentGatewayPort {
    Payment processPayment(Payment payment);

    PaymentStatus checkPaymentStatus(Payment payment);
}