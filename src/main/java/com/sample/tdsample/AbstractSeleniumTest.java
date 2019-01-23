package com.sample.tdsample;
import org.openqa.selenium.WebDriver;

public abstract class AbstractSeleniumTest{

    public static final int SHORT_SLEEP = 1;
    public static final int MEDIUM_SLEEP = 3;
    public static final int LONG_SLEEP = 10;

    //protected static Logger logger =  LoggerFactory.getLogger(AbstractSeleniumTest.class);
    protected static WebDriver driver;
    protected static int defaultWaitTime = 60;
    public static String screenshotsFolder = "";
    protected static int scCount = 0;

    protected static String testPlatform = System.getenv("PLATFORM_TO_RUN");

    protected static String testPlatformOSType = System.getenv("PLATFORM_OS");

    protected static String browserToRunTest = System.getenv("BROWSER_TO_TEST");

    protected static String browserWindowSizeX = System.getenv("BROWSER_SIZE_X");

    protected static String browserWindowSizeY = System.getenv("BROWSER_SIZE_Y");


    //public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

    public static void log(String message) {
        System.out.println(""+message);
    }

    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }
}
