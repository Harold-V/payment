package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.ProcessPsePaymentUseCase;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.util.Map;

@Service
public class ProcessPaymentService implements ProcessPsePaymentUseCase {

    private final PaymentGatewayPort paymentGatewayPort;

    public ProcessPaymentService(PaymentGatewayPort paymentGatewayPort) {
        this.paymentGatewayPort = paymentGatewayPort;
    }

    @Override
    public Map<String, Object> procesarPagoPse(PsePaymentRequest request) {
        return paymentGatewayPort.processPsePayment(request);
    }
}
