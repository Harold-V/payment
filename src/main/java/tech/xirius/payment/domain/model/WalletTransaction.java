package tech.xirius.payment.domain.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WalletTransaction {
    public enum Type {
        RECARGA, COMPRA // Debito y credito
    }

    private UUID transactionId;
    private UUID userId; // String mongo objectId
    private BigDecimal amount;
    private Type type;
    private ZonedDateTime timestamp;

}
