package tech.xirius.payment.infrastructure.persistence.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;

import java.util.Optional;
import java.util.UUID;

public interface WalletTransactionJpaRepository extends JpaRepository<WalletTransactionEntity, UUID> {
    Page<WalletTransactionEntity> findAllByWalletId(UUID walletId, Pageable pageable);

    Optional<WalletTransactionEntity> findByPayment_Id(UUID paymentId);

}
