package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Titles.CART_TITLE;

public class CartPage {

    private final String checkPageLocator = "//*[@id='order_confirmation-wrapper']/form//button[@name='confirm_order']";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private Logger LOGGER = LogManager.getLogger(CartPage.class.getName());

    private String removeButtonLocator = "//*[@id='box-checkout-cart']/div//button[@name='remove_cart_item']";
    private String logoImageLocator = "//*[@id='logotype-wrapper']/a/img";

    private String emptyCartLinkLocator = "//*[@id='checkout-cart-wrapper']/p/a";
    private String productTableLineLocator = "//*[@id='order_confirmation-wrapper']/table/tbody/tr/td[@class='item']/..";


    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;

        wait.until(visibilityOfElementLocated(By.xpath(checkPageLocator)));
        wait.until(titleIs(CART_TITLE));

        LOGGER.info("Открытие корзины - УСПЕШНО");
    }

    public CartPage removeProductFromCart() throws Exception {
        //получаем колво продуктов в корзине
        int beforeProductCount = getCountProductFromTable();

        try {
            WebElement webElement = driver.findElement(By.xpath(removeButtonLocator));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        //проверяем что счетчик товаров в таблице изменился
        for (int count = 0; ; count++) {
            if (count >= 20)
                throw new TimeoutException();
            try {
                int afterProductCount = getCountProductFromTable();
                if (beforeProductCount - afterProductCount != 1)
                    throw new NoSuchElementException("Таблица списка товаров в корзине НЕ обновилась");
                break;
            } catch (NoSuchElementException e) {
            }
            Thread.sleep(1000);
        }

        LOGGER.info("Удаление продукта из корзины - УСПЕШНО");

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


    private int getCountProductFromTable() {
        int productsCount = 0;
        //проверка пустой корзины
        if (driver.findElements(By.xpath(emptyCartLinkLocator)).size() > 0)
            return productsCount;

        try {
            productsCount = driver.findElements(By.xpath(productTableLineLocator)).size();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return productsCount;
    }
}

