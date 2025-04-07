package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.PaymentMethod;
import tech.xirius.payment.domain.model.PaymentProvider;

public record PaymentRequest(
        String userId,
        BigDecimal amount,
        Currency currency,
        PaymentMethod paymentMethod,
        PaymentProvider provider) {
}
