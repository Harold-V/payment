package tech.xirius.payment.application.port.in;

import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.Map;

public interface ProcessPsePaymentUseCase {
    Map<String, Object> processPsePayment(PsePaymentRequest request);
}
