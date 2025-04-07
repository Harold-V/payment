package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import tech.xirius.payment.domain.model.Currency;

public record TransactionResponse(
        UUID id,
        String userId,
        BigDecimal amount,
        Currency currency,
        String type,
        LocalDateTime timestamp,
        UUID paymentId) {
}
