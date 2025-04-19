package tech.xirius.payment.domain.repository;

import tech.xirius.payment.infrastructure.persistence.entity.PaymentMetadataEntity;

public interface PaymentMetadataRepositoryPort {
    void save(PaymentMetadataEntity metadata);
}
