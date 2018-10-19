package helpers;

import entities.Account;
import entities.DuckProduct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Credentials.PASSWORD;
import static settings.Constants.Credentials.USERNAME;
import static settings.Constants.Titles.*;
import static settings.Constants.Urls.ADMIN_URL;
import static settings.Constants.Urls.USER_URL;

/**
 * Сценарии логина в магазин
 */
public class StoryHelper {
    public static Logger LOGGER = LogManager.getLogger(StoryHelper.class.getName());

    public static void loginAdmin(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вход на страницу администратора");
        driver.get(ADMIN_URL);
        wait.until(titleIs(ADMIN_TITLE));

        driver.findElement(By.name("username")).sendKeys(USERNAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();

        By locator = new By.ByXPath("//*[@title='Logout']");
        wait.until(visibilityOfElementLocated(locator));

        LOGGER.info("Вход на страницу администратора - УСПЕШНО");
    }

    public static void fillNewProduct(WebDriver driver, WebDriverWait wait, DuckProduct product) {
        LOGGER.info("Заполнение полей нового продукта");
        try {
            //вкладка General
            for (int count = 0; ; count++) {
                if (count >= 10)
                    throw new TimeoutException();
                try {

                    if (driver.findElements(By.xpath("//*[@id='content']/form//li[@class='active']/a[contains(text(), 'General')]")).size() < 0)
                        throw new NoSuchElementException("Вкладка General НЕ открылась");
                    break;
                } catch (NoSuchElementException e) {
                }
                Thread.sleep(1000);
            }

            WebElement webElement = driver.findElement(By.xpath("//*[@id='tab-general']/table//label/input[@name='status' and @value='1']"));
            webElement.click();

            webElement = driver.findElement(By.xpath("//*[@id='tab-general']/table//td//input[@name='name[en]']"));
            webElement.sendKeys(product.getName());

            webElement = driver.findElement(By.xpath("//*[@id='tab-general']/table//td//input[@name='quantity']"));
            webElement.sendKeys(String.valueOf(product.getQuantity()));

            webElement = driver.findElement(By.xpath("//*[@id='tab-general']/table//td//input[@name='new_images[]']"));
            webElement.sendKeys(product.getFilePath());

            //вкладка Information
            webElement = driver.findElement(By.xpath("//*[@id='content']/form//a[contains(text(), 'Information')]"));
            webElement.click();

            for (int count = 0; ; count++) {
                if (count >= 10)
                    throw new TimeoutException();
                try {

                    if (driver.findElements(By.xpath("//*[@id='content']/form//li[@class='active']/a[contains(text(), 'Information')]")).size() < 0)
                        throw new NoSuchElementException("Вкладка Information НЕ открылась");
                    break;
                } catch (NoSuchElementException e) {
                }
                Thread.sleep(1000);
            }

            webElement = driver.findElement(By.xpath("//*[@id='tab-information']/table//td/select[@name='manufacturer_id']"));
            Select select = new Select(webElement);
            select.selectByVisibleText("ACME Corp.");

            webElement = driver.findElement(By.xpath("//*[@id='tab-information']/table//td//input[@name='short_description[en]']"));
            webElement.sendKeys("Very short product description");

            webElement = driver.findElement(By.xpath("//*[@id='tab-information']/table//td//textarea[@name='description[en]']"));
            webElement.sendKeys("Very big product description");

            webElement = driver.findElement(By.xpath("//*[@id='tab-information']/table//td//input[@name='head_title[en]']"));
            webElement.sendKeys("Head title");

            //вкладка Information
            webElement = driver.findElement(By.xpath("//*[@id='content']/form//a[contains(text(), 'Prices')]"));
            webElement.click();

            for (int count = 0; ; count++) {
                if (count >= 10)
                    throw new TimeoutException();
                try {

                    if (driver.findElements(By.xpath("//*[@id='content']/form//li[@class='active']/a[contains(text(), 'Prices')]")).size() < 0)
                        throw new NoSuchElementException("Вкладка Prices НЕ открылась");
                    break;
                } catch (NoSuchElementException e) {
                }
                Thread.sleep(1000);
            }

            webElement = driver.findElement(By.xpath("//*[@id='tab-prices']/table//td//input[@name='purchase_price']"));
            webElement.sendKeys(product.getRegularPrice());

            webElement = driver.findElement(By.xpath("//*[@id='tab-prices']/table//td/select[@name='purchase_price_currency_code']"));
            select = new Select(webElement);
            select.selectByVisibleText("US Dollars");

            //сохранение
            webElement = driver.findElement(By.xpath("//*[@id='content']/form//button[@name='save']"));
            webElement.click();

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        LOGGER.info("Заполнение полей нового продукта - УСПЕШНО");
    }

    public static void openMainPage(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вход на главную страницу");
        driver.get(USER_URL);

        By locator = new By.ByXPath("//div/form/table//button[@name='login']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));
        LOGGER.info("Вход на главную страницу - УСПЕШНО");
    }

    public static void addProductToCart(WebDriver driver, WebDriverWait wait) throws Exception {
        LOGGER.info("Добавление продукта в корзину");

        By locator = new By.ByXPath("//*[@id='box-product']//div[@class='content']//a[@class='main-image fancybox zoomable shadow']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleContains(PRODUCT_TITLE));

        //получаем текущее колво товаров в корзине
        int beforeProductCount = getCountProductFromCart(driver, wait);

        //выбираем размер при необходимости
        String locatorLine = "//*[@id='box-product']//div[@class='content']//td/select[@name='options[Size]']";
        if (driver.findElements(By.xpath(locatorLine)).size() > 0) {
            selectText(driver, wait, locatorLine, "Small");
        }

        //вызываем добавление продукта
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-product']//div[@class='buy_now']//td/button[@name='add_cart_product']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        //проверяем что счетчик товаров изменился
        for (int count = 0; ; count++) {
            if (count >= 20)
                throw new TimeoutException();
            try {
                int afterProductCount = getCountProductFromCart(driver, wait);
                if (afterProductCount - beforeProductCount != 1)
                    throw new NoSuchElementException("Счетчик в корзине НЕ обновился");
                break;
            } catch (NoSuchElementException e) {
            }
            Thread.sleep(1000);
        }

        LOGGER.info("Добавление продукта в корзину - УСПЕШНО");
    }

    private static int getCountProductFromCart(WebDriver driver, WebDriverWait wait) {
        int productsCount = 0;
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='cart']/a[@class='content']/span[@class='quantity']"));
            productsCount = Integer.valueOf(webElement.getText());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return productsCount;
    }

    public static void removeProductFromCart(WebDriver driver, WebDriverWait wait) throws Exception {
        LOGGER.info("Удаление продукта из корзины");
        int beforeProductCount = getCountProductFromTable(driver, wait);

        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-checkout-cart']/div//button[@name='remove_cart_item']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        //проверяем что счетчик товаров в таблице изменился
        for (int count = 0; ; count++) {
            if (count >= 20)
                throw new TimeoutException();
            try {
                int afterProductCount = getCountProductFromTable(driver, wait);
                if (beforeProductCount - afterProductCount != 1)
                    throw new NoSuchElementException("Таблица списка товаров в корзине НЕ обновилась");
                break;
            } catch (NoSuchElementException e) {
            }
            Thread.sleep(1000);
        }


        LOGGER.info("Удаление продукта из корзины - УСПЕШНО");
    }

    private static int getCountProductFromTable(WebDriver driver, WebDriverWait wait) {
        int productsCount = 0;
        //проверка пустой корзины
        if (driver.findElements(By.xpath("//*[@id='checkout-cart-wrapper']/p/a")).size() > 0)
            return productsCount;

        try {
            productsCount = driver.findElements(By.xpath("//*[@id='order_confirmation-wrapper']/table/tbody/tr/td[@class='item']/..")).size();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return productsCount;
    }

    public static void createAccount(WebDriver driver, WebDriverWait wait, Account account) {
        LOGGER.info("Регистрация нового пользователя");

        By locator = new By.ByXPath("//*[@id='create-account']/div//td/button[@name='create_account']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(CREATE_ACCOUNT_TITLE));

        //Заполнение обязательных полей
        LOGGER.info("Заполнение ФИО");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='firstname']", account.getFirstName());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='lastname']", account.getLastName());

        LOGGER.info("Заполнение адреса города емайла телефона");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='address1']", account.getAddress());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='city']", account.getCity());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='email']", account.getEmail());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='phone']", account.getPhone());

        LOGGER.info("Заполнение страны и индекса");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='postcode']", account.getPostcode());
        selectText(driver, wait, "//*[@id='create-account']/div//td/select[@name='country_code']", account.getCountry());

        LOGGER.info("Заполнение пароля");
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='password']", account.getPassword());
        inputText(driver, wait, "//*[@id='create-account']/div//td/input[@name='confirmed_password']", account.getConfirmedPassword());

        //вызываем создание аккаунта
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='create-account']/div//td/button[@name='create_account']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        //проверяем открытие главной страницы с залогированным пользователем
        openLoginMainPage(driver, wait);

        LOGGER.info("Регистрация нового пользователя - УСПЕШНО");
    }

    public static void loginAccount(WebDriver driver, WebDriverWait wait, Account account) {
        LOGGER.info("Вход зарегистрированным пользователем");

        //заполнение емайла и пароля
        LOGGER.info("Заполнение кредов");
        inputText(driver, wait, "//*[@id='box-account-login']/div//td/input[@name='email']", account.getEmail());
        inputText(driver, wait, "//*[@id='box-account-login']/div//td/input[@name='password']", account.getPassword());

        //логинимся
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-account-login']/div//td//button[@name='login']"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        //проверяем открытие главной страницы с залогированным пользователем
        openLoginMainPage(driver, wait);

        LOGGER.info("Вход зарегистрированным пользователем - УСПЕШНО");
    }

    public static void logout(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вылогирование из аккаунта");
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='box-account']/div//a[contains(text(),'Logout')]"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        By locator = new By.ByXPath("//div/form/table//button[@name='login']");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));

        LOGGER.info("Вылогирование из аккаунта - УСПЕШНО");
    }

    public static void openLoginMainPage(WebDriver driver, WebDriverWait wait) {
        LOGGER.info("Вход на главную страницу залогининым пользователем");

        By locator = new By.ByXPath("//*[@id='box-account']/div//a[contains(text(),'Logout')]");
        wait.until(visibilityOfElementLocated(locator));

        wait.until(titleIs(USER_TITLE));
        LOGGER.info("Вход на главную страницу залогининым пользователем - УСПЕШНО");
    }

    private static void inputText(WebDriver driver, WebDriverWait wait, String locator, String text) {
        try {
            WebElement webElement = driver.findElement(By.xpath(locator));
            webElement.sendKeys(text);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private static void selectText(WebDriver driver, WebDriverWait wait, String locator, String text) {
        try {
            WebElement webElement = driver.findElement(By.xpath(locator));
            Select select = new Select(webElement);
            select.selectByVisibleText(text);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private static boolean isElementPresent(WebDriver driver, WebDriverWait wait, By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }


}
