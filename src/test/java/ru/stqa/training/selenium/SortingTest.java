package ru.stqa.training.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.Constants.Credentials.PASSWORD;
import static settings.Constants.Credentials.USERNAME;
import static settings.Constants.Titles.ADMIN_TITLE;
import static settings.Constants.Urls.ADMIN_URL;


public class SortingTest {

    private Logger LOGGER = LogManager.getLogger(StickerTest.class.getName());

    private final static String COUNTRIES_PAGE = "http://localhost/litecart/admin/?app=countries&doc=countries";
    private final static String COUNTRIES_TITLE = "Countries | My Store";
    private final static String COUNTRY_EDIT_TITLE = "Edit Country | My Store";

    private final static String GEOZONES_PAGE = "http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones";
    private final static String GEOZONES_TITLE = "Geo Zones | My Store";
    private final static String GEOZONE_EDIT_TITLE = "Edit Geo Zone | My Store";

    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void before() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

        //логинимся на форму админки
        loginAdmin();
    }

    //    @Test
    public void SortingTest001() {
        //переходим на страницу со странами
        driver.get(COUNTRIES_PAGE);
        wait.until(titleIs(COUNTRIES_TITLE));

        //получаем список стран
        ArrayList<String> countries = getListNames("//*[@id='content']/form/table/tbody/tr[@class='row']/td[5]/a");

        //проверяет список на сортировку по алфвиту
        checkSorting(countries);
    }

    //    @Test
    public void SortingTest002() throws NullPointerException {
        //переходим на страницу со странами
        driver.get(COUNTRIES_PAGE);
        wait.until(titleIs(COUNTRIES_TITLE));

        //получаем список стран с НЕ нулевыми зонами
        ArrayList<String> countriesWithoutZero = getCountryWithoutZero("//*[@id='content']/form/table/tbody/tr[@class='row']");

        for (String countryItem : countriesWithoutZero) {
            //перебираем страны и кликаем по ним
            try {
                LOGGER.info(String.format("Проверка сортировки зон страны %s", countryItem));
                driver.findElement(By.xpath(String.format("//*[@id='content']/form/table/tbody/tr[@class='row']//a[text()='%s']", countryItem))).click();
                wait.until(titleIs(COUNTRY_EDIT_TITLE));

                //получаем список зон
                ArrayList<String> zones = getListNames("//table[@id='table-zones']//td/input[contains(@name,'name') and @type='hidden']//..");
                //проверяет список на сортировку по алфвиту
                try {
                    checkSorting(zones);
                } catch (UnsupportedOperationException ex) {
                    LOGGER.error(ex.getMessage());
                }

                //возвращаемся на страницу стран
                driver.get(COUNTRIES_PAGE);
                wait.until(titleIs(COUNTRIES_TITLE));

            } catch (TimeoutException | NoSuchElementException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    @Test
    public void SortingTest003() throws NullPointerException {
        //переходим на страницу с зонами
        driver.get(GEOZONES_PAGE);
        wait.until(titleIs(GEOZONES_TITLE));

        //получаем список стран
        ArrayList<String> countries = getListNames("//*[@id='content']/form/table/tbody/tr[@class='row']/td[3]/a");

        for (String countryItem : countries) {
            //перебираем страны и кликаем по ним
            try {
                LOGGER.info(String.format("Проверка сортировки зон страны %s", countryItem));
                driver.findElement(By.xpath(String.format("//*[@id='content']/form/table/tbody/tr[@class='row']//a[text()='%s']", countryItem))).click();
                wait.until(titleIs(GEOZONE_EDIT_TITLE));

                //получаем список зон
                ArrayList<String> zones = getListNames("//table[@id='table-zones']//td/select[contains(@name,'zone_code')]/option[@selected='selected']");

                //проверяет список на сортировку по алфавиту
                try {
                    checkSorting(zones);
                } catch (UnsupportedOperationException ex) {
                    LOGGER.error(ex.getMessage());
                }

                //возвращаемся на страницу стран
                driver.get(GEOZONES_PAGE);
                wait.until(titleIs(GEOZONES_TITLE));

            } catch (TimeoutException | NoSuchElementException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
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

    private void checkSorting(ArrayList<String> list) {
        LOGGER.info("Проверка сортировки списка");

        ArrayList<String> orderedList = new ArrayList<String>(list);
        Collections.sort(orderedList);

        assertEquals(list, orderedList);
    }

    private ArrayList<String> getListNames(String locator) {
        ArrayList<String> list = new ArrayList<String>();

        ArrayList<WebElement> webElements = (ArrayList<WebElement>) driver.findElements(By.xpath(locator));
        if (webElements.size() != 0)
            for (WebElement item : webElements) {
                list.add(item.getText());
            }
        return list;
    }

    private ArrayList<String> getCountryWithoutZero(String locator) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<WebElement> webElements = (ArrayList<WebElement>) driver.findElements(By.xpath(locator));
        if (webElements.size() != 0)
            for (WebElement item : webElements) {
                int countZones = Integer.valueOf(item.findElement(By.xpath("./td[6]")).getText());
                if (countZones > 0) {
                    WebElement webElement = item.findElement(By.xpath(".//a"));
                    list.add(webElement.getText());
                }
            }
        return list;
    }

    private void loginAdmin() {
        driver.get(ADMIN_URL);
        wait.until(titleIs(ADMIN_TITLE));

        driver.findElement(By.name("username")).sendKeys(USERNAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("login")).click();

        By locator = new By.ByXPath("//*[@title='Logout']");
        wait.until(visibilityOfElementLocated(locator));
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
