package tech.xirius.payment.domain.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Payment {
    private final UUID id;
    private final Money amount;
    private PaymentStatus status;
    private final PaymentMethod paymentMethod;
    private final PaymentProvider provider;
    private final ZonedDateTime timestamp;

    public Payment(UUID id, Money amount, PaymentStatus status,
            PaymentMethod paymentMethod, PaymentProvider provider, ZonedDateTime timestamp) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.provider = provider;
        this.timestamp = timestamp;
    }

    public static Payment createPayment(Money amount, PaymentMethod paymentMethod, PaymentProvider provider) {
        return new Payment(
                UUID.randomUUID(),
                amount,
                PaymentStatus.PENDING,
                paymentMethod,
                provider,
                ZonedDateTime.now());
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Money getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentProvider getProvider() {
        return provider;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
    }

    public boolean isPending() {
        return this.status == PaymentStatus.PENDING;
    }

    public boolean isCompleted() {
        return this.status == PaymentStatus.COMPLETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}