package tech.xirius.payment.domain.repository;

import tech.xirius.payment.domain.model.Wallet;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletRepositoryPort {
    Optional<Wallet> findByUserId(String userId);

    Optional<BigDecimal> findBalanceByUserId(String userId);

    Wallet save(Wallet wallet);
}
