package tech.xirius.payment.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;

import java.util.List;
import java.util.UUID;

public interface WalletTransactionJpaRepository extends JpaRepository<WalletTransactionEntity, UUID> {
    List<WalletTransactionEntity> findAllByWalletId(UUID walletId);
}
