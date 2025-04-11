package tech.xirius.payment.domain.model;

import java.util.UUID;
import tech.xirius.payment.infrastructure.web.exception.InsufficientBalanceException;

import lombok.Getter;

@Getter
public class Wallet {
    private final UUID id;
    private final String userId;
    private Money balance;

    public Wallet(UUID id, String userId, Money balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public void recharge(Money amount) {
        validateCurrency(amount);
        this.balance = this.balance.add(amount);
    }

    public void deduct(Money amount) {
        if (!this.balance.isGreaterThanOrEqual(amount)) {
            throw new InsufficientBalanceException(
                    "Saldo insuficiente.");
        }
        this.balance = this.balance.subtract(amount);
    }

    private void validateCurrency(Money amount) {
        if (!this.balance.getCurrency().equals(amount.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }
}
