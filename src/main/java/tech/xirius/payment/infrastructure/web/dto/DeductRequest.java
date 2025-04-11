package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;

import lombok.Getter;
import tech.xirius.payment.domain.model.Currency;

@Getter
public class DeductRequest {
    private String userId;
    private BigDecimal amount;
    private Currency currency;
}
