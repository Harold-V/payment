package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;
import tech.xirius.payment.domain.model.Currency;

public record RechargeRequest(
        String userId,
        BigDecimal amount,
        Currency currency,
        UUID paymentId) {
}
