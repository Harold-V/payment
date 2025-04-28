package tech.xirius.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Entidad que representa un pago registrado en el sistema.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment", uniqueConstraints = {
        @UniqueConstraint(columnNames = "payment_id")
})
public class PaymentEntity {

    @Id
    @Column(name = "payment_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "reference_code", nullable = false, unique = true)
    private String referenceCode; // <-- 🔥 AGREGA ESTO

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    // Constructor completo
    public PaymentEntity(UUID id, String referenceCode, BigDecimal amount, String status,
            String paymentMethod, String provider, ZonedDateTime timestamp) {
        this.id = id;
        this.referenceCode = referenceCode;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.provider = provider;
        this.timestamp = timestamp;
    }

    public PaymentEntity(UUID id) {
        this.id = id;
    }
}
