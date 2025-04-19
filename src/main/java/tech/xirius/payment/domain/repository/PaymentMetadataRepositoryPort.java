package tech.xirius.payment.domain.repository;

import java.util.Optional;
import java.util.UUID;

import tech.xirius.payment.infrastructure.persistence.entity.PaymentMetadataEntity;

public interface PaymentMetadataRepositoryPort {
    void save(PaymentMetadataEntity metadata);

    Optional<PaymentMetadataEntity> findById(UUID paymentId);

}
