package tech.xirius.payment.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Wallet {
    private final UUID id;
    private final String userId;
    private Money balance;

    public Wallet(UUID id, String userId, Money balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Money getBalance() {
        return balance;
    }

    public void addFunds(Money amount) {
        if (!this.balance.getCurrency().equals(amount.getCurrency())) {
            throw new IllegalArgumentException("Cannot add funds with different currency");
        }
        this.balance = this.balance.add(amount);
    }

    public void deductFunds(Money amount) {
        if (!this.balance.getCurrency().equals(amount.getCurrency())) {
            throw new IllegalArgumentException("Cannot deduct funds with different currency");
        }
        if (this.balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(userId, wallet.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}