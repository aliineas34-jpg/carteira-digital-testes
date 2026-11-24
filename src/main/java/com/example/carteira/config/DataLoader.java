package com.example.carteira.config;

import com.example.carteira.model.Conta;
import com.example.carteira.repository.ContaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner carregarContasIniciais(ContaRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Conta(null, "Maria Silva", 1000.0));
                repository.save(new Conta(null, "João Santos", 500.0));
            }
        };
    }
}
