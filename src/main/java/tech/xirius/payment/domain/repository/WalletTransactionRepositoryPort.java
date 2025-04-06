package tech.xirius.payment.domain.repository;

import tech.xirius.payment.domain.model.WalletTransaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletTransactionRepositoryPort {
    WalletTransaction save(WalletTransaction transaction);

    Optional<WalletTransaction> findById(UUID id);

    List<WalletTransaction> findByUserId(String userId);
}