package tech.xirius.payment.application.port.in;

import java.math.BigDecimal;

public interface DeductFromWalletUseCase {
    void deduct(String userId, BigDecimal amount);
}
