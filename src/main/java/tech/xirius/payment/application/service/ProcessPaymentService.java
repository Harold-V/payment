package tech.xirius.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.xirius.payment.application.port.in.ProcessPsePaymentUseCase;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.domain.repository.PaymentMetadataRepositoryPort;
import tech.xirius.payment.domain.repository.PaymentRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentMetadataEntity;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio de aplicación para procesar pagos de pse a través de PayU.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProcessPaymentService implements ProcessPsePaymentUseCase {

    private final PaymentGatewayPort paymentGatewayPort;
    private final PaymentRepositoryPort paymentRepository;
    private final PaymentMetadataRepositoryPort metadataRepository;
    private final RechargeWalletUseCase rechargeWalletUseCase;

    @Override
    public Map<String, Object> processPsePayment(PsePaymentRequest request) {
        log.info("Iniciando procesamiento de pago PSE para usuario: {}", request.getUserId());

        Map<String, Object> response = paymentGatewayPort.processPsePayment(request);

        UUID paymentId = UUID.randomUUID();
        String status = extractStatus(response);
        String jsonMetadata = (String) response.get("rawJson");
        String referenceCode = (String) response.get("referenceCode");
        String provider = "PAYU";

        PaymentEntity payment = new PaymentEntity(
                paymentId,
                referenceCode,
                request.getAmount(),
                status,
                "PSE",
                provider,
                ZonedDateTime.now());
        paymentRepository.save(payment);

        PaymentMetadataEntity metadata = new PaymentMetadataEntity(
                paymentId,
                provider,
                jsonMetadata);
        metadataRepository.save(metadata);

        if ("APPROVED".equalsIgnoreCase(status)) {
            rechargeWalletUseCase.recharge(request.getUserId(), request.getAmount(), paymentId);
            log.info("Recarga exitosa para usuario: {}", request.getUserId());
        } else {
            log.warn("El pago no fue aprobado. Estado: {}", status);
        }

        return response;
    }

    private String extractStatus(Map<String, Object> response) {
        Object trx = response.get("transactionResponse");
        if (trx instanceof Map<?, ?> map) {
            Object state = map.get("state");
            if (state != null) {
                return state.toString();
            }
        }
        return "PENDING";
    }
}
