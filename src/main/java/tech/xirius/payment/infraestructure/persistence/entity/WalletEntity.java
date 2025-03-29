package tech.xirius.payment.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Table(name = "wallets")
public class WalletEntity {

    @Id
    private UUID userId;

    @Column(nullable = false)
    private BigDecimal balance;

    public WalletEntity() {
    }

    public WalletEntity(UUID userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
