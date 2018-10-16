package ru.stqa.training.selenium;

import comparators.Comparator;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class CheckProductTest {

    private Logger LOGGER = LogManager.getLogger(CheckProductTest.class.getName());

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

        //логинимся
        StoryHelper.openMainPage(driver, wait);
    }

    @Test
    public void CheckProductTest001() throws NullPointerException {
        //получаем данные по уточке на главной странице
        DuckProduct productFromMainPage = getProductFromMainPage("//*[@id='box-campaigns']/div[@class='content']//a[@class='link']");
        //переходим на страницу уточки и получаем там данные
        productFromMainPage.getLink().click();
        DuckProduct productFromProductPage = getProductFromProductPage("//*[@id='box-product']");

        //сравниваем уточек на разных страницах
        assertTrue("Проверка одинаковости уточек по основным параметрам", Comparator.equalsDuckProduct(productFromMainPage, productFromProductPage));
        //проверка цветов цен
        assertTrue("Проверка на серость дефолтной цены на главной странице", Comparator.checkColorGray(productFromMainPage.getRegularPriceColor()));
        assertTrue("Проверка на серость дефолтной цены на странице продукта", Comparator.checkColorGray(productFromProductPage.getRegularPriceColor()));
        assertTrue("Проверка на красность акционной цены на главной странице", Comparator.checkColorRed(productFromMainPage.getCampaignPriceColor()));
        assertTrue("Проверка на красность акционной цены на странице продукта", Comparator.checkColorRed(productFromProductPage.getCampaignPriceColor()));
        //проверка размеров шрифта
        assertTrue("Проверка большести акционного шрифта на главной странице", Comparator.checkUpperSizeText(productFromMainPage.getRegularPriceSize(), productFromMainPage.getCampaignPriceSize()));
        assertTrue("Проверка большести акционного шрифта на странице продукта", Comparator.checkUpperSizeText(productFromProductPage.getRegularPriceSize(), productFromProductPage.getCampaignPriceSize()));
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

    private DuckProduct getProductFromMainPage(String locator) {
        LOGGER.info("Получение информации по уточке с главной страницы");
        DuckProduct product = null;

        try {
            WebElement webElement = driver.findElement(By.xpath(locator));
            product = new DuckProduct(
                    webElement,
                    webElement.findElement(By.xpath("./div[@class='name']")).getText(),
                    webElement.findElement(By.xpath("./div[@class='price-wrapper']/s[@class='regular-price']")).getText(),
                    webElement.findElement(By.xpath("./div[@class='price-wrapper']/s[@class='regular-price']")).getCssValue("color"),
                    webElement.findElement(By.xpath("./div[@class='price-wrapper']/s[@class='regular-price']")).getCssValue("font-size"),
                    webElement.findElement(By.xpath("./div[@class='price-wrapper']/strong[@class='campaign-price']")).getText(),
                    webElement.findElement(By.xpath("./div[@class='price-wrapper']/strong[@class='campaign-price']")).getCssValue("color"),
                    webElement.findElement(By.xpath("./div[@class='price-wrapper']/strong[@class='campaign-price']")).getCssValue("font-size")
            );
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        assertNotNull("Проверка что уточка с главной страницы НЕ пустая", product);
        LOGGER.info(product.toString());
        return product;
    }

    private DuckProduct getProductFromProductPage(String locator) {
        LOGGER.info("Получение информации по уточке со страницы уточки");
        DuckProduct product = null;

        try {
            WebElement webElement = driver.findElement(By.xpath(locator));
            product = new DuckProduct(
                    webElement,
                    webElement.findElement(By.xpath("./div/h1[@class='title']")).getText(),
                    webElement.findElement(By.xpath(".//div[@class='price-wrapper']/s[@class='regular-price']")).getText(),
                    webElement.findElement(By.xpath(".//div[@class='price-wrapper']/s[@class='regular-price']")).getCssValue("color"),
                    webElement.findElement(By.xpath(".//div[@class='price-wrapper']/s[@class='regular-price']")).getCssValue("font-size"),
                    webElement.findElement(By.xpath(".//div[@class='price-wrapper']/strong[@class='campaign-price']")).getText(),
                    webElement.findElement(By.xpath(".//div[@class='price-wrapper']/strong[@class='campaign-price']")).getCssValue("color"),
                    webElement.findElement(By.xpath(".//div[@class='price-wrapper']/strong[@class='campaign-price']")).getCssValue("font-size")
            );
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        assertNotNull("Проверка что уточка со страницы уточки НЕ пустая", product);
        LOGGER.info(product.toString());
        return product;
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}

