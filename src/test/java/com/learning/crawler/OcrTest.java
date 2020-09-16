package com.learning.crawler;

import com.leaning.crawler.ocr.CaptchaOCR;
import com.leaning.crawler.ocr.CaptchaOCRFactory;
import com.leaning.crawler.ocr.CaptchaResponse;
import com.leaning.crawler.ocr.CaptchaType;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class OcrTest {
    @Test
    public void doOcr(){
        byte[] captcha = readFile("src/test/resources/pics/mrtd.png");
        CaptchaOCR service = CaptchaOCRFactory.getInstance().createService();
        CaptchaResponse captchaResponse = service.doCaptureOCR(captcha, CaptchaType.CHARACTER_4);
        System.out.println(captchaResponse.getCaptchaStr());
    }

    private byte[] readFile(String path){
        try {
            return FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
