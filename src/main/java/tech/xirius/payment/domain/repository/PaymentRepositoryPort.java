package tech.xirius.payment.domain.repository;

import java.util.Optional;

import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;

public interface PaymentRepositoryPort {
    void save(PaymentEntity payment);

    Optional<PaymentEntity> findByReferenceCode(String referenceCode);

}
