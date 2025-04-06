package tech.xirius.payment.domain.repository;

import tech.xirius.payment.domain.model.Wallet;
import java.util.Optional;

public interface WalletRepositoryPort {
    Optional<Wallet> findByUserId(String userId);

    Wallet save(Wallet wallet);
}