package tech.xirius.payment.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.xirius.payment.domain.repository.*;
import tech.xirius.payment.domain.service.*;

@Configuration
public class ServiceConfig {

    @Bean
    public WalletService walletService(WalletRepositoryPort walletRepo, WalletTransactionRepositoryPort txRepo) {
        return new WalletService(walletRepo, txRepo);
    }

    @Bean
    public PaymentService paymentService(PaymentRepositoryPort paymentRepo) {
        return new PaymentService(paymentRepo);
    }
}
