package tech.xirius.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment_metadata")
public class PaymentMetadataEntity {

    @Id
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(nullable = false)
    private String provider;

    @Column(name = "json_metadata", columnDefinition = "jsonb")
    private String jsonMetadata;

    public PaymentMetadataEntity(UUID paymentId, String provider, String jsonMetadata) {
        this.paymentId = paymentId;
        this.provider = provider;
        this.jsonMetadata = jsonMetadata;
    }
}
