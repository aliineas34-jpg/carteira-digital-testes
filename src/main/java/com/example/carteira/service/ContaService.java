package com.example.carteira.service;

import com.example.carteira.exception.SaldoInsuficienteException;
import com.example.carteira.model.Conta;
import com.example.carteira.repository.ContaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Double consultarSaldo(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        return conta.getSaldo();
    }

    public void depositar(Long id, Double valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero");
        }
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        conta.setSaldo(conta.getSaldo() + valor);
        contaRepository.save(conta);
    }

    public void transferir(Long idOrigem, Long idDestino, Double valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero");
        }
        Conta origem = contaRepository.findById(idOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada"));
        Conta destino = contaRepository.findById(idDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada"));

        if (origem.getSaldo() < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência");
        }

        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);
        contaRepository.save(origem);
        contaRepository.save(destino);
    }
}
