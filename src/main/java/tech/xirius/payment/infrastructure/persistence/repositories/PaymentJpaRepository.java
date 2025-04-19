package tech.xirius.payment.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByReferenceCode(String referenceCode);

}