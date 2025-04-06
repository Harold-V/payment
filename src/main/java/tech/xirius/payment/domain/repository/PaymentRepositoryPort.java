package tech.xirius.payment.domain.repository;

import tech.xirius.payment.domain.model.Payment;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);
}