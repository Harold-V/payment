package tech.xirius.payment.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;
import tech.xirius.payment.infrastructure.persistence.entity.WalletEntity;
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
        WalletTransactionEntity entity = new WalletTransactionEntity();
        entity.setId(tx.getTransactionId());

        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setId(tx.getWalletId());
        entity.setWallet(walletEntity);

        if (tx.getPaymentId() != null) {
            PaymentEntity paymentEntity = new PaymentEntity();
            paymentEntity.setId(tx.getPaymentId());
            entity.setPayment(paymentEntity);
        }

        entity.setAmount(tx.getAmount());
        entity.setType(tx.getType());
        entity.setPreviousBalance(tx.getPreviousBalance());
        entity.setNewBalance(tx.getNewBalance());
        entity.setTimestamp(tx.getTimestamp());

        return entity;
    }

    private WalletTransaction toDomain(WalletTransactionEntity entity) {
        return new WalletTransaction(
                entity.getId(),
                entity.getWallet().getId(),
                entity.getPayment() != null ? entity.getPayment().getId() : null,
                entity.getAmount(),
                entity.getType(),
                entity.getPreviousBalance(),
                entity.getNewBalance(),
                entity.getTimestamp());
    }

}
