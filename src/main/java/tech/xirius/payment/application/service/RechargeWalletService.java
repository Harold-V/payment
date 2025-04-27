package tech.xirius.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Servicio de aplicación para recargar saldo en una wallet de usuario.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RechargeWalletService implements RechargeWalletUseCase {

        private final WalletRepositoryPort walletRepository;
        private final WalletTransactionRepositoryPort transactionRepository;

        @Override
        public void recharge(String userId, BigDecimal amount) {
                Wallet wallet = walletRepository.findByUserId(userId)
                                .orElse(new Wallet(UUID.randomUUID(), userId,
                                                new Money(BigDecimal.ZERO, Currency.COP)));

                BigDecimal previousBalance = wallet.getBalance().getAmount();
                wallet.recharge(new Money(amount, Currency.COP));
                walletRepository.save(wallet);

                WalletTransaction tx = WalletTransaction.recharge(wallet.getId(), amount, previousBalance);
                transactionRepository.save(tx);
        }

        @Override
        public void recharge(String userId, BigDecimal amount, UUID paymentId) {
                Wallet wallet = walletRepository.findByUserId(userId)
                                .orElse(new Wallet(UUID.randomUUID(), userId,
                                                new Money(BigDecimal.ZERO, Currency.COP)));

                BigDecimal previousBalance = wallet.getBalance().getAmount();
                wallet.recharge(new Money(amount, Currency.COP));
                walletRepository.save(wallet);

                WalletTransaction tx = new WalletTransaction(
                                UUID.randomUUID(),
                                wallet.getId(),
                                paymentId,
                                amount,
                                "RECHARGE",
                                previousBalance,
                                previousBalance.add(amount),
                                ZonedDateTime.now());

                transactionRepository.save(tx);
        }
}
