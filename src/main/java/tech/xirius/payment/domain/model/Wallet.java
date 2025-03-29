package tech.xirius.payment.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Wallet {
    private UUID userId;
    private BigDecimal balance;

    public Wallet(UUID userId) {
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
    }

    public Wallet(UUID userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public void recargar(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        this.balance = this.balance.add(monto);
    }

    public void descontar(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (this.balance.compareTo(monto) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        this.balance = this.balance.subtract(monto);
    }
}
