package tech.xirius.payment.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.repository.PaymentRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;
import tech.xirius.payment.infrastructure.persistence.repositories.PaymentJpaRepository;

@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository jpaRepository;

    public PaymentRepositoryAdapter(PaymentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(PaymentEntity payment) {
        jpaRepository.save(payment);
    }

    @Override
    public Optional<PaymentEntity> findByReferenceCode(String referenceCode) {
        return jpaRepository.findByReferenceCode(referenceCode);
    }

}
