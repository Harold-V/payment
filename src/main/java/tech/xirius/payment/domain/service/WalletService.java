package tech.xirius.payment.domain.service;

import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

import java.util.Optional;
import java.util.UUID;

public class WalletService {
    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort transactionRepository;

    public WalletService(WalletRepositoryPort walletRepository, WalletTransactionRepositoryPort transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public Wallet getOrCreateWallet(String userId, Money initialBalance) {
        return walletRepository.findByUserId(userId)
                .orElseGet(() -> walletRepository.save(new Wallet(userId, initialBalance)));
    }

    public Wallet rechargeWallet(String userId, Money amount, UUID paymentId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Wallet not found for user: " + userId));

        wallet.addFunds(amount);
        WalletTransaction transaction = WalletTransaction.createRechargeTransaction(userId, amount, paymentId);

        transactionRepository.save(transaction);
        return walletRepository.save(wallet);
    }

    public Wallet deductFromWallet(String userId, Money amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Wallet not found for user: " + userId));

        try {
            wallet.deductFunds(amount);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Insufficient funds in wallet");
        }

        WalletTransaction transaction = WalletTransaction.createDeductTransaction(userId, amount);

        transactionRepository.save(transaction);
        return walletRepository.save(wallet);
    }

    public Optional<Wallet> getWallet(String userId) {
        return walletRepository.findByUserId(userId);
    }
}