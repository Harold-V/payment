package tech.xirius.payment.infraestructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
                UUID transactionId,
                BigDecimal amount,
                String type,
                LocalDateTime timestamp) {
}
