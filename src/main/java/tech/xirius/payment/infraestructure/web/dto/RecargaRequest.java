package tech.xirius.payment.infraestructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RecargaRequest(UUID userId, BigDecimal monto) {
}
