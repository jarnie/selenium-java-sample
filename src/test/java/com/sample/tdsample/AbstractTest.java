package com.sample.tdsample;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import jdk.nashorn.internal.runtime.ECMAException;
import okhttp3.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTest extends AbstractSeleniumTest implements SauceOnDemandSessionIdProvider {

    protected static WebDriver driver;
    protected static String sessionId;
    protected static boolean hamburgerMenuVisible = false;

    protected static String BSTestName = "a_openPageTest";
    protected static String BSTestStatus;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");

    private final String testName;

    public AbstractTest(String testName) {
        if (testName != null && testName.length() > 0) {
            this.testName = testName + "_";
        } else this.testName = "";
    }

    public static SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(
            System.getenv("SAUCE_USER"), System.getenv("SAUCE_KEY"));

    public static void setupDriver() throws Exception {
        boolean canRun = false;
        if (!testPlatform.isEmpty() && !testPlatform.equals(""))
        {
            switch (testPlatform) {
                case "local":
                    if (browserToRunTest.equals("firefox"))
                    {
                        driver = getLocalFirefoxDriver();
                    }
                    if (browserToRunTest.equals("chrome"))
                    {
                        driver = getLocalChromeDriver();
                    }
                    canRun = true;
                    break;
                case "sauce":
                    if (browserToRunTest.equals("firefox"))
                    {
                        driver = getFirefoxSauceDriver();
                    }
                    if (browserToRunTest.equals("chrome"))
                    {
                        driver = getChromeSauceDriver();
                    }
                    canRun = true;
                    break;
                case "browserstack":
                    if (browserToRunTest.equals("firefox"))
                    {
                        driver = getFirefoxBrowserStackDriver();
                    }
                    if (browserToRunTest.equals("chrome"))
                    {
                        driver = getChromeBrowserStackDriver();
                    }
                    canRun = true;
                    break;
                case "bitbar":  driver = getLocalFirefoxDriver();
                    canRun = false;
                    break;
                default: canRun = false;
                    break;
            }
            if (!canRun)
            {
                throw new Exception("failed to read: 'PLATFORM_TO_RUN' value");
            }
        }
    }

    //@BeforeClass
    public static void setUp() throws Exception {

        setupDriver();
    }

    @Before
    public void setUpTest() throws Exception{
        log("Setting implicit wait to " + defaultWaitTime + " seconds");
        //driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        //setupDriver();
    }

    @After
    public void tearDownTest() throws Exception{
        log("tearDownTest");
        if (driver != null) driver.quit();
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) driver.quit();
    }

    public static WebDriver getLocalChromeDriver() throws Exception {

        ChromeOptions options = new ChromeOptions();
        screenshotsFolder = System.getProperty("user.dir") + "/screenshots/";

        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        if (!testPlatformOSType.isEmpty() && !testPlatformOSType.equals(""))
        {
            switch (testPlatformOSType) {
                case "mac": options.setCapability("platform", "MAC");
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/mac/chrome/chromedriver");
                    break;
                case "linux": options.setCapability("platform", "LINUX");
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/linux/chrome/chromedriver");
                    break;
                default:
                    break;
            }
        }

        System.out.println("Creating Selenium session, this may take couple minutes..");
        options.addArguments("--headless");
        options.addArguments("window-size=1280x720");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver getLocalFirefoxDriver() throws Exception {

        FirefoxOptions options = new FirefoxOptions();
        screenshotsFolder = System.getProperty("user.dir") + "/screenshots/";

        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        if (!testPlatformOSType.isEmpty() && !testPlatformOSType.equals(""))
        {
            switch (testPlatformOSType) {
                case "mac": options.setCapability("platform", "MAC");
                    System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/drivers/mac/gecko/geckodriver");
                    break;
                case "linux": options.setCapability("platform", "LINUX");
                    System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/drivers/linux/gecko/geckodriver");
                    break;
                default:
                    break;
            }
        }

        System.out.println("Creating Selenium session, this may take couple minutes..");
        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver getChromeSauceDriver() throws Exception {

        ChromeOptions options = new ChromeOptions();
        screenshotsFolder = System.getProperty("user.dir") + "/screenshots/";

        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        if (!testPlatformOSType.isEmpty() && !testPlatformOSType.equals(""))
        {
            switch (testPlatformOSType) {
                case "mac":
                    options.setCapability("platform", "macOS 10.13");
                    options.setCapability("version", "71.0");
                    options.setCapability("chromedriverVersion", "2.43");
                    options.setCapability("screenResolution", "1920x1440");
                    options.setCapability("name", BSTestName);
                    break;
                case "linux":
                    options.setCapability("platform", "Linux");
                    options.setCapability("version", "48.0");
                    options.setCapability("screenResolution", "1024x768");
                    options.setCapability("name", BSTestName);
                    break;
                case "windows":
                    options.setCapability("platform", "Windows 10");
                    options.setCapability("version", "71.0");
                    options.setCapability("screenResolution", "1920x1080");
                    options.setCapability("name", BSTestName);
                    break;
                default:
                    break;
            }
        }

        options.setCapability("browserName", "chrome");

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        System.out.println("Creating Selenium session, this may take couple minutes..");
        WebDriver driver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"), capabilities);
        sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver getFirefoxSauceDriver() throws Exception {

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        FirefoxOptions options = new FirefoxOptions(capabilities);

        screenshotsFolder = System.getProperty("user.dir") + "/screenshots/";

        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        if (!testPlatformOSType.isEmpty() && !testPlatformOSType.equals(""))
        {
            switch (testPlatformOSType) {
                case "mac":
                    options.setCapability("platform", "macOS 10.13");
                    options.setCapability("version", "64.0");
                    options.setCapability("screenResolution", "1920x1440");
                    options.setCapability("name", BSTestName);
                    break;
                case "linux":
                    options.setCapability("platform", "Linux");
                    options.setCapability("version", "45.0");
                    options.setCapability("screenResolution", "1024x768");
                    options.setCapability("name", BSTestName);
                    break;
                case "windows":
                    options.setCapability("platform", "Windows 10");
                    options.setCapability("version", "64.0");
                    options.setCapability("screenResolution", "1920x1080");
                    options.setCapability("name", BSTestName);
                    break;
                default:
                    break;
            }
        }

        options.setCapability("browserName", "firefox");

        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS,options);

        System.out.println("Creating Selenium session, this may take couple minutes..");
        WebDriver driver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"), capabilities);
        sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
        driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver getFirefoxBrowserStackDriver() throws Exception {

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        FirefoxOptions options = new FirefoxOptions(capabilities);

        screenshotsFolder = System.getProperty("user.dir") + "/screenshots/";

        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        if (!testPlatformOSType.isEmpty() && !testPlatformOSType.equals(""))
        {
            switch (testPlatformOSType) {
                case "mac":
                    options.setCapability("os", "OS X");
                    options.setCapability("os_version", "High Sierra");
                    options.setCapability("browser", "Firefox");
                    options.setCapability("browser_version", "62.0");
                    options.setCapability("resolution", "1920x1080");
                    options.setCapability("browserstack.local", "false");
                    options.setCapability("browserstack.geckodriver", "0.22.0");
                    options.setCapability("browserstack.selenium_version", "3.14.0");
                    break;
                case "windows":
                    options.setCapability("os", "Windows");
                    options.setCapability("os_version", "10");
                    options.setCapability("browser", "Firefox");
                    options.setCapability("browser_version", "62.0");
                    options.setCapability("resolution", "1920x1080");
                    options.setCapability("browserstack.local", "false");
                    options.setCapability("browserstack.geckodriver", "0.22.0");
                    options.setCapability("browserstack.selenium_version", "3.14.0");
                    break;
                default:
                    break;
            }
        }

        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS,options);

        System.out.println("Creating Selenium session, this may take couple minutes..");
        WebDriver driver = new RemoteWebDriver(new URL("http://" + System.getenv("BROW_STACK_USER") + ":" + System.getenv("BROW_STACK_KEY") + "@hub.browserstack.com/wd/hub"), capabilities);
        sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
        //driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver getChromeBrowserStackDriver() throws Exception {

        ChromeOptions options = new ChromeOptions();

        screenshotsFolder = System.getProperty("user.dir") + "/screenshots/";

        File dir = new File(screenshotsFolder);
        dir.mkdirs();

        if (!testPlatformOSType.isEmpty() && !testPlatformOSType.equals(""))
        {
            switch (testPlatformOSType) {
                case "mac":
                    options.setCapability("os", "OS X");
                    options.setCapability("os_version", "High Sierra");
                    options.setCapability("browser", "Chrome");
                    options.setCapability("browser_version", "71.0");
                    options.setCapability("resolution", "1920x1080");
                    options.setCapability("browserstack.local", "false");
                    options.setCapability("browserstack.chrome.driver", "2.44");
                    options.setCapability("browserstack.selenium_version", "3.5.2");
                    break;
                case "windows":
                    options.setCapability("os", "Windows");
                    options.setCapability("os_version", "10");
                    options.setCapability("browser", "Chrome");
                    options.setCapability("browser_version", "71.0");
                    options.setCapability("resolution", "1920x1080");
                    options.setCapability("browserstack.local", "false");
                    options.setCapability("browserstack.chrome.driver", "2.44");
                    options.setCapability("browserstack.selenium_version", "3.5.2");
                    break;
                default:
                    break;
            }
        }

        options.setCapability("browserName", "chrome");

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        System.out.println("Creating Selenium session, this may take couple minutes..");
        WebDriver driver = new RemoteWebDriver(new URL("http://" + System.getenv("BROW_STACK_USER") + ":" + System.getenv("BROW_STACK_KEY") + "@hub.browserstack.com/wd/hub"), capabilities);
        sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
        //driver.manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
        return driver;
    }

    public void takeScreenShot(String name) throws Exception {
        byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        Files.write(Paths.get(screenshotsFolder + scCount  + "_" + name + ".png"), screenshot, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        scCount++;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    protected void checkMenuElement() throws Exception {
        boolean proceedTest = false;
        int startTime;
        int timer;

        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        startTime = (int)(System.currentTimeMillis() / 1000);
        while (!proceedTest)
        {
            timer = (((int)(System.currentTimeMillis() / 1000))-startTime);
            if(timer>30)
            {
                throw new Exception("finding menu takes too long, fail");
            }

            try
            {
                driver.findElement(By.cssSelector("ul[class='menu navigation__main-menu'] a[href='/startups/']"));
                hamburgerMenuVisible = false;
                proceedTest = true;
                log("hamburger menu not visible");
                break;
            }
            catch (Exception e)
            {}

            try
            {
                driver.findElement(By.tagName("g"));
                hamburgerMenuVisible = true;
                proceedTest = true;
                log("hamburger menu visible");
                break;
            }
            catch (Exception e)
            {}
        }
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    protected void openHamburgerMenu() throws Exception {
        // issue where menu items are not fully visible, need to re-open the menu
        try
        {
            driver.findElement(By.cssSelector("rect[y='9']")).click();
            sleep(1);
        }
        catch (Exception e)
        {
            sleep(5);
            driver.findElement(By.cssSelector("rect[y='9']")).click();
            sleep(1);
        }

        try
        {
            //takeScreenShot("hamburger-open");
            driver.findElement(By.cssSelector("svg[class='icon icon--close icon--green ']")).click();
            sleep(1);
        }
        catch (Exception e)
        {
            sleep(5);
            //takeScreenShot("hamburger-open");
            driver.findElement(By.cssSelector("svg[class='icon icon--close icon--green ']")).click();
            sleep(1);
        }

        try
        {
            //takeScreenShot("hamburger-closed");
            driver.findElement(By.cssSelector("rect[y='9']")).click();
            sleep(1);
            //takeScreenShot("hamburger-open-again");

        }
        catch (Exception e)
        {
            sleep(5);
            //takeScreenShot("hamburger-closed");
            driver.findElement(By.cssSelector("rect[y='9']")).click();
            sleep(1);
            //takeScreenShot("hamburger-open-again");
        }
    }

    protected void modifyWindowSize() throws Exception {
        int x = Integer.parseInt(browserWindowSizeX);
        int y = Integer.parseInt(browserWindowSizeY);
        log("set window size: " +x + "x"+y);
        driver.manage().window().setSize(new Dimension(x,y));
    }

    protected void setTestNAmeAndStatus(String name, String status)
    {
        BSTestName = name;
        BSTestStatus = status;
    }

    protected void uploadResultsToBrowserStack(String test, String testStatus) throws Exception
    {
        String retrunApiResponse = "";

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.browserstack.com/automate/sessions/" + sessionId + ".json").newBuilder();
        String url = urlBuilder.build().toString();
        String credential = Credentials.basic(System.getenv("BROW_STACK_USER"), System.getenv("BROW_STACK_KEY"));

        JSONObject postData = new JSONObject();
        postData.put("name",test);
        postData.put("status",testStatus);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, postData.toString());

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Authorization", credential)
                .build();

        try (Response response = client.newCall(request).execute())
        {
            retrunApiResponse = response.body().string();
            log("API_POST-" +retrunApiResponse);
        }
        catch (Exception e)
        {
            throw new Exception("api connection failed-"+e);
        }
    }
}
