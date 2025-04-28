package tech.xirius.payment.infrastructure.web.api.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.application.port.in.DeductFromWalletUseCase;
import tech.xirius.payment.application.port.in.GetWalletBalanceUseCase;
import tech.xirius.payment.application.port.in.GetWalletTransactionsUseCase;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.infrastructure.persistence.mapper.TransactionMapper;
import tech.xirius.payment.infrastructure.web.dto.DeductRequest;
import tech.xirius.payment.infrastructure.web.dto.RechargeRequest;
import tech.xirius.payment.infrastructure.web.dto.TransactionResponse;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletResource {

    private final RechargeWalletUseCase rechargeUseCase;
    private final DeductFromWalletUseCase deductUseCase;
    private final GetWalletBalanceUseCase getBalanceUseCase;
    private final GetWalletTransactionsUseCase getTransactionsUseCase;
    private final TransactionMapper transactionMapper;

    @PostMapping("/recharge")
    public ResponseEntity<Void> recharge(@Valid @RequestBody RechargeRequest request) {
        log.info("Recargando wallet para usuario: {}", request.getUserId());
        rechargeUseCase.recharge(request.getUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deduct")
    public ResponseEntity<Void> deduct(@Valid @RequestBody DeductRequest request) {
        log.info("Descontando saldo wallet para usuario: {}", request.getUserId());
        deductUseCase.deduct(request.getUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String userId) {
        log.info("Consultando balance de wallet para usuario: {}", userId);
        return ResponseEntity.ok(getBalanceUseCase.getBalanceByUserId(userId));
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String userId) {
        log.info("Consultando transacciones de wallet para usuario: {}", userId);

        List<TransactionResponse> response = getTransactionsUseCase.getTransactionsByUserId(userId)
                .stream()
                .map(tx -> {
                    TransactionResponse tr = transactionMapper.toResponse(tx);
                    return new TransactionResponse(
                            tr.id(),
                            userId,
                            tr.amount(),
                            tr.currency(),
                            tr.type(),
                            tr.timestamp(),
                            tr.paymentId());
                })
                .toList();

        return ResponseEntity.ok(response);
    }
}
