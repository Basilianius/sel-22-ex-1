package ru.stqa.training.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Titles.USER_TITLE;
import static settings.Constants.Urls.USER_URL;


public class StickerTest {

    private Logger LOGGER = LogManager.getLogger(StickerTest.class.getName());

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void stickerTest001() {
        //заходим на сайт
        loginUser();

        //получаем список товаров
        List<WebElement> duckList = new ArrayList<WebElement>();
        try {
            duckList = driver.findElements(By.xpath(String.format("//div//ul//a[@class='link' and contains(@title,'%s')]", "Duck")));
        } catch (TimeoutException | NoSuchElementException ex) {
            LOGGER.error(ex.getMessage());
        }

        if (duckList.size() > 0) {
            for (WebElement duckItem : duckList) {
                //перебираем товары и проверяем наличие только одного стикера
                try {
                    List<WebElement> stickerList = duckItem.findElements(By.xpath(String.format(".//div[contains(@class,'%s')]", "sticker")));
                    checkCountStickerOnlyOne(duckItem.getAttribute("title"), stickerList);
                } catch (TimeoutException | NoSuchElementException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        }
    }

    private void checkCountStickerOnlyOne(String duckName, List<WebElement> itemList) {
        LOGGER.info(String.format("Проверка стикеров товара %s", duckName));
        if (itemList.size() > 1 || itemList.size() < 1) {
            throw new NoSuchElementException(String.format("Более или менее одного стикера на %s", duckName));
        } else {
            LOGGER.info(String.format("Один стикер на %s", duckName));
        }
    }

    private void loginUser() {
        driver.get(USER_URL);

        By locator = new By.ByXPath("//div/form/table//button[@name='login']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}

