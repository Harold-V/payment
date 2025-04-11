package tech.xirius.payment.infrastructure.adapter.payment.wrapper;

import java.util.Map;

import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

public interface PaymentGatewayWrapper {
    Map<String, Object> processPsePayment(PsePaymentRequest request);
}
