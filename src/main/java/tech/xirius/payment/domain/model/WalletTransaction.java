package tech.xirius.payment.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WalletTransaction {
    public enum Type {
        RECARGA, COMPRA
    }

    private UUID transactionId;
    private UUID userId;
    private BigDecimal amount;
    private Type type;
    private LocalDateTime timestamp;

}
