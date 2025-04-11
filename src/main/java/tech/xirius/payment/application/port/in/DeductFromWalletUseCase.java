package tech.xirius.payment.application.port.in;

import java.math.BigDecimal;
import tech.xirius.payment.domain.model.Currency;

public interface DeductFromWalletUseCase {
    void deduct(String userId, BigDecimal amount, Currency currency);
}
