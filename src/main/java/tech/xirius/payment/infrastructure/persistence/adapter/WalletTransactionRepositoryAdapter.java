package tech.xirius.payment.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;
import tech.xirius.payment.infrastructure.persistence.repositories.WalletTransactionJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WalletTransactionRepositoryAdapter implements WalletTransactionRepositoryPort {

    private final WalletTransactionJpaRepository repository;

    public WalletTransactionRepositoryAdapter(WalletTransactionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public WalletTransaction save(WalletTransaction transaction) {
        WalletTransactionEntity entity = mapToEntity(transaction);
        return mapToDomain(repository.save(entity));
    }

    @Override
    public Optional<WalletTransaction> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<WalletTransaction> findByUserId(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private WalletTransactionEntity mapToEntity(WalletTransaction tx) {
        return new WalletTransactionEntity(
                tx.getId(),
                tx.getUserId(),
                tx.getAmount().getAmount(),
                tx.getType(),
                tx.getTimestamp(),
                tx.getPaymentId());
    }

    private WalletTransaction mapToDomain(WalletTransactionEntity entity) {
        return new WalletTransaction(
                entity.getId(),
                entity.getUserId(),
                Money.of(entity.getAmount(), Currency.USD),
                entity.getType(),
                entity.getTimestamp(),
                entity.getPaymentId());
    }
}
