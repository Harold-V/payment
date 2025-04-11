package tech.xirius.payment.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "wallet_transaction")
public class WalletTransactionEntity {

    @Id
    @Column(name = "transaction_id")
    private UUID id;

    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(nullable = false, precision = 38, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String type;

    @Column(name = "previous_balance", nullable = false, precision = 38, scale = 2)
    private BigDecimal previousBalance;

    @Column(name = "new_balance", nullable = false, precision = 38, scale = 2)
    private BigDecimal newBalance;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    public WalletTransactionEntity() {
    }

    public WalletTransactionEntity(UUID id, UUID walletId, UUID paymentId,
            BigDecimal amount, String type, BigDecimal previousBalance,
            BigDecimal newBalance, ZonedDateTime timestamp) {
        this.id = id;
        this.walletId = walletId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.type = type;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
        this.timestamp = timestamp;
    }
}
