package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class DeductRequest {
    private String userId;
    private BigDecimal amount;
}
