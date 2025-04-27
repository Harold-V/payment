package tech.xirius.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

/**
 * Entidad que almacena los metadatos asociados a un pago.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment_metadata")
public class PaymentMetadataEntity {

    @Id
    @Column(name = "payment_id", nullable = false, updatable = false)
    private UUID paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_payment_metadata_payment"))
    private PaymentEntity payment;

    @Column(nullable = false, length = 50)
    private String provider;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "json_metadata", columnDefinition = "jsonb")
    private String jsonMetadata;

    public PaymentMetadataEntity(UUID paymentId, String provider, String jsonMetadata) {
        this.paymentId = paymentId;
        this.provider = provider;
        this.jsonMetadata = jsonMetadata;
    }
}
