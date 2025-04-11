package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.PaymentMethod;
import tech.xirius.payment.domain.model.PaymentProvider;

public record PaymentRequest(
        @NotNull UUID walletId,
        @NotNull @Positive BigDecimal amount,
        @NotNull Currency currency,
        @NotNull PaymentMethod paymentMethod,
        @NotNull PaymentProvider provider) {
}
