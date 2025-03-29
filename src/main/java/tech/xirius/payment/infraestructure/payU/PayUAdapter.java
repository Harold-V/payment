package tech.xirius.payment.infraestructure.payU;

import com.payu.sdk.PayU;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.model.PayUParameters;
import com.payu.sdk.model.PaymentResponse;
import com.payu.sdk.exceptions.PayUException;

import org.springframework.stereotype.Component;
import tech.xirius.payment.application.port.out.PayUGateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class PayUAdapter implements PayUGateway {

    public PayUAdapter() {
        PayU.apiKey = "tu_api_key";
        PayU.apiLogin = "tu_api_login";
        PayU.merchantId = "tu_merchant_id";
        PayU.isTest = true; // false en producción
    }

    @Override
    public boolean procesarPago(String referencia, BigDecimal monto, UUID userId) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(PayUParameters.ACCOUNT_ID, "tu_account_id");
            parameters.put(PayUParameters.REFERENCE_CODE, referencia);
            parameters.put(PayUParameters.DESCRIPTION, "Recarga de billetera para usuario " + userId);
            parameters.put(PayUParameters.VALUE, monto.setScale(2).toPlainString());
            parameters.put(PayUParameters.CURRENCY, "COP");

            parameters.put(PayUParameters.PAYER_NAME, "Usuario Prueba");
            parameters.put(PayUParameters.PAYMENT_METHOD, "VISA");
            parameters.put(PayUParameters.CREDIT_CARD_NUMBER, "4097440000000004");
            parameters.put(PayUParameters.CREDIT_CARD_EXPIRATION_DATE, "2025/12");
            parameters.put(PayUParameters.CREDIT_CARD_SECURITY_CODE, "321");

            parameters.put(PayUParameters.PAYER_EMAIL, "prueba@payu.com");
            parameters.put(PayUParameters.IP_ADDRESS, "127.0.0.1");

            PaymentResponse response = PayUPayments.doAuthorizationAndCapture(parameters);

            return response != null && "APPROVED".equals(response.getState());

        } catch (PayUException | Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
