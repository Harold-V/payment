package tech.xirius.payment.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.application.port.in.DeductFromWalletUseCase;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.domain.model.Money;
import tech.xirius.payment.domain.model.Wallet;
import tech.xirius.payment.domain.model.WalletTransaction;
import tech.xirius.payment.domain.service.WalletService;
import tech.xirius.payment.infrastructure.web.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final RechargeWalletUseCase rechargeWalletUseCase;
    private final DeductFromWalletUseCase deductFromWalletUseCase;

    public WalletController(
            WalletService walletService,
            RechargeWalletUseCase rechargeWalletUseCase,
            DeductFromWalletUseCase deductFromWalletUseCase) {
        this.walletService = walletService;
        this.rechargeWalletUseCase = rechargeWalletUseCase;
        this.deductFromWalletUseCase = deductFromWalletUseCase;
    }

    @PostMapping("/recharge")
    public ResponseEntity<BalanceResponse> rechargeWallet(@RequestBody RechargeRequest request) {
        Money amount = Money.of(request.amount(), request.currency());
        Wallet wallet = rechargeWalletUseCase.rechargeWallet(request.userId(), amount, request.paymentId());

        return ResponseEntity.ok(toBalanceResponse(wallet));
    }

    @PostMapping("/deduct")
    public ResponseEntity<BalanceResponse> deductFromWallet(@RequestBody DeductRequest request) {
        Money amount = Money.of(request.amount(), request.currency());
        Wallet wallet = deductFromWalletUseCase.deductFromWallet(request.userId(), amount);

        return ResponseEntity.ok(toBalanceResponse(wallet));
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BalanceResponse> getWalletBalance(@PathVariable String userId) {
        Wallet wallet = walletService.getWallet(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user: " + userId));

        return ResponseEntity.ok(toBalanceResponse(wallet));
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String userId) {
        List<WalletTransaction> transactions = walletService.getTransactionsByUserId(userId);

        List<TransactionResponse> response = transactions.stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private BalanceResponse toBalanceResponse(Wallet wallet) {
        return new BalanceResponse(
                wallet.getUserId(),
                wallet.getBalance().getAmount(),
                wallet.getBalance().getCurrency());
    }

    private TransactionResponse toTransactionResponse(WalletTransaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getUserId(),
                tx.getAmount().getAmount(),
                tx.getAmount().getCurrency(),
                tx.getType(),
                tx.getTimestamp(),
                tx.getPaymentId());
    }
}
