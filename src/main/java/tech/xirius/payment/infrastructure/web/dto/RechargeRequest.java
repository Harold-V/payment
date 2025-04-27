package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

/**
 * DTO para solicitud de recarga de saldo en la wallet.
 */
@Getter
public class RechargeRequest {

    @NotNull(message = "El userId no puede ser nulo")
    private String userId;

    @NotNull(message = "El amount no puede ser nulo")
    @Positive(message = "El amount debe ser un número positivo")
    private BigDecimal amount;
}
