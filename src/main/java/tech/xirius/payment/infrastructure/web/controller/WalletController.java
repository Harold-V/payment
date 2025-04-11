package tech.xirius.payment.infrastructure.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import tech.xirius.payment.application.port.in.GetWalletTransactionsUseCase;
import tech.xirius.payment.domain.model.Currency;
import tech.xirius.payment.domain.model.WalletTransaction;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.application.port.in.DeductFromWalletUseCase;
import tech.xirius.payment.application.port.in.GetWalletBalanceUseCase;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.infrastructure.web.dto.DeductRequest;
import tech.xirius.payment.infrastructure.web.dto.RechargeRequest;
import tech.xirius.payment.infrastructure.web.dto.TransactionResponse;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final RechargeWalletUseCase rechargeUseCase;
    private final DeductFromWalletUseCase deductUseCase;
    private final GetWalletTransactionsUseCase getTransactionsUseCase;

    public WalletController(
            RechargeWalletUseCase rechargeUseCase,
            DeductFromWalletUseCase deductUseCase,
            GetWalletBalanceUseCase getBalanceUseCase,
            GetWalletTransactionsUseCase getTransactionsUseCase) {
        this.rechargeUseCase = rechargeUseCase;
        this.deductUseCase = deductUseCase;
        this.getBalanceUseCase = getBalanceUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
    }

    @PostMapping("/recharge")
    public ResponseEntity<Void> recharge(@RequestBody RechargeRequest request) {
        rechargeUseCase.recharge(request.getUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deduct")
    public ResponseEntity<Void> deduct(@RequestBody DeductRequest request) {
        deductUseCase.deduct(request.getUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    private final GetWalletBalanceUseCase getBalanceUseCase;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String userId) {
        BigDecimal balance = getBalanceUseCase.getBalanceByUserId(userId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String userId) {
        List<WalletTransaction> transactions = getTransactionsUseCase.getTransactionsByUserId(userId);

        List<TransactionResponse> response = transactions.stream()
                .map(tx -> new TransactionResponse(
                        tx.getTransactionId(),
                        userId,
                        tx.getAmount(),
                        Currency.COP,
                        tx.getType(),
                        tx.getTimestamp(),
                        tx.getPaymentId()))
                .toList();

        return ResponseEntity.ok(response);
    }

}
