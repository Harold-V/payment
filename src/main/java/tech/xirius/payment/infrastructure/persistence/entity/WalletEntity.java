package tech.xirius.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidad que representa la billetera de un usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "wallet", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")
})
public class WalletEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true, length = 255)
    private String userId;

    @Column(nullable = false, precision = 38, scale = 2)
    private BigDecimal balance;
}
