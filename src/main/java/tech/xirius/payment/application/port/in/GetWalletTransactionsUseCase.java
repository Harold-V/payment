package tech.xirius.payment.application.port.in;

import tech.xirius.payment.domain.model.WalletTransaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetWalletTransactionsUseCase {
    Page<WalletTransaction> getTransactionsByUserId(String userId, Pageable pageable);
}
