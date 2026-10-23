package advanced;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.ApiHelper;
import utils.Config;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BaseAdvancedTest {

    // Сюда будет сохраняться имя товара, сгенерированное через API
    protected String testProduct;

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = Config.BASE_URL;
        Configuration.browser = "chrome";
        Configuration.timeout = 5000;
    }

    /** 3.5 Генерация тестовых данных через API */
    @BeforeEach
    void createTestData() {
        // Передаем фиксированные значения, а не несуществующие переменные
        testProduct = ApiHelper.createProduct("AutoProduct", "100");
    }

    /** 3.6 Удаление тестовых данных в @AfterEach */
    @AfterEach
    void cleanUp() {
        if (testProduct != null) {
            ApiHelper.deleteProduct(testProduct);
        }
        Selenide.closeWebDriver();
    }

    /** Вспомогательный метод для логина в админку */
    protected void loginAsAdmin() {
        open(Config.LOGIN_URL);
        $("input[name=username]").setValue(Config.USERNAME);
        $("input[name=password]").setValue(Config.PASSWORD);
        // На вашем сайте кнопка входа имеет type="submit"
        $("button[type=submit]").click();
    }
}