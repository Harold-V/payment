package tech.xirius.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Entidad que representa una transacción en la wallet de un usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "wallet_transaction", uniqueConstraints = {
        @UniqueConstraint(columnNames = "transaction_id")
}, indexes = {
        @Index(name = "idx_wallet_transaction_wallet_id", columnList = "wallet_id"),
        @Index(name = "idx_wallet_transaction_payment_id", columnList = "payment_id")
})
public class WalletTransactionEntity {

    @Id
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wallet_tx_wallet"))
    private WalletEntity wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "fk_wallet_tx_payment"))
    private PaymentEntity payment;

    @Column(nullable = false, precision = 38, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(name = "previous_balance", nullable = false, precision = 38, scale = 2)
    private BigDecimal previousBalance;

    @Column(name = "new_balance", nullable = false, precision = 38, scale = 2)
    private BigDecimal newBalance;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    @PrePersist
    private void validate() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (type == null || (!type.equals("RECHARGE") && !type.equals("PURCHASE") && !type.equals("REFUND"))) {
            throw new IllegalArgumentException("Tipo de transacción inválido: " + type);
        }
    }
}
