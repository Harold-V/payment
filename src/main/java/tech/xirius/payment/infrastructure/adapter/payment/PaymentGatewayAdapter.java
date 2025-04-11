package tech.xirius.payment.infrastructure.adapter.payment;

import org.springframework.stereotype.Component;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.infrastructure.adapter.payment.wrapper.PaymentGatewayWrapper;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.Map;

@Component
public class PaymentGatewayAdapter implements PaymentGatewayPort {

    private final PaymentGatewayWrapper wrapper;

    public PaymentGatewayAdapter(PaymentGatewayWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public Map<String, Object> processPsePayment(PsePaymentRequest request) {
        return wrapper.processPsePayment(request);
    }
}
