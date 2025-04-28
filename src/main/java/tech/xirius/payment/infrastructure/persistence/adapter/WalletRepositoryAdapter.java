package tech.xirius.payment.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.WalletEntity;
import tech.xirius.payment.infrastructure.persistence.mapper.WalletMapper;
import tech.xirius.payment.infrastructure.persistence.repositories.WalletJpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class WalletRepositoryAdapter implements WalletRepositoryPort {

    private final WalletJpaRepository walletJpaRepository;
    private final WalletMapper walletMapper;

    @Override
    public Optional<Wallet> findByUserId(String userId) {
        return walletJpaRepository.findByUserId(userId)
                .map(walletMapper::toDomain);
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = walletMapper.toEntity(wallet);
        WalletEntity saved = walletJpaRepository.save(entity);
        return walletMapper.toDomain(saved);
    }

    @Override
    public Optional<BigDecimal> findBalanceByUserId(String userId) {
        return walletJpaRepository.findBalanceByUserId(userId);
    }
}
