package tech.xirius.payment.application.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import tech.xirius.payment.application.port.in.DeductFromWalletUseCase;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

@Service
public class DeductFromWalletService implements DeductFromWalletUseCase {

    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort transactionRepository;

    public DeductFromWalletService(WalletRepositoryPort walletRepository,
            WalletTransactionRepositoryPort transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void deduct(String userId, BigDecimal amount, Currency currency) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Validación de moneda
        if (!wallet.getBalance().getCurrency().equals(currency)) {
            throw new IllegalArgumentException("Currency mismatch: expected " +
                    wallet.getBalance().getCurrency() + " but received " + currency);
        }

        Money moneyToDeduct = new Money(amount, currency);
        BigDecimal previousBalance = wallet.getBalance().getAmount();

        wallet.deduct(moneyToDeduct);
        walletRepository.save(wallet);

        WalletTransaction tx = WalletTransaction.purchase(wallet.getId(), null, amount, previousBalance);
        transactionRepository.save(tx);
    }
}
