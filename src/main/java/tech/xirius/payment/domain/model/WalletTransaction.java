package tech.xirius.payment.domain.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class WalletTransaction {
    private final UUID transactionId;
    private final UUID walletId;
    private final UUID paymentId;
    private final BigDecimal amount;
    private final String type;
    private final BigDecimal previousBalance;
    private final BigDecimal newBalance;
    private final ZonedDateTime timestamp;

    public WalletTransaction(UUID transactionId, UUID walletId, UUID paymentId,
            BigDecimal amount, String type, BigDecimal previousBalance,
            BigDecimal newBalance, ZonedDateTime timestamp) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.type = type;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
        this.timestamp = timestamp;
    }

    public static WalletTransaction recharge(UUID walletId, BigDecimal amount, BigDecimal previousBalance) {
        return new WalletTransaction(
                UUID.randomUUID(),
                walletId,
                null,
                amount,
                "RECHARGE",
                previousBalance,
                previousBalance.add(amount),
                ZonedDateTime.now());
    }

    public static WalletTransaction purchase(UUID walletId, UUID paymentId, BigDecimal amount,
            BigDecimal previousBalance) {
        return new WalletTransaction(
                UUID.randomUUID(),
                walletId,
                paymentId,
                amount,
                "PURCHASE",
                previousBalance,
                previousBalance.subtract(amount),
                ZonedDateTime.now());
    }
}
