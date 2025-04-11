package tech.xirius.payment.infrastructure.web.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xirius.payment.application.port.in.DeductFromWalletUseCase;
import tech.xirius.payment.application.port.in.GetWalletBalanceUseCase;
import tech.xirius.payment.application.port.in.RechargeWalletUseCase;
import tech.xirius.payment.infrastructure.web.dto.DeductRequest;
import tech.xirius.payment.infrastructure.web.dto.RechargeRequest;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final RechargeWalletUseCase rechargeUseCase;
    private final DeductFromWalletUseCase deductUseCase;

    public WalletController(
            RechargeWalletUseCase rechargeUseCase,
            DeductFromWalletUseCase deductUseCase,
            GetWalletBalanceUseCase getBalanceUseCase) {
        this.rechargeUseCase = rechargeUseCase;
        this.deductUseCase = deductUseCase;
        this.getBalanceUseCase = getBalanceUseCase;
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

}
