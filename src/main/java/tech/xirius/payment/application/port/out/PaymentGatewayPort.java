package tech.xirius.payment.application.port.out;

import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.Map;

public interface PaymentGatewayPort {
    Map<String, Object> processPsePayment(PsePaymentRequest request);
}
