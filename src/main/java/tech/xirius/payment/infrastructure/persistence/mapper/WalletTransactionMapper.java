package tech.xirius.payment.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;

@Mapper(componentModel = "spring")
public interface WalletTransactionMapper {

    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "payment.id", target = "paymentId")
    @Mapping(source = "id", target = "transactionId")
    WalletTransaction toDomain(WalletTransactionEntity entity);

    @Mapping(source = "walletId", target = "wallet.id")
    @Mapping(source = "paymentId", target = "payment.id")
    @Mapping(source = "transactionId", target = "id")
    WalletTransactionEntity toEntity(WalletTransaction domain);
}
