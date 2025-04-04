package tech.xirius.payment.infraestructure.web.dto;

import java.math.BigDecimal;

public record BalanceResponse(BigDecimal saldo) {
}
