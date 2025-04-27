package tech.xirius.payment.infrastructure.adapter.payment.wrapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
@Component
public class PayuWrapper implements PaymentGatewayPort {

    private final RestTemplate restTemplate;

    @Value("${payu.api.url}")
    private String payuUrl;

    @Value("${payu.api.login}")
    private String apiLogin;

    @Value("${payu.api.key}")
    private String apiKey;

    @Value("${payu.account.id}")
    private String accountId;

    @Value("${payu.merchant.id}")
    private String merchantId;

    @Value("${payu.response.url}")
    private String responseUrl;

    public PayuWrapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getAvailableBanks() {
        Map<String, Object> request = new HashMap<>();
        request.put("language", "es");
        request.put("command", "GET_BANKS_LIST");
        request.put("test", false);
        request.put("merchant", Map.of("apiLogin", apiLogin, "apiKey", apiKey));
        request.put("bankListInformation", Map.of("paymentMethod", "PSE", "paymentCountry", "CO"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                payuUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                });

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("banks")) {
                Object banksObj = body.get("banks");
                if (banksObj instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Map) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> banks = (List<Map<String, Object>>) banksObj;
                    return banks;
                }
            }
        }
        return Collections.emptyList();
    }

    private String generateSignature(String referenceCode, BigDecimal amount, String currency) {
        try {
            String raw = apiKey + "~" + merchantId + "~" + referenceCode + "~" + amount + "~" + currency;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PayU signature", e);
        }
    }

    @Override
    public Map<String, Object> processPsePayment(PsePaymentRequest req) {
        String referenceCode = "ORD-" + System.currentTimeMillis();
        BigDecimal value = req.getAmount();
        String currency = "COP";
        String signature = generateSignature(referenceCode, value, currency);

        Map<String, Object> request = new HashMap<>();
        request.put("language", "es");
        request.put("command", "SUBMIT_TRANSACTION");
        request.put("test", false);

        request.put("merchant", Map.of("apiLogin", apiLogin, "apiKey", apiKey));

        Map<String, Object> address = Map.of(
                "street1", req.getStreet1(),
                "city", req.getCity(),
                "state", req.getState(),
                "country", req.getCountry(),
                "postalCode", req.getPostalCode(),
                "phone", req.getContactPhone());

        Map<String, Object> order = new HashMap<>();
        order.put("accountId", accountId);
        order.put("referenceCode", referenceCode);
        order.put("description", req.getDescription());
        order.put("language", "es");
        order.put("signature", signature);
        order.put("additionalValues", Map.of(
                "TX_VALUE", Map.of("value", value, "currency", currency),
                "TX_TAX", Map.of("value", BigDecimal.ZERO, "currency", currency),
                "TX_TAX_RETURN_BASE", Map.of("value", BigDecimal.ZERO, "currency", currency)));
        order.put("shippingAddress", address);
        order.put("buyer", Map.of(
                "fullName", req.getFullName(),
                "emailAddress", req.getEmail(),
                "contactPhone", req.getContactPhone(),
                "dniNumber", req.getDniNumber(),
                "shippingAddress", address));

        Map<String, Object> payer = Map.of(
                "fullName", req.getFullName(),
                "emailAddress", req.getEmail(),
                "contactPhone", req.getContactPhone(),
                "dniNumber", req.getDniNumber(),
                "dniType", req.getDniType(),
                "billingAddress", address);

        Map<String, Object> extraParameters = Map.of(
                "RESPONSE_URL", responseUrl,
                "PSE_REFERENCE1", req.getEmail(),
                "FINANCIAL_INSTITUTION_CODE", req.getBankCode(),
                "USER_TYPE", req.getUserType(),
                "PSE_REFERENCE2", req.getDniType(),
                "PSE_REFERENCE3", req.getDniNumber());

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("order", order);
        transaction.put("payer", payer);
        transaction.put("extraParameters", extraParameters);
        transaction.put("type", "AUTHORIZATION_AND_CAPTURE");
        transaction.put("paymentMethod", "PSE");
        transaction.put("paymentCountry", "CO");
        transaction.put("deviceSessionId", req.getDeviceSessionId());
        transaction.put("ipAddress", req.getIpAddress());
        transaction.put("cookie", req.getCookie());
        transaction.put("userAgent", req.getUserAgent());

        request.put("transaction", transaction);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                payuUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                });

        Map<String, Object> body = response.getBody() != null ? new HashMap<>(response.getBody()) : new HashMap<>();
        body.put("referenceCode", referenceCode);
        body.put("userId", req.getUserId());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMetadata = mapper.writeValueAsString(body);
            body.put("rawJson", jsonMetadata);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar respuesta PayU como JSON", e);
        }

        return body;
    }
}
