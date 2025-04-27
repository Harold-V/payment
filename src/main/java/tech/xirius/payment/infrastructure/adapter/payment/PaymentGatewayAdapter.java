package tech.xirius.payment.infrastructure.adapter.payment;

import org.springframework.stereotype.Component;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.Map;

@Component
public class PaymentGatewayAdapter implements PaymentGatewayPort {

    private final PaymentGatewayPort paymentGatewayPort;

    public PaymentGatewayAdapter(PaymentGatewayPort paymentGatewayPort) {
        this.paymentGatewayPort = paymentGatewayPort;
    }

    @Override
    public Map<String, Object> processPsePayment(PsePaymentRequest request) {
        return paymentGatewayPort.processPsePayment(request);
    }
}
