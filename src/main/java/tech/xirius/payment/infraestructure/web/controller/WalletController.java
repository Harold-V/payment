package tech.xirius.payment.infraestructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.domain.service.WalletService;
import tech.xirius.payment.infraestructure.web.dto.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/recargar")
    public ResponseEntity<Void> recargar(@RequestBody RecargaRequest request) {
        walletService.recargarSaldo(request.userId(), request.monto());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/descontar")
    public ResponseEntity<Void> descontar(@RequestBody DescuentoRequest request) {
        walletService.descontarSaldo(request.userId(), request.monto());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/saldo/{userId}")
    public ResponseEntity<SaldoResponse> consultarSaldo(@PathVariable UUID userId) {
        var saldo = walletService.consultarSaldo(userId);
        return ResponseEntity.ok(new SaldoResponse(saldo));
    }

    @GetMapping("/transacciones/{userId}")
    public ResponseEntity<List<TransaccionResponse>> consultarTransacciones(@PathVariable UUID userId) {
        var transacciones = walletService.consultarTransacciones(userId);
        var response = transacciones.stream().map(tx -> new TransaccionResponse(
                tx.getTransactionId(),
                tx.getAmount(),
                tx.getType().name(),
                tx.getTimestamp())).toList();
        return ResponseEntity.ok(response);
    }

}
