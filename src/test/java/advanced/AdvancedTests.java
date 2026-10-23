package advanced;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AdvancedTests extends BaseAdvancedTest {

    /** 3.1 Три единицы товара в корзину и оплата */
    @Test
    void buyThreeItemsAndCheckNotification() {
        Configuration.timeout = 20000;

        // 1. Логинимся и попадаем в админку
        loginAsAdmin();

        // 2. Создаём товар через форму админки
        $("#n-name").clear();
        $("#n-name").setValue(testProduct);
        $("#n-price").clear();
        $("#n-price").setValue("100");
        $("#add-btn").click();
        $("#toast-container").shouldBe(visible, Duration.ofSeconds(5));

        // 3. Возвращаемся на витрину
        $$("a").findBy(text("Вернуться на сайт")).click();
        Selenide.sleep(2000);

        // 4. Кликаем "В корзину" на первой карточке 3 раза
        var addToCartBtn = $$("button").findBy(text("В корзину"));
        addToCartBtn.click();
        addToCartBtn.click();
        addToCartBtn.click();

        // 5. Открываем корзину
        $("#open-cart-btn").click();
        $("#cartModal > div > h2").shouldBe(visible);

        // 6. Проверяем, что количество в корзине = 3
        //    (кнопка корзины показывает счётчик: "Корзина (3)")
        $("#cartModal").shouldHave(text("3"));

        // 7. Оформляем заказ по реальному id кнопки
        $("#makeOrder").click();

        // 8. Проверяем уведомление об успехе
        $("#toast-container").shouldBe(visible, Duration.ofSeconds(10));
    }

    /** 3.2 Несколько товаров — корректная общая цена */
    @Test
    void multipleProductsTotalPrice() {
        Configuration.timeout = 20000;

        // 1. Логинимся
        loginAsAdmin();

        // 2. Создаём ПЕРВЫЙ товар (100 ₽)
        $("#n-name").clear();
        $("#n-name").setValue(testProduct + "_A");
        $("#n-price").clear();
        $("#n-price").setValue("100");
        $("#add-btn").click();
        $("#toast-container").shouldBe(visible, Duration.ofSeconds(5));

        // 3. Создаём ВТОРОЙ товар (150 ₽)
        $("#n-name").clear();
        $("#n-name").setValue(testProduct + "_B");
        $("#n-price").clear();
        $("#n-price").setValue("150");
        $("#add-btn").click();
        $("#toast-container").shouldBe(visible, Duration.ofSeconds(5));

        // 4. Возвращаемся на витрину
        $$("a").findBy(text("Вернуться на сайт")).click();
        Selenide.sleep(2000);

        // 5. Кликаем "В корзину" именно в карточке НАШЕГО товара
        $$("h4").findBy(text(testProduct + "_A"))
                .parent()
                .$$("button").findBy(text("В корзину"))
                .click();

        $$("h4").findBy(text(testProduct + "_B"))
                .parent()
                .$$("button").findBy(text("В корзину"))
                .click();

        // 6. Открываем корзину
        $("#open-cart-btn").click();
        $("#cartModal").shouldBe(visible);

        // 7. Проверяем, что в корзине лежат ОБА наших товара
        $("#cartModal").shouldHave(text(testProduct + "_A"));
        $("#cartModal").shouldHave(text(testProduct + "_B"));

        // 8. Проверяем общую сумму: 100 + 150 = 350
        $("#cartModal").shouldHave(text("250"));
    }
    /** 3.3 Добавить товар через админку — проверить уведомление */
    @Test
    void addProductViaAdminWithNotification() {
        Configuration.timeout = 20000;

        loginAsAdmin();

        $("#n-name").clear();
        $("#n-name").setValue(testProduct);
        $("#n-price").clear();
        $("#n-price").setValue("150");
        $("#add-btn").click();

        // Уведомление о создании товара
        $("#toast-container").shouldBe(visible, Duration.ofSeconds(5));
    }

    /** 3.4 Отредактировать товар — проверить изменения */
    @Test
    void editProductAndVerifyChanges() {
        Configuration.timeout = 20000;

        loginAsAdmin();

        // 1. Создаём товар с ценой 100
        $("#n-name").clear();
        $("#n-name").setValue(testProduct);
        $("#n-price").clear();
        $("#n-price").setValue("100");
        $("#add-btn").click();
        $("#toast-container").shouldBe(visible, Duration.ofSeconds(5));

        // 2. Обновляем страницу, чтобы товар появился в таблице
        Selenide.refresh();

        // 3.(находим строку по названию товара, затем input[type='number'] в ней)
        var priceInput = $x("//tr[.//input[@value='" + testProduct + "']]//input[@type='number']");
        priceInput.clear();
        priceInput.setValue("999");

        // 4. Нажимаем "Сохранить" в ЭТОЙ ЖЕ строке
        $x("//tr[.//input[@value='" + testProduct + "']]//button[contains(@class,'btn-upd')]")
                .click();
        $("#toast-container").shouldBe(visible, Duration.ofSeconds(5));

        // 5. Обновляем страницу и проверяем, что цена изменилась
        Selenide.refresh();
        $x("//tr[.//input[@value='" + testProduct + "']]//input[@type='number']")
                .shouldHave(value("999"));
    }
}