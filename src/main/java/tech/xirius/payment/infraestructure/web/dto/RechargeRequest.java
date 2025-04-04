package tech.xirius.payment.infraestructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RechargeRequest(UUID userId, BigDecimal monto) {
}
