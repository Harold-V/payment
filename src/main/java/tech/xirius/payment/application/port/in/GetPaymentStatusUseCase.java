package tech.xirius.payment.application.port.in;

import tech.xirius.payment.domain.model.Payment;
import java.util.UUID;

public interface GetPaymentStatusUseCase {
    Payment getPaymentStatus(UUID paymentId);
}