package com.sample.tdsample;

import junit.framework.AssertionFailedError;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class techCrunchBrowserStackTest extends AbstractTest{

    static String currentTestState = "failed";

    public techCrunchBrowserStackTest() {
        super("techCrunchBrowserStackTest");
    }

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    @Before
    public void setUpTestNew() throws Exception {
        log("new test");
        // set window size
        setupDriver();
        modifyWindowSize();
    }

    @Override
    @After
    public void tearDownTest() throws Exception{
        log("tearDownTest");
        if (testPlatform.equals("browserstack"))
        {
            setTestNAmeAndStatus(name.getMethodName().toString(),currentTestState);
            uploadResultsToBrowserStack(BSTestName,BSTestStatus);
        }
        else
        {}
        if (driver != null) driver.quit();
    }

    @Test
    public void a_openPageTest() throws Exception {
        try {
            openPage();
            currentTestState = "passed";
        } catch (Exception | AssertionFailedError | ComparisonFailure e) {
            log(""+e);
            currentTestState = "failed";
            takeScreenShot("test_failed");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    @Test
    public void b_goToStartupsTest() throws Exception {
        try {
            openPage();
            goToStartups();
            currentTestState = "passed";
        } catch (Exception | AssertionFailedError | ComparisonFailure e) {
            log(""+e);
            currentTestState = "failed";
            takeScreenShot("test_failed");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    @Test
    public void c_goToAppsTest() throws Exception {
        try {
            openPage();
            goToApps();
            currentTestState = "passed";
        } catch (Exception | AssertionFailedError | ComparisonFailure e) {
            log(""+e);
            currentTestState = "failed";
            takeScreenShot("test_failed");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    @Test
    public void d_useSearchTest() throws Exception {
        try {
            openPage();
            useSearch();
            currentTestState = "passed";
        } catch (Exception | AssertionFailedError | ComparisonFailure e) {
            log(""+e);
            currentTestState = "failed";
            takeScreenShot("test_failed");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    @Test
    public void e_failTest() throws Exception {
        try {
            openPage();
            failureTestCase();
            currentTestState = "passed";
        } catch (Exception | AssertionFailedError | ComparisonFailure e) {
            log(""+e);
            currentTestState = "failed";
            takeScreenShot("test_failed");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    protected void openPage() throws Exception {

        String title;

        log("Start open tech crunch page test");
        driver.get("https://techcrunch.com");
        log("page opened");
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try
        {
            WebDriverWait wait = new WebDriverWait(driver,10);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("input[type='submit']"))));
            driver.findElement(By.cssSelector("input[type='submit']")).click();
        }
        catch (Exception e)
        {

        }

        // check if window is small and hamburger menu is visible instead of side menu
        checkMenuElement();
        log("get page title");
        title = driver.getTitle();
        Assert.assertTrue(title.contains("TechCrunch"));
        log("correct title");
        takeScreenShot("page-open");
    }

    protected void goToStartups() throws Exception {

        String currentUrl;
        WebElement startupElement;
        boolean startupsFound = false;

        log("Start go to startup test");
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        log("get page url");
        currentUrl = driver.getCurrentUrl();
        log("url:"+currentUrl);

        if (hamburgerMenuVisible)
        {
            openHamburgerMenu();
        }
        else
        {}

        WebDriverWait wait = new WebDriverWait(driver,20);
        startupElement = driver.findElement(By.cssSelector("ul[class='menu navigation__main-menu'] a[href='/startups/']"));
        startupsFound = true;
        startupElement.click();
        log("Startup clicked");
        sleep(10);

        Assert.assertTrue("locate Startups", startupsFound);
        currentUrl = driver.getCurrentUrl();
        Assert.assertEquals("https://techcrunch.com/startups/",currentUrl);
    }

    protected void goToApps() throws Exception {

        String currentUrl;
        WebElement appsElement;
        boolean appsFound = false;

        log("Start go to apps test");
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        log("get page url");
        currentUrl = driver.getCurrentUrl();
        log("url:"+currentUrl);

        if (hamburgerMenuVisible)
        {
            try
            {
                driver.findElement(By.tagName("g")).click();

            }catch (Exception e)
            {
                log("hamburger menu click error");
            }
        }
        else
        {}

        WebDriverWait wait = new WebDriverWait(driver,20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("ul[class='menu navigation__main-menu'] a[href='/apps/']"))));
        appsElement = driver.findElement(By.cssSelector("ul[class='menu navigation__main-menu'] a[href='/apps/']"));
        appsFound = true;
        appsElement.click();
        log("Apps clicked");
        sleep(10);

        Assert.assertTrue("locate Apps", appsFound);
        currentUrl = driver.getCurrentUrl();
        Assert.assertEquals("https://techcrunch.com/apps/",currentUrl);
    }

    protected void useSearch() throws Exception {

        String currentUrl;
        WebElement searchElement;
        WebElement searchField;
        WebElement searchResult;

        log("Start use search test");
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        log("get page url");
        currentUrl = driver.getCurrentUrl();
        log("url:"+currentUrl);

        if (hamburgerMenuVisible)
        {
            try
            {
                driver.findElement(By.tagName("g")).click();

            }catch (Exception e)
            {
                log("hamburger menu click error");
            }
        }
        else
        {}

        WebDriverWait wait = new WebDriverWait(driver,20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class='search-field']"))));
        searchElement = driver.findElement(By.cssSelector("label[for='nav-search-field']"));
        searchElement.click();

        log("search clicked");
        sleep(5);
        searchField = driver.findElement(By.cssSelector("input[class='search-form__input']"));
        searchField.sendKeys("mobile");
        searchField.sendKeys(Keys.RETURN);
        log("search mobile");
        sleep(5);

        searchResult = driver.findElement(By.cssSelector("span[class='editable-search-results']"));
        log("search result:"+searchResult.getText());
    }

    protected void failureTestCase() throws Exception {

        String currentUrl;

        log("Start failure test");
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        log("get page url");
        currentUrl = driver.getCurrentUrl();
        log("url:"+currentUrl);

        Assert.assertEquals("https://techcrunch.com/fail/",currentUrl);
    }
}
