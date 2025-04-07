package tech.xirius.payment.infrastructure.adapter.payment.wrapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentStatus;
import tech.xirius.payment.infrastructure.adapter.payment.wrapper.PaymentGatewayWrapper;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class PayuWrapper implements PaymentGatewayWrapper {

    @Value("${payu.api.key}")
    private String apiKey;

    @Value("${payu.api.login}")
    private String apiLogin;

    @Value("${payu.merchant.id}")
    private String merchantId;

    @Value("${payu.account.id}")
    private String accountId;

    @Value("${payu.api.url}")
    private String payuApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Payment processPayment(Payment payment) {
        Map<String, Object> payload = buildTransactionPayload(payment);
        Map<?, ?> response = restTemplate.postForObject(payuApiUrl, payload, Map.class);
        // TODO: Parse response and update status based on result
        return payment; // Devolver actualizado si lo deseas
    }

    @Override
    public PaymentStatus getStatus(Payment payment) {
        // Optional: puedes implementar una verificación real aquí
        return PaymentStatus.COMPLETED;
    }

    private Map<String, Object> buildTransactionPayload(Payment payment) {
        Map<String, Object> tx = new HashMap<>();

        tx.put("language", "en");
        tx.put("command", "SUBMIT_TRANSACTION");

        Map<String, Object> merchant = Map.of(
                "apiKey", apiKey,
                "apiLogin", apiLogin);
        tx.put("merchant", merchant);

        Map<String, Object> transaction = new HashMap<>();

        Map<String, Object> order = new HashMap<>();
        order.put("accountId", accountId);
        order.put("referenceCode", payment.getId().toString());
        order.put("description", "Pago para " + payment.getUserId());

        Map<String, Object> buyer = Map.of("fullName", payment.getUserId());
        order.put("buyer", buyer);

        Map<String, Object> amount = Map.of(
                "currency", payment.getAmount().getCurrency().toString(),
                "value", payment.getAmount().getAmount());

        Map<String, Object> txValues = new HashMap<>();
        txValues.put("TX_VALUE", amount);
        order.put("additionalValues", txValues);

        transaction.put("order", order);
        transaction.put("type", "AUTHORIZATION_AND_CAPTURE");
        transaction.put("paymentMethod", payment.getPaymentMethod().name());
        transaction.put("paymentCountry", "CO");

        tx.put("transaction", transaction);
        return tx;
    }
}
