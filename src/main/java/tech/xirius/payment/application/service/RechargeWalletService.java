package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.service.WalletService;

import java.util.UUID;

@Service // 🔥 Necesario para registrar el bean correctamente
public class RechargeWalletService implements RechargeWalletUseCase {

    private final WalletService walletService;

    public RechargeWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public Wallet rechargeWallet(String userId, Money amount, UUID paymentId) {
        return walletService.rechargeWallet(userId, amount, paymentId);
    }
}
