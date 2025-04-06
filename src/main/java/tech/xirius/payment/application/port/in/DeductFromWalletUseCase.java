package tech.xirius.payment.application.port.in;

import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;

public interface DeductFromWalletUseCase {
    Wallet deductFromWallet(String userId, Money amount);
}