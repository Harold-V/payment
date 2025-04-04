package tech.xirius.payment.infraestructure.web.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.xirius.payment.infraestructure.web.dto.BalanceResponse;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @GetMapping("/balance/{userId}")
    public BalanceResponse getBalance(@PathVariable UUID userId) {
        return new BalanceResponse(BigDecimal.valueOf(150.75)); // Simulado
    }
}