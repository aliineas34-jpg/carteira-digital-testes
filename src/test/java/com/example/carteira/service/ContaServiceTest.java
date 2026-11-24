package com.example.carteira.service;

import com.example.carteira.exception.SaldoInsuficienteException;
import com.example.carteira.model.Conta;
import com.example.carteira.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @Test
    void deveConsultarSaldoComSucesso() {
        Conta conta = new Conta(1L, "Maria", 500.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Double saldo = contaService.consultarSaldo(1L);

        assertEquals(500.0, saldo);
        verify(contaRepository).findById(1L);
    }

    @Test
    void deveRealizarDepositoComSucesso() {
        Conta conta = new Conta(1L, "Maria", 500.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        contaService.depositar(1L, 200.0);

        assertEquals(700.0, conta.getSaldo());
        verify(contaRepository).save(conta);
    }

    @Test
    void deveLancarExcecaoQuandoDepositoForZeroOuNegativo() {
        assertThrows(IllegalArgumentException.class, () -> contaService.depositar(1L, 0.0));
        assertThrows(IllegalArgumentException.class, () -> contaService.depositar(1L, -50.0));
        verify(contaRepository, never()).save(any());
    }

    @Test
    void deveRealizarTransferenciaComSucesso() {
        Conta origem = new Conta(1L, "Maria", 500.0);
        Conta destino = new Conta(2L, "João", 100.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(destino));

        contaService.transferir(1L, 2L, 150.0);

        assertEquals(350.0, origem.getSaldo());
        assertEquals(250.0, destino.getSaldo());
        verify(contaRepository).save(origem);
        verify(contaRepository).save(destino);
    }

    @Test
    void deveLancarSaldoInsuficienteExceptionQuandoSaldoForMenorQueTransferencia() {
        Conta origem = new Conta(1L, "Maria", 100.0);
        Conta destino = new Conta(2L, "João", 100.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(destino));

        assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.transferir(1L, 2L, 300.0);
        });

        verify(contaRepository, never()).save(any());
    }
}
