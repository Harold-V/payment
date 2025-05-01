package tech.xirius.payment.domain.repository;

import tech.xirius.payment.domain.model.WalletTransaction;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletTransactionRepositoryPort {
    void save(WalletTransaction transaction);

    Page<WalletTransaction> findAllByWalletId(UUID walletId, Pageable pageable);

    Optional<WalletTransaction> findByPaymentId(UUID paymentId);

}
