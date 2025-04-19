package tech.xirius.payment.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentMetadataEntity;

import java.util.UUID;

public interface PaymentMetadataJpaRepository extends JpaRepository<PaymentMetadataEntity, UUID> {
}
