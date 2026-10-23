package selenium;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Config;

public class SeleniumTests extends BaseSeleniumTest {

    /** 1.1 Добавить товар через админку, выйти на витрину и проверить */
    @Test
    void addProductViaAdminAndCheckOnStorefront() {
        loginAsAdmin();
        String productName = "TestPhone_" + System.currentTimeMillis();

        driver.findElement(By.id("n-name")).sendKeys(productName);
        driver.findElement(By.id("n-price")).sendKeys("199");
        driver.findElement(By.id("add-btn")).click();

        driver.get(Config.BASE_URL);
        WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), '" + productName + "')]")));
        Assertions.assertTrue(product.isDisplayed());
    }

    /** 1.2 Добавить товар в корзину и проверить */
    @Test
    void addProductToCartAndCheck() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(text(),'В корзину')])[1]"))).click();
        driver.findElement(By.id("open-cart-btn")).click();
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cartModal")));
        Assertions.assertTrue(modal.isDisplayed());
    }

    /** 1.3 Попытаться войти в админку с неверным логином и паролем */
    @Test
    void loginToAdminWithWrongCredentials() {
        System.out.println("1. Открываем страницу логина...");
        driver.get(Config.LOGIN_URL);

        System.out.println("2. Страница открыта. Вводим логин...");
        driver.findElement(By.name("username")).sendKeys("wrong");

        System.out.println("3. Вводим пароль...");
        driver.findElement(By.name("password")).sendKeys("wrong");

        System.out.println("4. Нажимаем кнопку 'Sign in'...");
        driver.findElement(By.cssSelector("button[type=submit]")).click();

        System.out.println("5. Ждем 2 секунды...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        System.out.println("6. Проверяем URL...");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"), "Мы оказались не на странице логина");

        System.out.println("7. Тест завершен успешно!");
    }

    /** 1.4 Проверить сохранение товаров в корзине после обновления страницы */
    @Test
    void cartPersistsAfterRefresh() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(text(),'В корзину')])[1]"))).click();
        driver.findElement(By.id("open-cart-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cartModal")));

        driver.navigate().refresh();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("open-cart-btn"))).click();
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cartModal")));
        Assertions.assertTrue(modal.isDisplayed());
    }
}