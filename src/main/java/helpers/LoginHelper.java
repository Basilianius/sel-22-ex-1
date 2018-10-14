package helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Credentials.PASSWORD;
import static settings.Constants.Credentials.USERNAME;
import static settings.Constants.Titles.ADMIN_TITLE;
import static settings.Constants.Titles.USER_TITLE;
import static settings.Constants.Urls.ADMIN_URL;
import static settings.Constants.Urls.USER_URL;

/**
 * Сценарии логина в магазин
 */
public class LoginHelper {
    public static Logger LOGGER = LogManager.getLogger(LoginHelper.class.getName());

    public static void loginAdmin(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вход на страницу администратора");
        driver.get(ADMIN_URL);
        wait.until(titleIs(ADMIN_TITLE));

        driver.findElement(By.name("username")).sendKeys(USERNAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();

        By locator = new By.ByXPath("//*[@title='Logout']");
        wait.until(visibilityOfElementLocated(locator));

        LOGGER.info("Вход на страницу администратора - УСПЕШНО");
    }

    public static void loginUser(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вход на главную страницу");
        driver.get(USER_URL);

        By locator = new By.ByXPath("//div/form/table//button[@name='login']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));
        LOGGER.info("Вход на главную страницу - УСПЕШНО");
    }
}
