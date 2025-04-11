package tech.xirius.payment.application.port.in;

import java.math.BigDecimal;

public interface GetWalletBalanceUseCase {
    BigDecimal getBalanceByUserId(String userId);
}
