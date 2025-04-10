package tech.xirius.payment.infrastructure.adapter.payment.wrapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentStatus;
import tech.xirius.payment.infrastructure.adapter.payment.wrapper.PaymentGatewayWrapper;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    @SuppressWarnings("unchecked")
    @Override
    public Payment processPayment(Payment payment) {
        Map<String, Object> payload = buildTransactionPayload(payment);

        String signature = generateSignature(payment);
        Map<String, Object> transaction = (Map<String, Object>) payload.get("transaction");
        Map<String, Object> order = (Map<String, Object>) transaction.get("order");
        order.put("signature", signature);

        Map<?, ?> response = restTemplate.postForObject(payuApiUrl, payload, Map.class);

        if (response != null && response.containsKey("transactionResponse")) {
            Map<String, Object> txResponse = (Map<String, Object>) response.get("transactionResponse");
            if (txResponse != null && txResponse.get("state") != null) {
                String state = (String) txResponse.get("state");

                switch (state.toUpperCase()) {
                    case "APPROVED" -> payment.complete();
                    case "DECLINED", "ERROR" -> payment.fail();
                    case "PENDING" -> {
                        /* Mantener PENDING */ }
                    default -> payment.cancel(); // fallback
                }
            } else {
                payment.fail(); // si txResponse es null
            }
        } else {
            payment.fail(); // si la respuesta es null o no contiene transactionResponse
        }

        return payment;
    }

    @Override
    public PaymentStatus getStatus(Payment payment) {

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

        Map<String, Object> buyer = Map.of("Id Transaction", payment.getId().toString());
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

    private String generateSignature(Payment payment) {
        String referenceCode = payment.getId().toString();
        String value = payment.getAmount().getAmount()
                .setScale(1, RoundingMode.HALF_UP)
                .toString();
        String currency = payment.getAmount().getCurrency().name();

        String rawSignature = String.format("%s~%s~%s~%s~%s",
                apiKey, merchantId, referenceCode, value, currency);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(rawSignature.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not generate signature", e);
        }
    }

}
