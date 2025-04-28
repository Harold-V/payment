package tech.xirius.payment.infrastructure.adapter.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.infrastructure.adapter.payment.wrapper.impl.PayuWrapper;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.Map;

/**
 * Adaptador para delegar operaciones de gateway de pago.
 */
@Component
@RequiredArgsConstructor
public class PaymentGatewayAdapter implements PaymentGatewayPort {

    private final PayuWrapper payuWrapper;

    @Override
    public Map<String, Object> processPsePayment(PsePaymentRequest request) {
        return payuWrapper.processPsePayment(request);
    }
}
