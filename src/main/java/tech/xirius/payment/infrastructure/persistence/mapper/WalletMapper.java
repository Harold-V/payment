package tech.xirius.payment.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.infrastructure.persistence.entity.WalletEntity;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(target = "balance.amount", source = "balance")
    @Mapping(target = "balance.currency", constant = "COP")
    Wallet toDomain(WalletEntity entity);

    @Mapping(target = "balance", source = "balance.amount")
    WalletEntity toEntity(Wallet domain);
}
