package ru.stqa.training.selenium.crossLogin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class LoginFirefoxTest {

    private String USERNAME = "admin";
    private String PASSWORD = "admin";

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void LoginTest001() {
        driver.get("http://localhost/litecart/admin");
        wait.until(titleIs("My Store"));

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
