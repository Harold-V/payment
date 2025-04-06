package tech.xirius.payment.domain.service;

import tech.xirius.payment.domain.model.Payment;
import tech.xirius.payment.domain.model.PaymentStatus;
import tech.xirius.payment.domain.repository.PaymentRepositoryPort;

import java.util.Optional;
import java.util.UUID;

public class PaymentService {
    private final PaymentRepositoryPort paymentRepository;

    public PaymentService(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentById(UUID id) {
        return paymentRepository.findById(id);
    }

    public Payment updatePaymentStatus(UUID id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + id));

        switch (status) {
            case COMPLETED:
                payment.complete();
                break;
            case FAILED:
                payment.fail();
                break;
            case CANCELLED:
                payment.cancel();
                break;
            default:
                throw new IllegalArgumentException("Invalid status transition: " + status);
        }

        return paymentRepository.save(payment);
    }
}