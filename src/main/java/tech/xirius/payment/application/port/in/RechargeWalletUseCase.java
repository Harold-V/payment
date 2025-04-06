package tech.xirius.payment.application.port.in;

import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;

import java.util.UUID;

public interface RechargeWalletUseCase {
    Wallet rechargeWallet(String userId, Money amount, UUID paymentId);
}