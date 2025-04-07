package tech.xirius.payment.infrastructure.adapter.payment;

import org.springframework.stereotype.Component;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentStatus;
import tech.xirius.payment.infrastructure.adapter.payment.wrapper.PaymentGatewayWrapper;

@Component
public class PaymentGatewayAdapter implements PaymentGatewayPort {

    private final PaymentGatewayWrapper gateway;

    public PaymentGatewayAdapter(PaymentGatewayWrapper gateway) {
        this.gateway = gateway;
    }

    @Override
    public Payment processPayment(Payment payment) {
        return gateway.processPayment(payment);
    }

    @Override
    public PaymentStatus checkPaymentStatus(Payment payment) {
        return gateway.getStatus(payment);
    }
}
