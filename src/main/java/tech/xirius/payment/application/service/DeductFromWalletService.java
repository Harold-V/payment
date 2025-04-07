package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;

import tech.xirius.payment.application.port.in.DeductFromWalletUseCase;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.service.WalletService;

@Service
public class DeductFromWalletService implements DeductFromWalletUseCase {

    private final WalletService walletService;

    public DeductFromWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public Wallet deductFromWallet(String userId, Money amount) {
        return walletService.deductFromWallet(userId, amount);
    }

}
