package tech.xirius.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Modelo de dominio para un Payment.
 */
@Getter
@AllArgsConstructor
public class Payment {
    private UUID id;
    private String referenceCode;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private String provider;
    private ZonedDateTime timestamp;
}
