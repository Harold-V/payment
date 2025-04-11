package tech.xirius.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.GetWalletBalanceUseCase;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GetWalletBalanceService implements GetWalletBalanceUseCase {

    private final WalletRepositoryPort walletRepository;

    @Override
    public BigDecimal getBalanceByUserId(String userId) {
        return walletRepository.findBalanceByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet no encontrada"));
    }
}
