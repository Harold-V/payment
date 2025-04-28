package tech.xirius.payment.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.infrastructure.web.dto.TransactionResponse;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "currency", constant = "COP")
    @Mapping(source = "transactionId", target = "id")
    @Mapping(target = "userId", ignore = true) // <--- Corrección aquí
    TransactionResponse toResponse(WalletTransaction transaction);
}
