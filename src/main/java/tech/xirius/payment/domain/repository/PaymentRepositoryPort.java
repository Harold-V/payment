package tech.xirius.payment.domain.repository;

import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;

public interface PaymentRepositoryPort {
    void save(PaymentEntity payment);
}
