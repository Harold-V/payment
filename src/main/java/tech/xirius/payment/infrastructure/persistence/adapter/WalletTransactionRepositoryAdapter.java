package tech.xirius.payment.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;
import tech.xirius.payment.infrastructure.persistence.mapper.WalletTransactionMapper;
import tech.xirius.payment.infrastructure.persistence.repositories.WalletTransactionJpaRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class WalletTransactionRepositoryAdapter implements WalletTransactionRepositoryPort {

    private final WalletTransactionJpaRepository transactionJpaRepository;
    private final WalletTransactionMapper transactionMapper;

    @Override
    public void save(WalletTransaction transaction) {
        WalletTransactionEntity entity = transactionMapper.toEntity(transaction);
        transactionJpaRepository.save(entity);
    }

    @Override
    public Page<WalletTransaction> findAllByWalletId(UUID walletId, Pageable pageable) {
        return transactionJpaRepository.findAllByWalletId(walletId, pageable)
                .map(transactionMapper::toDomain);
    }

    @Override
    public Optional<WalletTransaction> findByPaymentId(UUID paymentId) {
        return transactionJpaRepository.findByPayment_Id(paymentId)
                .map(transactionMapper::toDomain);
    }

}
