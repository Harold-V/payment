package tech.xirius.payment.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.repository.WalletRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.WalletEntity;
import tech.xirius.payment.infrastructure.persistence.repositories.WalletJpaRepository;

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
                .map(this::mapToDomain);
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = mapToEntity(wallet);
        WalletEntity savedEntity = walletJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    private Wallet mapToDomain(WalletEntity entity) {
        Money balance = Money.of(entity.getBalance(), Currency.USD);
        return new Wallet(entity.getId, entity.getUserId(), balance);
    }

    private WalletEntity mapToEntity(Wallet wallet) {
        return new WalletEntity(wallet.getUserId(), wallet.getBalance().getAmount());
    }
}