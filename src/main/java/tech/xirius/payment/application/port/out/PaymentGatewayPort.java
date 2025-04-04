package tech.xirius.payment.application.port.out;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGatewayPort {
    boolean procesarPago(String referencia, BigDecimal monto, UUID userId); //
}
