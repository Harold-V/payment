package tech.xirius.payment.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.WalletEntity;
import tech.xirius.payment.infrastructure.persistence.repositories.WalletJpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class WalletRepositoryAdapter implements WalletRepositoryPort {

    private final WalletJpaRepository walletJpaRepository;

    public WalletRepositoryAdapter(WalletJpaRepository walletJpaRepository) {
        this.walletJpaRepository = walletJpaRepository;
    }

    @Override
    public Optional<Wallet> findByUserId(String userId) {
        return walletJpaRepository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = toEntity(wallet);
        WalletEntity saved = walletJpaRepository.save(entity);
        return toDomain(saved);
    }

    private WalletEntity toEntity(Wallet wallet) {
        WalletEntity entity = new WalletEntity();
        entity.setId(wallet.getId());
        entity.setUserId(wallet.getUserId());
        entity.setBalance(wallet.getBalance().getAmount());
        return entity;
    }

    private Wallet toDomain(WalletEntity entity) {
        return new Wallet(
                entity.getId(),
                entity.getUserId(),
                new Money(
                        entity.getBalance(),
                        Currency.COP));
    }

    @Override
    public Optional<BigDecimal> findBalanceByUserId(String userId) {
        return walletJpaRepository.findBalanceByUserId(userId);
    }

}
