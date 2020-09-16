package com.leaning.crawler.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtils {

    /**
     * 等待元素加载，10s超时
     *
     * @param driver
     * @param by
     */
    public static void waitForLoad(final WebDriver driver, final By by) {
        try {
            new WebDriverWait(driver, 10).until((ExpectedCondition<Boolean>) d -> {
                WebElement element = driver.findElement(by);
                return element != null;
            });
        } catch (Exception e) {
            throw new RuntimeException("获取目标元素超时：" + by.toString());
        }
    }

}
