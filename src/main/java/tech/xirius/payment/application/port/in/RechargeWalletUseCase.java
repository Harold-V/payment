package tech.xirius.payment.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface RechargeWalletUseCase {
    void recharge(String userId, BigDecimal amount);

    void recharge(String userId, BigDecimal amount, UUID paymentId);

}
