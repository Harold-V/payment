package tech.xirius.payment.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.xirius.payment.infrastructure.persistence.entity.WalletEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByUserId(String userId);

    @Query("SELECT w.balance FROM WalletEntity w WHERE w.userId = :userId")
    Optional<BigDecimal> findBalanceByUserId(String userId);
}
