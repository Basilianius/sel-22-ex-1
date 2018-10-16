package helpers;

import entities.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Generator;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Credentials.PASSWORD;
import static settings.Constants.Credentials.USERNAME;
import static settings.Constants.Titles.ADMIN_TITLE;
import static settings.Constants.Titles.CREATE_ACCOUNT_TITLE;
import static settings.Constants.Titles.USER_TITLE;
import static settings.Constants.Urls.ADMIN_URL;
import static settings.Constants.Urls.USER_URL;

/**
 * Сценарии логина в магазин
 */
public class StoryHelper {
    public static Logger LOGGER = LogManager.getLogger(StoryHelper.class.getName());

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

    public static void openMainPage(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вход на главную страницу");
        driver.get(USER_URL);

        By locator = new By.ByXPath("//div/form/table//button[@name='login']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));
        LOGGER.info("Вход на главную страницу - УСПЕШНО");
    }

    public static void createAccount(WebDriver driver, WebDriverWait wait, Account account) {
        LOGGER.info("Регистрация нового пользователя");

        By locator = new By.ByXPath("//*[@id='create-account']/div//td/button[@name='create_account']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(CREATE_ACCOUNT_TITLE));

        //Заполнение обязательных полей
        LOGGER.info("Заполнение ФИО");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='firstname']", account.getFirstName());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='lastname']", account.getLastName());

        LOGGER.info("Заполнение адреса города емайла телефона");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='address1']", account.getAddress());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='city']", account.getCity());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='email']", account.getEmail());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='phone']", account.getPhone());

        LOGGER.info("Заполнение страны и индекса");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='postcode']", account.getPostcode());
        selectText(driver, wait, "//*[@id='create-account']/div//td/select[@name='country_code']", account.getCountry());

        LOGGER.info("Заполнение пароля");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='password']", account.getPassword());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='confirmed_password']", account.getConfirmedPassword());

        //вызываем создание аккаунта
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='create-account']/div//td/button[@name='create_account']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        //проверяем открытие главной страницы с залогированным пользователем
        openLoginMainPage(driver, wait);

        LOGGER.info("Регистрация нового пользователя - УСПЕШНО");
    }

    public static void loginAccount(WebDriver driver, WebDriverWait wait, Account account) {
        LOGGER.info("Вход зарегистрированным пользователем");

        //заполнение емайла и пароля
        LOGGER.info("Заполнение кредов");
        inputText(driver, wait, "//*[@id='box-account-login']/div//td/input[@name='email']", account.getEmail());
        inputText(driver, wait, "//*[@id='box-account-login']/div//td/input[@name='password']", account.getPassword());

        //логинимся
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-account-login']/div//td//button[@name='login']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        //проверяем открытие главной страницы с залогированным пользователем
        openLoginMainPage(driver, wait);

        LOGGER.info("Вход зарегистрированным пользователем - УСПЕШНО");
    }

    public static void logout(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вылогирование из аккаунта");
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-account']/div//a[contains(text(),'Logout')]"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        By locator = new By.ByXPath("//div/form/table//button[@name='login']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));

        LOGGER.info("Вылогирование из аккаунта - УСПЕШНО");
    }

    public static void openLoginMainPage(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вход на главную страницу залогининым пользователем");

        By locator = new By.ByXPath("//*[@id='box-account']/div//a[contains(text(),'Logout')]");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));
        LOGGER.info("Вход на главную страницу залогининым пользователем - УСПЕШНО");
    }

    private static void inputText(WebDriver driver, WebDriverWait wait, String locator, String text) {
        try {
            WebElement webElement = driver.findElement(By.xpath(locator));
            webElement.sendKeys(text);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private static void selectText(WebDriver driver, WebDriverWait wait, String locator, String text) {
        try {
            WebElement webElement = driver.findElement(By.xpath(locator));
            Select select = new Select(webElement);
            select.selectByVisibleText(text);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private boolean isElementPresent(WebDriver driver, WebDriverWait wait, By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }


}
