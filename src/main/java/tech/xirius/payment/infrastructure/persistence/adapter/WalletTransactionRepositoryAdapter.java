package tech.xirius.payment.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;
import tech.xirius.payment.infrastructure.persistence.mapper.WalletTransactionMapper;
import tech.xirius.payment.infrastructure.persistence.repositories.WalletTransactionJpaRepository;

import java.util.List;
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
    public List<WalletTransaction> findAllByWalletId(UUID walletId) {
        return transactionJpaRepository.findAllByWalletId(walletId)
                .stream()
                .map(transactionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<WalletTransaction> findByPaymentId(UUID paymentId) {
        return transactionJpaRepository.findByPayment_Id(paymentId)
                .map(transactionMapper::toDomain);
    }

}
