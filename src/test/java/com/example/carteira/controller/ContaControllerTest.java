package com.example.carteira.controller;

import com.example.carteira.exception.SaldoInsuficienteException;
import com.example.carteira.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ContaControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ContaService contaService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void deveRetornar200AoConsultarSaldo() throws Exception {
        when(contaService.consultarSaldo(1L)).thenReturn(500.0);

        mockMvc.perform(get("/api/contas/1/saldo"))
                .andExpect(status().isOk())
                .andExpect(content().string("500.0"));
    }

    @Test
    void deveRetornar200AoDepositarValorValido() throws Exception {
        doNothing().when(contaService).depositar(1L, 100.0);

        mockMvc.perform(post("/api/contas/1/depositar").param("valor", "100.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Depósito realizado com sucesso!"));
    }

    @Test
    void deveRetornar400AoDepositarValorInvalido() throws Exception {
        doThrow(new IllegalArgumentException("Valor inválido"))
                .when(contaService).depositar(1L, -10.0);

        mockMvc.perform(post("/api/contas/1/depositar").param("valor", "-10.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Valor inválido"));
    }

    @Test
    void deveRetornar200AoTransferirComSucesso() throws Exception {
        doNothing().when(contaService).transferir(1L, 2L, 50.0);

        mockMvc.perform(post("/api/contas/transferir")
                        .param("origemId", "1")
                        .param("destinoId", "2")
                        .param("valor", "50.0"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar400QuandoSaldoForInsuficienteNaTransferencia() throws Exception {
        doThrow(new SaldoInsuficienteException("Saldo insuficiente"))
                .when(contaService).transferir(1L, 2L, 900.0);

        mockMvc.perform(post("/api/contas/transferir")
                        .param("origemId", "1")
                        .param("destinoId", "2")
                        .param("valor", "900.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente"));
    }
}