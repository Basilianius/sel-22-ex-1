package ru.stqa.training.selenium;

import helpers.StoryHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;


public class NewAddProductToCartTest {

    private Logger LOGGER = LogManager.getLogger(NewAddProductToCartTest.class.getName());

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
    public void NewAddProductToCartTest001() throws Exception {
        //добавляем товары в корзину
        MainPage mainPage = new MainPage(driver, wait);
        ProductPage productPage = mainPage.openFirstProduct();
        productPage.addProductToCart();
        mainPage = productPage.returnToMainPage();

        productPage = mainPage.openFirstProduct();
        productPage.addProductToCart();
        mainPage = productPage.returnToMainPage();

        productPage = mainPage.openFirstProduct();
        productPage.addProductToCart();
        mainPage = productPage.returnToMainPage();

        //удаляем товары из корзины
        CartPage cartPage = mainPage.openCart();
        cartPage.removeProductFromCart();
        cartPage.removeProductFromCart();
        cartPage.removeProductFromCart();

        cartPage.returnToMainPage();
    }


    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}

