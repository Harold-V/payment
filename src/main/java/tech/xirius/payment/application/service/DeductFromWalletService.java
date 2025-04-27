package tech.xirius.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.xirius.payment.application.port.in.DeductFromWalletUseCase;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

import java.math.BigDecimal;

/**
 * Servicio de aplicación para descontar saldo de la wallet de un usuario.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DeductFromWalletService implements DeductFromWalletUseCase {

    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort transactionRepository;

    @Override
    public void deduct(String userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet no encontrada para el usuario: " + userId));

        BigDecimal previousBalance = wallet.getBalance().getAmount();
        wallet.deduct(new Money(amount, Currency.COP));
        walletRepository.save(wallet);

        WalletTransaction transaction = WalletTransaction.purchase(wallet.getId(), null, amount, previousBalance);
        transactionRepository.save(transaction);
    }
}
