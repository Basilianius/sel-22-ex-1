package ru.stqa.training.selenium;

import comparators.Comparator;
import entities.Account;
import entities.DuckProduct;
import helpers.StoryHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Generator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class RegistrationTest {

    private Logger LOGGER = LogManager.getLogger(RegistrationTest.class.getName());

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

        //открываем главную страницу
        StoryHelper.openMainPage(driver, wait);
    }

    @Test
    public void RegistrationTest001() throws NullPointerException {
        //переходим на страницу создания аккаунта
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-account-login']/div//td/a[contains(text(),'New customers click here')]"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        //генерация пользователя
        Account account = new Account(
                Generator.generateFirstName(),
                Generator.generateLastName(),
                Generator.generateEmail()
        );

        //вызываем сценарий создания аккаунта
        StoryHelper.createAccount(driver, wait, account);
        By locator = new By.ByXPath("//*[@id='box-account']/div//a[contains(text(),'Logout')]");
        assertTrue("Проверка создания аккаунта", isElementPresent(locator));
        //вылогиниваемся
        StoryHelper.logout(driver, wait);
        locator = new By.ByXPath("//div/form/table//button[@name='login']");
        assertTrue("Проверка вылогирования", isElementPresent(locator));
        //логинимся
        locator = new By.ByXPath("//*[@id='box-account']/div//a[contains(text(),'Logout')]");
        assertTrue("Проверка входа", isElementPresent(locator));

    }

    private boolean isElementPresent(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }


    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}

