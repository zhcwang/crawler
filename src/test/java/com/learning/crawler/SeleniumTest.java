package com.learning.crawler;

import com.leaning.crawler.ocr.CaptchaOCR;
import com.leaning.crawler.ocr.CaptchaOCRFactory;
import com.leaning.crawler.ocr.CaptchaResponse;
import com.leaning.crawler.ocr.CaptchaType;
import com.leaning.crawler.selenium.SeleniumUtils;
import com.leaning.crawler.selenium.WebDriverFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SeleniumTest {

    @Test
    public void testVisitBaidu() throws Exception {
        WebDriver chromeDriver = WebDriverFactory.getInstance().newChromeDriver(false);
        try {
            chromeDriver.get("https://www.baidu.com");
            By.ByCssSelector searchBoxIdentifier = new By.ByCssSelector("#kw");
            By.ByCssSelector searchButtonIdentifier = new By.ByCssSelector("#su");
            SeleniumUtils.waitForLoad(chromeDriver, searchBoxIdentifier);
            WebElement searchBox = chromeDriver.findElement(searchBoxIdentifier);
            searchBox.sendKeys("大连");
            Thread.sleep(2000);
            WebElement searchButton = chromeDriver.findElement(searchButtonIdentifier);
            searchButton.click();
            Thread.sleep(5000);
        } finally {
            chromeDriver.close();
            chromeDriver.quit();
        }
    }

    @Test
    public void testGetCaptcha() throws Exception{
        WebDriver chromeDriver = WebDriverFactory.getInstance().newChromeDriver(false);
        try {
            getCaptcha(chromeDriver);
        } finally {
            chromeDriver.close();
            chromeDriver.quit();
        }
    }

    private String getCaptcha(WebDriver chromeDriver) throws Exception{
        chromeDriver.get("http://www.chaojiying.com/");
        Thread.sleep(1000);
        By.ByCssSelector loginMenuIdentifier = new By.ByCssSelector("#login-register > a");
        chromeDriver.findElement(loginMenuIdentifier).click();
        Thread.sleep(2000);
        By.ByCssSelector captchaImageIdentifier = new By.ByCssSelector("#userone > section > form > div.form-group.common-login-group-other > div > img");
        WebElement captchaImageElem = chromeDriver.findElement(captchaImageIdentifier);
        Point point = captchaImageElem.getLocation();
        Dimension size = captchaImageElem.getSize();
        byte[] screenshotBytes = ((ChromeDriver)chromeDriver).getScreenshotAs(OutputType.BYTES);
        BufferedImage screenshot = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(screenshotBytes)));
        BufferedImage captchaImage = screenshot.getSubimage(point.getX(), point.getY(),
                size.getWidth(), size.getHeight());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "png", baos);
        byte[] captchaBytes = baos.toByteArray();
        String filePath = "src/test/pictures/captcha/" + UUID.randomUUID() + ".png";
        FileUtils.writeByteArrayToFile(new File(filePath),captchaBytes);
        return filePath;
    }

    @Test
    public void testLogin() throws Exception{
        WebDriver chromeDriver = WebDriverFactory.getInstance().newChromeDriver(false);
        try {
            String captchaPath = getCaptcha(chromeDriver);
            byte[] captchaImg = FileUtils.readFileToByteArray(new File(captchaPath));
            CaptchaOCR service = CaptchaOCRFactory.getInstance().createService();
            CaptchaResponse captchaResponse = service.doCaptureOCR(captchaImg, CaptchaType.CHARACTER_4);
            String captchaValue = captchaResponse.getCaptchaStr();

            By.ByCssSelector userInputIdentifier = new By.ByCssSelector("#user");
            By.ByCssSelector pwdInputIdentifier = new By.ByCssSelector("#pass");
            By.ByCssSelector captchaInputIdentifier = new By.ByCssSelector("#auth");
            By.ByCssSelector loginButtonIdentifier = new By.ByCssSelector("#userone > section > form > div:nth-child(6) > button");
            WebElement userName = chromeDriver.findElement(userInputIdentifier);
            WebElement pwd = chromeDriver.findElement(pwdInputIdentifier);
            WebElement captcha = chromeDriver.findElement(captchaInputIdentifier);
            WebElement loginButton = chromeDriver.findElement(loginButtonIdentifier);
            userName.sendKeys("felixwang");
            pwd.sendKeys("aaa123+-*/");
            captcha.sendKeys(captchaValue);
            Thread.sleep(2000);
            loginButton.click();
            Thread.sleep(2000);
            printAuth((ChromeDriver) chromeDriver);
        } finally {
            chromeDriver.close();
            chromeDriver.quit();
        }
    }

    private void printAuth(ChromeDriver webDriver){
        Set<Cookie> cookies = webDriver.manage().getCookies();
        LocalStorage localStorage = webDriver.getLocalStorage();
        SessionStorage sessionStorage = webDriver.getSessionStorage();
        System.out.println(cookies);
        Map<String, String> localStorages = localStorage.keySet().stream().collect(Collectors.toMap(String::toString, key -> Optional.ofNullable(localStorage.getItem(key)).orElse("")));
        Map<String, String> sessionStorages = sessionStorage.keySet().stream().collect(Collectors.toMap(String::toString, key -> Optional.ofNullable(sessionStorage.getItem(key)).orElse("")));
        System.out.println("localStorage");
        System.out.println(localStorages);
        System.out.println("sessionStorages");
        System.out.println(sessionStorages);
    }
}
