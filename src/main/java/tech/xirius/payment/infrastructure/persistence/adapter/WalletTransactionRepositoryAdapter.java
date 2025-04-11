package tech.xirius.payment.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

import java.util.List;
import java.util.UUID;

@Component
public class WalletTransactionRepositoryAdapter implements WalletTransactionRepositoryPort {

    @Override
    public void save(WalletTransaction transaction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<WalletTransaction> findAllByWalletId(UUID walletId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByWalletId'");
    }

}
