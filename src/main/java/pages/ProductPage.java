package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Titles.PRODUCT_TITLE;

public class ProductPage {

    private final String checkPageLocator = "//*[@id='box-product']//div[@class='content']//a[@class='main-image fancybox zoomable shadow']";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private Logger LOGGER = LogManager.getLogger(ProductPage.class.getName());

    private String sizeSelectLocator = "//*[@id='box-product']//div[@class='content']//td/select[@name='options[Size]']";
    private String addProductButtonLocator = "//*[@id='box-product']//div[@class='buy_now']//td/button[@name='add_cart_product']";
    private String logoImageLocator = "//*[@id='logotype-wrapper']/a/img";

    private String productsInCartCountLocator = "//*[@id='cart']/a[@class='content']/span[@class='quantity']";

    public ProductPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;

        wait.until(visibilityOfElementLocated(By.xpath(checkPageLocator)));
        wait.until(titleContains(PRODUCT_TITLE));

        LOGGER.info("Открытие страницы продукта - УСПЕШНО");
    }

    public ProductPage addProductToCart() throws Exception {
        //получаем текущее колво товаров в корзине
        int beforeProductCount = getCountProductFromCart();

        //выбираем размер при необходимости
        if (driver.findElements(By.xpath(sizeSelectLocator)).size() > 0) {
            selectText(sizeSelectLocator, "Small");
        }

        //вызываем добавление продукта
        try {
            WebElement webElement = driver.findElement(By.xpath(addProductButtonLocator));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        //проверяем что счетчик товаров изменился
        for (int count = 0; ; count++) {
            if (count >= 20)
                throw new TimeoutException();
            try {
                int afterProductCount = getCountProductFromCart();
                if (afterProductCount - beforeProductCount != 1)
                    throw new NoSuchElementException("Счетчик в корзине НЕ обновился");
                break;
            } catch (NoSuchElementException e) {
            }
            Thread.sleep(1000);
        }

        LOGGER.info("Добавление продукта в корзину - УСПЕШНО");

        return this;
    }

    public MainPage returnToMainPage(){
        //открываем главную страницу
        try {
            WebElement webElement = driver.findElement(By.xpath(logoImageLocator));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        LOGGER.info("Возвращение на главную страницу - УСПЕШНО");
        return new MainPage(driver,wait);
    }


    private int getCountProductFromCart() {
        int productsCount = 0;
        try {
            WebElement webElement = driver.findElement(By.xpath(productsInCartCountLocator));
            productsCount = Integer.valueOf(webElement.getText());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return productsCount;
    }

    private void selectText(String locator, String text) {
        try {
            WebElement webElement = driver.findElement(By.xpath(locator));
            Select select = new Select(webElement);
            select.selectByVisibleText(text);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}

