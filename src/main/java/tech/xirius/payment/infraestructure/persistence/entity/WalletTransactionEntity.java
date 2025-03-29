package tech.xirius.payment.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "wallet_transactions")
public class WalletTransactionEntity {

    @Id
    private UUID transactionId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public enum Type {
        RECARGA, COMPRA
    }

    public WalletTransactionEntity() {
    }

    public WalletTransactionEntity(UUID transactionId, UUID userId, BigDecimal amount, Type type,
            LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }
}
