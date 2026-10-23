package selenide;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import utils.Config;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BaseSelenideTest {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = Config.BASE_URL;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = Config.TIMEOUT * 1000L;
        Configuration.pageLoadTimeout = 30_000;
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    protected void loginAsAdmin() {
        open(Config.LOGIN_URL);
        $("input[name=username]").setValue(Config.USERNAME);
        $("input[name=password]").setValue(Config.PASSWORD);
        $("button[type=submit]").click();
    }
}