package tech.xirius.payment.application.service;

import org.springframework.stereotype.Service;
import tech.xirius.payment.application.port.out.PayUGateway;
import tech.xirius.payment.domain.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ProcesarRecargaService {

    private final PayUGateway payUGateway;
    private final WalletService walletService;

    public ProcesarRecargaService(PayUGateway payUGateway, WalletService walletService) {
        this.payUGateway = payUGateway;
        this.walletService = walletService;
    }

    public void procesarRecarga(UUID userId, BigDecimal monto, String referencia) {
        boolean pagoExitoso = payUGateway.procesarPago(referencia, monto, userId);
        if (pagoExitoso) {
            walletService.recargarSaldo(userId, monto);
        } else {
            throw new IllegalStateException("El pago fue rechazado por PayU");
        }
    }
}
