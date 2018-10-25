package ru.stqa.training.selenium;

import entities.DuckProduct;
import helpers.StoryHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Generator;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static settings.Constants.Titles.ADMIN_ADD_NEW_PRODUCT_TITLE;
import static settings.Constants.Titles.ADMIN_CATALOG_TITLE;


public class AddNewProductTest {

    private Logger LOGGER = LogManager.getLogger(StickerTest.class.getName());
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
    public void AddNewProductTest001() throws Exception {
        //переходим в каталог, кликаем кнопку добавления продукта
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='app-']/a/span[contains(text(),'Catalog')]"));
            webElement.click();
            wait.until(titleIs(ADMIN_CATALOG_TITLE));

            webElement = driver.findElement(By.xpath("//*[@id='content']/div/a[contains(text(),' Add New Product')]"));
            webElement.click();
            wait.until(titleIs(ADMIN_ADD_NEW_PRODUCT_TITLE));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        DuckProduct product = new DuckProduct(Generator.generateProductName(), String.valueOf(Generator.generateProductPrice()), Generator.generateProductCount());

        StoryHelper.fillNewProduct(driver, wait, product);

        for (int count = 0; ; count++) {
            if (count >= 10)
                throw new TimeoutException();
            try {

                if (driver.findElements(By.xpath(String.format("//*[@id='content']/form//td/a[contains(text(),'%s')]", product.getName()))).size() < 0)
                    throw new NoSuchElementException("Продукт НЕ добавился");
                break;
            } catch (NoSuchElementException e) {
            }
            Thread.sleep(1000);
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
