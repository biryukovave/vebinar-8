package selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Config;

import java.time.Duration;

public class BaseSeleniumTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.TIMEOUT));
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.TIMEOUT));
        driver.get(Config.BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    protected void loginAsAdmin() {
        driver.get(Config.LOGIN_URL);
        driver.findElement(By.name("username")).sendKeys(Config.USERNAME);
        driver.findElement(By.name("password")).sendKeys(Config.PASSWORD);
        driver.findElement(By.cssSelector("button[type=submit]")).click();
        wait.until(ExpectedConditions.urlContains("/admin"));
    }
}