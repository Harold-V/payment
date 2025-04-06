package tech.xirius.payment.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.xirius.payment.infrastructure.persistence.entity.WalletEntity;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, String> {
    Optional<WalletEntity> findByUserId(String userId);
}