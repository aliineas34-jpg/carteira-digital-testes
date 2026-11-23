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
        return buscarConta(id).getSaldo();
    }

    public void depositar(Long id, Double valor) {
        validarValorPositivo(valor);
        Conta conta = buscarConta(id);
        conta.setSaldo(conta.getSaldo() + valor);
        contaRepository.save(conta);
    }

    public void transferir(Long idOrigem, Long idDestino, Double valor) {
        validarValorPositivo(valor);
        Conta origem = buscarConta(idOrigem);
        Conta destino = buscarConta(idDestino);

        if (origem.getSaldo() < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência");
        }

        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);
        contaRepository.save(origem);
        contaRepository.save(destino);
    }

    private Conta buscarConta(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }

    private void validarValorPositivo(Double valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }
    }
}
