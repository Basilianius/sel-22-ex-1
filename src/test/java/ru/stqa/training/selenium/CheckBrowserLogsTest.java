package ru.stqa.training.selenium;

import helpers.StoryHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static settings.Constants.Titles.ADMIN_CATALOG_TITLE;
import static settings.Constants.Titles.ADMIN_EDIT_PRODUCT_TITLE;


public class CheckBrowserLogsTest {

    private Logger LOGGER = LogManager.getLogger(CheckBrowserLogsTest.class.getName());
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
    public void CheckBrowserLogsTest001() throws Exception {
        //переходим в каталог, открываем подкатегорию
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[@id='app-']/a/span[contains(text(),'Catalog')]"));
            webElement.click();
            wait.until(titleIs(ADMIN_CATALOG_TITLE));

            webElement = driver.findElement(By.xpath("//*[@id='content']/form/table//i[contains(@class, 'fa fa-folder')]/../a"));
            webElement.click();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        ArrayList<String> productNames = getProductNames("//*[@id='content']/form/table//img/../a");

        for (int i = 0; i < productNames.size(); i++) {
            //открываем поочередно страницы товара
            try {
                WebElement webElement = driver.findElement(By.xpath(String.format("//*[@id='content']/form/table//img/../a[contains(text(),'%s')]", productNames.get(i))));
                webElement.click();

                LOGGER.info("Вывод логов браузера:");
                List<LogEntry> logs =  driver.manage().logs().get("browser").getAll();
                assertEquals(String.format("Лог браузера НЕ пустой: %s", logs.toString()), logs.size(), 0);

                By locator = new By.ByXPath("//*[@id='content']/h1[contains(text(),'Edit Product:')]");
                wait.until(visibilityOfElementLocated(locator));
                wait.until(titleContains(ADMIN_EDIT_PRODUCT_TITLE));

                webElement = driver.findElement(By.xpath("//*[@id='content']/form//button[@name='cancel']"));
                webElement.click();
                wait.until(titleIs(ADMIN_CATALOG_TITLE));

                for (int count = 0; ; count++) {
                    if (count >= 10)
                        throw new TimeoutException();
                    try {

                        if (driver.findElements(By.xpath("//*[@id='content']/h1[contains(text(),'Catalog')]")).size() < 0)
                            throw new NoSuchElementException("Catalog НЕ открылся");
                        break;
                    } catch (NoSuchElementException e) {
                    }
                    Thread.sleep(1000);
                }

            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }


    private ArrayList<String> getProductNames(String locator) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<WebElement> webElements = (ArrayList<WebElement>) driver.findElements(By.xpath(locator));
        if (webElements.size() != 0)
            for (WebElement item : webElements) {
                list.add(item.getText());
            }
        return list;
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
