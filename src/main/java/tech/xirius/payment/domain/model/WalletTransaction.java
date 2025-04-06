package tech.xirius.payment.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class WalletTransaction {
    private final UUID id;
    private final String userId;
    private final Money amount;
    private final String type;
    private final LocalDateTime timestamp;
    private final UUID paymentId;

    public WalletTransaction(UUID id, String userId, Money amount, String type, LocalDateTime timestamp,
            UUID paymentId) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.paymentId = paymentId;
    }

    // Constructor para transacciones sin payment asociado
    public WalletTransaction(UUID id, String userId, Money amount, String type, LocalDateTime timestamp) {
        this(id, userId, amount, type, timestamp, null);
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Money getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    // Factory methods
    public static WalletTransaction createRechargeTransaction(String userId, Money amount, UUID paymentId) {
        return new WalletTransaction(
                UUID.randomUUID(),
                userId,
                amount,
                "RECHARGE",
                LocalDateTime.now(),
                paymentId);
    }

    public static WalletTransaction createDeductTransaction(String userId, Money amount) {
        return new WalletTransaction(
                UUID.randomUUID(),
                userId,
                amount,
                "DEDUCT",
                LocalDateTime.now());
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