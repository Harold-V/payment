package tech.xirius.payment.infrastructure.adapter.payment;

import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentStatus;

public class PaymentGatewayAdapter implements PaymentGatewayPort {

    @Override
    public Payment processPayment(Payment payment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }

    @Override
    public PaymentStatus checkPaymentStatus(Payment payment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkPaymentStatus'");
    }

}
