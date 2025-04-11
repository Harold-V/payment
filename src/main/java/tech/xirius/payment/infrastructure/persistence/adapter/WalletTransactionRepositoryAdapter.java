package tech.xirius.payment.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;
import tech.xirius.payment.infrastructure.persistence.repositories.WalletTransactionJpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WalletTransactionRepositoryAdapter implements WalletTransactionRepositoryPort {

    private final WalletTransactionJpaRepository transactionJpaRepository;

    @Override
    public void save(WalletTransaction transaction) {
        WalletTransactionEntity entity = toEntity(transaction);
        transactionJpaRepository.save(entity);
    }

    @Override
    public List<WalletTransaction> findAllByWalletId(UUID walletId) {
        return transactionJpaRepository.findAllByWalletId(walletId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private WalletTransactionEntity toEntity(WalletTransaction tx) {
        return new WalletTransactionEntity(
                tx.getTransactionId(),
                tx.getWalletId(),
                tx.getPaymentId(),
                tx.getAmount(),
                tx.getType(),
                tx.getPreviousBalance(),
                tx.getNewBalance(),
                tx.getTimestamp());
    }

    private WalletTransaction toDomain(WalletTransactionEntity entity) {
        return new WalletTransaction(
                entity.getId(),
                entity.getWalletId(),
                entity.getPaymentId(),
                entity.getAmount(),
                entity.getType(),
                entity.getPreviousBalance(),
                entity.getNewBalance(),
                entity.getTimestamp());
    }
}
