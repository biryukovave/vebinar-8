package selenide;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import utils.Config;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class SelenideTests extends BaseSelenideTest {

    /** 2.1 Добавить товар через админку, выйти на витрину и проверить */
    @Test
    void addProductViaAdminAndCheckOnStorefront() {
        loginAsAdmin();
        open(Config.ADMIN_URL); // Используем переменную, чтобы не было warning
        String productName = "SelPhone_" + System.currentTimeMillis();

        $("#n-name").setValue(productName);
        $("#n-price").setValue("299");
        $("#add-btn").click();

        open("/");
        $(byText(productName)).shouldBe(visible);
    }

    /** 2.2 Добавить товар в корзину и проверить */
    @Test
    void addProductToCartAndCheck() {
        open("/");
        $$("button").findBy(text("В корзину")).click();
        $("#open-cart-btn").click();
        $("#cartModal").shouldBe(visible);
    }

    /** 2.3 Неверный логин */
    @Test
    void loginToAdminWithWrongCredentials() {
        open(Config.LOGIN_URL);
        $("input[name=username]").setValue("wrong");
        $("input[name=password]").setValue("wrong");
        $("button[type=submit]").click();
        $("body").shouldHave(text("Please sign in"));
    }

    /** 2.4 Сохранение корзины после обновления */
    @Test
    void cartPersistsAfterRefresh() {
        open("/");
        $$("button").findBy(text("В корзину")).click();
        $("#open-cart-btn").click();
        $("#cartModal").shouldBe(visible);

        Selenide.refresh();
        $("#open-cart-btn").click();
        $("#cartModal").shouldBe(visible);
    }

    /** 2.5 Добавить товаров > 300 руб. и проверить JS Alert */
    @Test
    void jsAlertOnCheckoutOver300() {
        open("/");
        for (int i = 0; i < 13; i++) { // 13 * 25 = 325 руб.
            $$("button").findBy(text("В корзину")).click();
        }
        $("#open-cart-btn").click();
        $(byText("Оформить заказ")).click();

        switchTo().alert().accept();
    }
}