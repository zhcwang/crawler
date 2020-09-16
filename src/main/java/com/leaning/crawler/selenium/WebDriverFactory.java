package com.leaning.crawler.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;

public class WebDriverFactory {

    private WebDriverFactory() {

    }

    private static WebDriverFactory INSTANCE = new WebDriverFactory();

    public static WebDriverFactory getInstance() {
        return INSTANCE;
    }

    /**
     * windows环境chrome驱动默认位置
     */
    private static final String WINDOWS_CHROME_DRIVER_PATH = "src/main/resources/selenium/chrome-driver.exe";
    /**
     * windows环境chrome浏览器默认位置
     */
    private static final String WINDOWS_CHROME_EXE_PATH = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";

    /**
     * 初始化Chrome浏览器启动参数
     */
    public ChromeOptions initChromeOpts(boolean headless) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(WINDOWS_CHROME_EXE_PATH);
        if (headless) {
            chromeOptions.setHeadless(true);
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--proxy-server='direct://'");
            chromeOptions.addArguments("--proxy-bypass-list=*");
            chromeOptions.addArguments("--disable-gpu");
        }
        chromeOptions.addArguments("--allow-running-insecure-content");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        int[] screenSize = getScreenSize();
        chromeOptions.addArguments("--window-size=" + screenSize[0] + "," + screenSize[1]);
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-infobars");
        return chromeOptions;
    }

    /**
     * 获取当前可视区域大小, 如果没有屏幕，则优先查看JVM参数，如果没有JVM参数默认1960, 1080
     *
     * @return int[width, height]
     */
    private static int[] getScreenSize() {
        int width, height;
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            width = screenSize.width;
            height = screenSize.height;
        } catch (HeadlessException e) {
            width = 1960;
            height = 1080;
        }
        return new int[]{width, height};
    }


    public WebDriver newChromeDriver(boolean headless) {
        System.setProperty("webdriver.chrome.driver", WINDOWS_CHROME_DRIVER_PATH);
        return new ChromeDriver(initChromeOpts(headless));
    }


}
