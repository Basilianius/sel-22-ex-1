package ru.stqa.training.selenium;

import helpers.StoryHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Credentials.PASSWORD;
import static settings.Constants.Credentials.USERNAME;
import static settings.Constants.Titles.*;
import static settings.Constants.Urls.ADMIN_URL;
import static settings.Constants.Urls.COUNTRIES_PAGE;


public class OpenNewPageTest {

    private Logger LOGGER = LogManager.getLogger(OpenNewPageTest.class.getName());
    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void before() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

        //логинимся на форму админки
        StoryHelper.loginAdmin(driver, wait);
    }

    @Test
    public void OpenNewPageTest001() throws Exception {
        LOGGER.info("Проверка открытий в новых окнах");

        //переходим на страницу со странами
        driver.get(COUNTRIES_PAGE);
        wait.until(titleIs(COUNTRIES_TITLE));

        ArrayList<WebElement> linkList = null;

        try {
            //открываем создание новой страницы
            WebElement webElement = driver.findElement(By.xpath("//*[@id='content']/div/a[contains(text(),'Add New Country')]"));
            webElement.click();
            wait.until(titleIs(ADD_NEW_COUNTRY_TITLE));

            //получаем список внешних ссылок
            linkList = (ArrayList<WebElement>) driver.findElements(By.xpath("//*[@id='content']/form//td/a/i[@class='fa fa-external-link']/.."));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        assertNotNull("Список внешних ссылок НЕ пустой", linkList);

        for (int i = 0; i < linkList.size(); i++) {
            //получаем дескриптор текущего окна
            String oldWindowHandler = driver.getWindowHandle();
            // получаем набор дескрипторов текущих открытых окон
            Set<String> oldWindowsSet = driver.getWindowHandles();
            Set<String> newWindowsSet = null;

            //открываем новое окно
            linkList.get(i).click();

            for (int count = 0; ; count++) {
                if (count >= 5)
                    throw new TimeoutException();
                try {
                    newWindowsSet = driver.getWindowHandles();
                    newWindowsSet.removeAll(oldWindowsSet);
                    if (newWindowsSet.size() != 1) {
                        throw new NoSuchElementException("Ссылка НЕ открылась в новом окне");
                    }
                    break;
                } catch (NoSuchElementException e) {
                }
                Thread.sleep(1000);
            }
            // переключаемся в новое окно
            LOGGER.info("Переключаемся в новое окно");
            String newWindowHandler = newWindowsSet.iterator().next();
            driver.switchTo().window(newWindowHandler);
            // закрываем новое окно
            LOGGER.info("Закрываем новое окно");
            driver.close();
            driver.switchTo().window(oldWindowHandler);

            LOGGER.info("Проверка открытий в новых окнах - УСПЕШНО");
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

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
