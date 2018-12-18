package com.sample.tdsample;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSeleniumTest{

    public static final int SHORT_SLEEP = 1;
    public static final int MEDIUM_SLEEP = 3;
    public static final int LONG_SLEEP = 10;

    protected static WebDriver driver;
    protected static int defaultWaitTime = 60;
    public static String screenshotsFolder = "";
    protected static int scCount = 0;

    protected static String testPlatform = System.getenv("PLATFORM_TO_RUN");

    protected static String testPlatformOSType = System.getenv("PLATFORM_OS");

    protected static String browserToRunTest = System.getenv("BROWSER_TO_TEST");

    protected static String browserWindowSizeX = System.getenv("BROWSER_SIZE_X");

    protected static String browserWindowSizeY = System.getenv("BROWSER_SIZE_Y");

    public static void log(String message) {
        System.out.println(""+message);
    }

    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }
}
