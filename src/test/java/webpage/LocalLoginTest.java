package webpage;
import java.io.File;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
public class LocalLoginTest {
	WebDriver driver;
    String pageUrl;

    @BeforeMethod
    public void setup() throws Exception {

        ChromeOptions options = new ChromeOptions();

        String remoteUrl =
            System.getenv("SELENIUM_REMOTE_URL");

        boolean remote =
            remoteUrl != null && !remoteUrl.isBlank();

        String headless = System.getenv("HEADLESS");

        if (remote || "true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1920,1080");

        if (remote) {

            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            driver = new RemoteWebDriver(
                new URL(remoteUrl), options
            );

            pageUrl = System.getenv("BASE_URL");

        } else {

            driver = new ChromeDriver(options);

            File file = new File("src/test/java/webpage/login.html");
            pageUrl = file.getAbsoluteFile()
                          .toURI().toString();
        }
    }

    @Test
    public void validLoginTest() {

        driver.get(pageUrl);

        driver.findElement(By.id("username"))
              .sendKeys("student");

        driver.findElement(By.id("password"))
              .sendKeys("12345");

        driver.findElement(By.tagName("button"))
              .click();

        String result =
            driver.findElement(By.id("message"))
                  .getText();

        Assert.assertEquals(
            result, "Login Successful"
        );
    }

    @Test
    public void invalidLoginTest() {

        driver.get(pageUrl);

        driver.findElement(By.id("username"))
              .sendKeys("wrong");

        driver.findElement(By.id("password"))
              .sendKeys("wrong123");

        driver.findElement(By.tagName("button"))
              .click();

        String result =
            driver.findElement(By.id("message"))
                  .getText();

        Assert.assertEquals(
            result, "Invalid Username or Password"
        );
    }

    @Test
    public void backgroundColorTest() {

        driver.get(pageUrl);

        String color =
            driver.findElement(By.tagName("body"))
                  .getCssValue("background-color");

        Assert.assertEquals(
            color, "rgba(173, 216, 230, 1)"
        );
    }

    @AfterMethod
    public void closeBrowser() {

        if (driver != null) {
            driver.quit();
        }
    }
}
