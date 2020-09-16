package com.leaning.crawler.ocr;

import java.io.InputStream;
import java.util.Properties;

public class CaptchaOCRFactory {
    private static volatile CaptchaOCRFactory instance = new CaptchaOCRFactory();
    private Properties properties;

    private CaptchaOCRFactory() {
        this.readProperties();
    }

    public static CaptchaOCRFactory getInstance() {
        return instance;
    }

    public CaptchaOCR createService() {
        String cjyUserName = this.properties.getProperty("cjyUserName");
        String cjyPassWord = this.properties.getProperty("cjyPassWord");
        return new CaptchaOCRImpl(cjyUserName, cjyPassWord);
    }

    private void readProperties() {
        this.properties = new Properties();

        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("ocrConfig.properties");
            this.properties.load(in);
        } catch (Exception e) {
            throw new RuntimeException("读取配置文件发生错误", e);
        }
    }

}
