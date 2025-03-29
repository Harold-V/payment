package tech.xirius.payment.application.port.out;

import java.math.BigDecimal;
import java.util.UUID;

public interface PayUGateway {
    boolean procesarPago(String referencia, BigDecimal monto, UUID userId);
}
