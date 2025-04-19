package tech.xirius.payment.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.repository.PaymentMetadataRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentMetadataEntity;
import tech.xirius.payment.infrastructure.persistence.repositories.PaymentMetadataJpaRepository;

@Component
public class PaymentMetadataRepositoryAdapter implements PaymentMetadataRepositoryPort {

    private final PaymentMetadataJpaRepository jpaRepository;

    public PaymentMetadataRepositoryAdapter(PaymentMetadataJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(PaymentMetadataEntity metadata) {
        jpaRepository.save(metadata);
    }

    @Override
    public Optional<PaymentMetadataEntity> findById(UUID paymentId) {
        return jpaRepository.findById(paymentId);
    }

}
