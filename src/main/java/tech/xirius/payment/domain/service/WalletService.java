package tech.xirius.payment.domain.service;

import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class WalletService {
    private final WalletRepositoryPort walletRepository;

    private final WalletTransactionRepositoryPort transactionRepository;

    public WalletService(WalletRepositoryPort walletRepository,
            WalletTransactionRepositoryPort transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public void recargarSaldo(UUID userId, BigDecimal monto) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElse(new Wallet(userId));
        wallet.recargar(monto);
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction(
                UUID.randomUUID(), userId, monto,
                WalletTransaction.Type.RECARGA, LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void descontarSaldo(UUID userId, BigDecimal monto) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("La billetera no existe"));
        wallet.descontar(monto);
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction(
                UUID.randomUUID(), userId, monto.negate(),
                WalletTransaction.Type.COMPRA, LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public BigDecimal consultarSaldo(UUID userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("La billetera no existe"));
        return wallet.getBalance();
    }
}
