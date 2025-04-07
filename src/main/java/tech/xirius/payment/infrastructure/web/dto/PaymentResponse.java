package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.PaymentMethod;
import tech.xirius.payment.domain.model.PaymentProvider;
import tech.xirius.payment.domain.model.PaymentStatus;

public record PaymentResponse(
        UUID id,
        String userId,
        BigDecimal amount,
        Currency currency,
        PaymentMethod paymentMethod,
        PaymentProvider provider,
        PaymentStatus status,
        LocalDateTime timestamp) {
}
