package tech.xirius.payment.domain.repository;

import tech.xirius.payment.domain.model.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {
    Optional<Wallet> findByUserId(UUID userId);

    Wallet save(Wallet wallet);
}
