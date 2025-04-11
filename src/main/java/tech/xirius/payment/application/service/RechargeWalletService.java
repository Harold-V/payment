package tech.xirius.payment.application.service;

import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class RechargeWalletService implements RechargeWalletUseCase {

    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort transactionRepository;

    public RechargeWalletService(WalletRepositoryPort walletRepository,
            WalletTransactionRepositoryPort transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void recharge(String userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElse(new Wallet(UUID.randomUUID(), userId, new Money(BigDecimal.ZERO, Currency.COP)));

        BigDecimal previousBalance = wallet.getBalance().getAmount();
        wallet.recharge(new Money(amount, Currency.COP));
        walletRepository.save(wallet);

        WalletTransaction tx = WalletTransaction.recharge(wallet.getId(), amount,
                previousBalance);
        transactionRepository.save(tx);

    }
}
