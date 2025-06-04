package selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NetworkDashboardTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:9095"); // Adjust as needed
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testPageTitle() {
        Assertions.assertTrue(driver.getTitle().contains("Network Traffic Dashboard"));
    }

    @Test
    void testSidebarNavigationTabs() {
        clickSidebarTab("traffic");
        WebElement trafficTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("traffic")));
        Assertions.assertTrue(trafficTab.getAttribute("class").contains("show"));

        clickSidebarTab("anomalies");
        WebElement anomaliesTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("anomalies")));
        Assertions.assertTrue(anomaliesTab.getAttribute("class").contains("show"));

        clickSidebarTab("dashboard");
        WebElement dashboardTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
        Assertions.assertTrue(dashboardTab.getAttribute("class").contains("show"));
    }

    @Test
    void testCatWidgetLoadsImage() {
        WebElement catImage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("catImage")));
        String src = catImage.getAttribute("src");
        Assertions.assertTrue(src.startsWith("https://") && src.contains("cdn2.thecatapi.com"), "Cat image did not load.");
    }

    @Test
    void testApplyFiltersButton() {
        WebElement timeDropdown = new Select(driver.findElement(By.id("timeRange"))).getFirstSelectedOption();
        Assertions.assertEquals("Last 24 hours", timeDropdown.getText());

        WebElement applyButton = driver.findElement(By.id("applyFilters"));
        Assertions.assertTrue(applyButton.isDisplayed() && applyButton.isEnabled());
        applyButton.click();
    }

    private void clickSidebarTab(String tabId) {
        WebElement tab = driver.findElement(By.cssSelector("a[href='#" + tabId + "']"));
        tab.click();
    }
}
