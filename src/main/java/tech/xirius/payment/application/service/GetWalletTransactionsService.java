package tech.xirius.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.GetWalletTransactionsUseCase;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.domain.repository.WalletTransactionRepositoryPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWalletTransactionsService implements GetWalletTransactionsUseCase {

    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort transactionRepository;

    @Override
    public List<WalletTransaction> getTransactionsByUserId(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return transactionRepository.findAllByWalletId(wallet.getId());
    }
}
