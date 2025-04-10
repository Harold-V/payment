package tech.xirius.payment.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.*;
import tech.xirius.payment.domain.repository.PaymentRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;
import tech.xirius.payment.infrastructure.persistence.repositories.PaymentJpaRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository repository;

    public PaymentRepositoryAdapter(PaymentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {
        return mapToDomain(repository.save(mapToEntity(payment)));
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    private PaymentEntity mapToEntity(Payment payment) {
        return new PaymentEntity(
                payment.getId(),
                payment.getAmount().getAmount(),
                payment.getStatus().name(),
                payment.getPaymentMethod().name(),
                payment.getProvider().name(),
                payment.getTimestamp());
    }

    private Payment mapToDomain(PaymentEntity entity) {
        return new Payment(
                entity.getId(),
                Money.of(entity.getAmount(), Currency.USD),
                PaymentStatus.valueOf(entity.getStatus()),
                PaymentMethod.valueOf(entity.getPaymentMethod()),
                PaymentProvider.valueOf(entity.getProvider()),
                entity.getTimestamp());
    }
}
