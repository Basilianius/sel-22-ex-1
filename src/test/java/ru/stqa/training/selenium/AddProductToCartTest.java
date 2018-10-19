package ru.stqa.training.selenium;

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

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Titles.USER_TITLE;


public class AddProductToCartTest {

    private Logger LOGGER = LogManager.getLogger(AddProductToCartTest.class.getName());

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
    public void AddProductToCartTest001() throws Exception {
        //добавляем товары в корзину
        addProductToCart();
        addProductToCart();
        addProductToCart();

        //открываем корзину
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='cart']/a[@class='link']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        //удаляем товары из корзины
        StoryHelper.removeProductFromCart(driver, wait);
        StoryHelper.removeProductFromCart(driver, wait);
        StoryHelper.removeProductFromCart(driver, wait);
    }

    private void addProductToCart() throws Exception{
        //открываем страницу первого продукта
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-most-popular']/div//a[@class='link']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        //вызываем сценарий добавления продуктов в корзину
        StoryHelper.addProductToCart(driver, wait);

        //открываем главную страницу
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='logotype-wrapper']/a/img"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        By locator = new By.ByXPath("//div/form/table//button[@name='login']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));
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

