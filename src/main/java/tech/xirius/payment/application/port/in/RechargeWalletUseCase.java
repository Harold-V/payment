package tech.xirius.payment.application.port.in;

import java.math.BigDecimal;

public interface RechargeWalletUseCase {
    void recharge(String userId, BigDecimal amount);
}
