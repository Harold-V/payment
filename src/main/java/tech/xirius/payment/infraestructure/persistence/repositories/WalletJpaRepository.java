package tech.xirius.payment.infraestructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xirius.payment.infraestructure.persistence.entity.WalletEntity;

import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, UUID> {
}
