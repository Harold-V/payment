package tech.xirius.payment.application.port.in;

import tech.xirius.payment.domain.model.WalletTransaction;

import java.util.List;

public interface GetWalletTransactionsUseCase {
    List<WalletTransaction> getTransactionsByUserId(String userId);
}
