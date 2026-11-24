package com.example.carteira.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CarteiraSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void deveRealizarDepositoPelaInterfaceWeb() {
        driver.get("http://localhost:8080/index.html");

        driver.findElement(By.id("contaId")).clear();
        driver.findElement(By.id("contaId")).sendKeys("1");
        driver.findElement(By.id("valor")).sendKeys("150");
        driver.findElement(By.id("btnDepositar")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("resultado"), "Depósito realizado com sucesso!"));

        String mensagem = driver.findElement(By.id("resultado")).getText();
        assertEquals("Depósito realizado com sucesso!", mensagem);
    }
}
