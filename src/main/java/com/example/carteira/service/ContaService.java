package com.example.carteira.service;

import com.example.carteira.repository.ContaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Double consultarSaldo(Long id) {
        return null;
    }

    public void depositar(Long id, Double valor) {
    }

    public void transferir(Long idOrigem, Long idDestino, Double valor) {
    }
}
