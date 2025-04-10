package tech.xirius.payment.domain.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class WalletTransaction {
    private final UUID id;
    private final UUID walletId;
    private final Money amount;
    private final String type;
    private final ZonedDateTime timestamp;
    private final UUID paymentId;

    public WalletTransaction(UUID id, UUID walletId, Money amount, String type, ZonedDateTime timestamp,
            UUID paymentId) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.paymentId = paymentId;
    }

    // Constructor para transacciones sin payment asociado
    public WalletTransaction(UUID id, UUID walletId, Money amount, String type, ZonedDateTime timestamp) {
        this(id, walletId, amount, type, timestamp, null);
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public Money getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    // Factory methods
    public static WalletTransaction createRechargeTransaction(UUID walletId, Money amount, UUID paymentId) {
        return new WalletTransaction(UUID.randomUUID(), walletId, amount, "RECHARGE", ZonedDateTime.now(), paymentId);
    }

    public static WalletTransaction createPurchaseTransaction(UUID walletId, Money amount) {
        return new WalletTransaction(UUID.randomUUID(), walletId, amount, "PURCHASE", ZonedDateTime.now(), null);
    }

    public static WalletTransaction createRefundTransaction(UUID walletId, Money amount) {
        return new WalletTransaction(UUID.randomUUID(), walletId, amount, "REFUND", ZonedDateTime.now(), null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WalletTransaction that = (WalletTransaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}