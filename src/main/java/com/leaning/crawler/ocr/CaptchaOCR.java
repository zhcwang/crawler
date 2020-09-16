package com.leaning.crawler.ocr;

public interface CaptchaOCR {
    CaptchaResponse doCaptureOCR(byte[] pic, CaptchaType type);

}
