# API Carteira Digital (e-Wallet)

Projeto Java com Spring Boot para **Testes Automatizados I** (Controller, Service e Repository) com TDD e JaCoCo.

## Endpoints
- GET /api/contas/{id}/saldo
- POST /api/contas/{id}/depositar?valor=100.0
- POST /api/contas/transferir?origemId=1&destinoId=2&valor=50.0

## Testes
- Unitarios: ContaServiceTest (JUnit 5 + Mockito)
- Integracao: ContaControllerTest (MockMvc)
- Interface: CarteiraSeleniumTest (Selenium WebDriver)

## Executar
./mvnw test