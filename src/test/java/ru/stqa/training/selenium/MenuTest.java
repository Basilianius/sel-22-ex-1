package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Credentials.USERNAME;
import static settings.Constants.Credentials.PASSWORD;
import static settings.Constants.Titles.ADMIN_TITLE;
import static settings.Constants.Urls.ADMIN_URL;


public class MenuTest {

    private static Logger logger = LogManager.getLogger();

    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void before() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void menuTest001() {
        //логинимся на форму админки
        login();

        //получаем список пунктов меню
        ArrayList<String> firstNames = getMenuNames("//*[@id='app-']//a");
        for (String firstItemName : firstNames) {
            //перебираем пункты меню и кликаем каждый
            try {
                driver.findElement(By.xpath(String.format("//*[@id='app-']//a/span[text()='%s']", firstItemName))).click();
                checkExistH1(firstItemName);
            } catch (TimeoutException | NoSuchElementException ex) {
                logger.error(ex.getMessage());
            }

            //получаем список элементов подменю
            ArrayList<String> secondNames = getMenuNames("//li[starts-with(@id,'doc-')]");
            if (secondNames.size() > 0) {
                for (String secondItemName : secondNames) {
                    //перебираем пункты подменю и кликаем каждый
                    try {
                        driver.findElement(By.xpath(String.format("//li[starts-with(@id,'doc-')]//span[text()='%s']", secondItemName))).click();
                        checkExistH1(secondItemName);
                    } catch (NoSuchElementException ex) {
                        logger.error(ex.getMessage());
                    }
                }
            }
        }
    }

    private void checkExistH1(String itemName) {
        if (!isElementPresent(By.xpath("//*[@id=\"content\"]/h1"))) {
            throw new NoSuchElementException(String.format("Отсутсвует H1 на %s", itemName));
        } else {
            logger.info(String.format("Найден H1 на %s", itemName));
        }
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

    private ArrayList<String> getMenuNames(String locator) {
        ArrayList<String> list = new ArrayList<String>();

        ArrayList<WebElement> webElements = (ArrayList<WebElement>) driver.findElements(By.xpath(locator));
        if (webElements.size() != 0)
            for (WebElement item : webElements) {
                list.add(item.getText());
            }
        return list;
    }

    private void login() {
        driver.get(ADMIN_URL);
        wait.until(titleIs(ADMIN_TITLE));

        driver.findElement(By.name("username")).sendKeys(USERNAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();
        By locator = new By.ByXPath("//*[@title='Logout']");
        wait.until(visibilityOfElementLocated(locator));
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
