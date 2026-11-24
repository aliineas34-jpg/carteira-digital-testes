package com.example.carteira.controller;

import com.example.carteira.exception.SaldoInsuficienteException;
import com.example.carteira.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<Double> consultarSaldo(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.consultarSaldo(id));
    }

    @PostMapping("/{id}/depositar")
    public ResponseEntity<String> depositar(@PathVariable Long id, @RequestParam Double valor) {
        contaService.depositar(id, valor);
        return ResponseEntity.ok("Depósito realizado com sucesso!");
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(
            @RequestParam Long origemId,
            @RequestParam Long destinoId,
            @RequestParam Double valor) {
        contaService.transferir(origemId, destinoId, valor);
        return ResponseEntity.ok("Transferência realizada com sucesso!");
    }

    @ExceptionHandler({IllegalArgumentException.class, SaldoInsuficienteException.class})
    public ResponseEntity<String> tratarErrosNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
