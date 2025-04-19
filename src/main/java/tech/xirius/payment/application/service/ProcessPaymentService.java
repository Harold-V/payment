package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.in.ProcessPsePaymentUseCase;
import tech.xirius.payment.application.port.out.PaymentGatewayPort;
import tech.xirius.payment.domain.repository.PaymentRepositoryPort;
import tech.xirius.payment.domain.repository.PaymentMetadataRepositoryPort;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentEntity;
import tech.xirius.payment.infrastructure.persistence.entity.PaymentMetadataEntity;
import tech.xirius.payment.infrastructure.web.dto.PsePaymentRequest;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class ProcessPaymentService implements ProcessPsePaymentUseCase {

    private final PaymentGatewayPort paymentGatewayPort;
    private final PaymentRepositoryPort paymentRepository;
    private final PaymentMetadataRepositoryPort metadataRepository;

    public ProcessPaymentService(PaymentGatewayPort paymentGatewayPort,
            PaymentRepositoryPort paymentRepository,
            PaymentMetadataRepositoryPort metadataRepository) {
        this.paymentGatewayPort = paymentGatewayPort;
        this.paymentRepository = paymentRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public Map<String, Object> procesarPagoPse(PsePaymentRequest request) {
        Map<String, Object> response = paymentGatewayPort.processPsePayment(request);

        UUID paymentId = UUID.randomUUID();
        String status = extractStatus(response);
        String jsonMetadata = (String) response.get("rawJson");
        String provider = "PAYU";

        // Guardar entidad Payment
        PaymentEntity payment = new PaymentEntity(
                paymentId,
                request.getAmount(),
                status,
                "PSE",
                provider,
                ZonedDateTime.now());
        paymentRepository.save(payment);

        // Guardar metadatos
        PaymentMetadataEntity metadata = new PaymentMetadataEntity(
                paymentId,
                provider,
                jsonMetadata);
        metadataRepository.save(metadata);

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
