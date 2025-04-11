package tech.xirius.payment.domain.repository;

import tech.xirius.payment.domain.model.WalletTransaction;
import java.util.List;
import java.util.UUID;

public interface WalletTransactionRepositoryPort {
    void save(WalletTransaction transaction);

    List<WalletTransaction> findAllByWalletId(UUID walletId);
}
