package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Titles.USER_TITLE;

public class MainPage {

    private final String checkPageLocator = "//div/form/table//button[@name='login']";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private Logger LOGGER = LogManager.getLogger(MainPage.class.getName());

    private String firstProductLinkLocator = "//*[@id='box-most-popular']/div//a[@class='link']";
    private String cartLinkLocator = "//*[@id='cart']/a[@class='link']";

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;

        wait.until(visibilityOfElementLocated(By.xpath(checkPageLocator)));
        wait.until(titleIs(USER_TITLE));

        LOGGER.info("Открытие главной страницы - УСПЕШНО");
    }

    public ProductPage openFirstProduct() {
        //открываем страницу первого продукта
        try {
            WebElement webElement = driver.findElement(By.xpath(firstProductLinkLocator));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        LOGGER.info("Открываем первый продукт на главной странице - УСПЕШНО");

        return new ProductPage(driver, wait);
    }

    public CartPage openCart(){
        //открываем корзину
        try {
            WebElement webElement = driver.findElement(By.xpath(cartLinkLocator));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        LOGGER.info("Открываем корзину - УСПЕШНО");

        return new CartPage(driver, wait);
    }

}
