package tech.xirius.payment.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.PaymentMethod;
import tech.xirius.payment.domain.model.PaymentProvider;

public record PaymentRequest(
                UUID walletId,
                BigDecimal amount,
                Currency currency,
                PaymentMethod paymentMethod,
                PaymentProvider provider) {
}
