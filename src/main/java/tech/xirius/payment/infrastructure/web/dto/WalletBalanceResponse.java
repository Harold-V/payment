package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;

public record WalletBalanceResponse(
        String userId,
        BigDecimal balance,
        String currency) {
}
