package tech.xirius.payment.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.infrastructure.persistence.entity.WalletTransactionEntity;

@Mapper(componentModel = "spring")
public interface WalletTransactionMapper {

    @Mapping(source = "walletId", target = "wallet.id")
    @Mapping(target = "payment", expression = "java(transaction.getPaymentId() != null ? new tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity(transaction.getPaymentId()) : null)")
    @Mapping(source = "transactionId", target = "id")
    WalletTransactionEntity toEntity(WalletTransaction transaction);

    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "payment.id", target = "paymentId")
    @Mapping(source = "id", target = "transactionId")
    WalletTransaction toDomain(WalletTransactionEntity entity);
}
