package com.leaning.crawler.ocr;

public class CaptchaResponse {
    private boolean isSuccess;
    private String captchaStr;
    private String captchaId;
    private String errorReason;

    public CaptchaResponse() {
    }

    public String getErrorReason() {
        return this.errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getCaptchaStr() {
        return this.captchaStr;
    }

    public void setCaptchaStr(String captchaStr) {
        this.captchaStr = captchaStr;
    }

    public String getCaptchaId() {
        return this.captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String toString() {
        StringBuilder ss = new StringBuilder();
        ss.append("isSuccess: ").append(this.isSuccess).append("\n").append("captchaStr: ").append(this.captchaStr).append("\n").append("captchaId: ").append(this.captchaId).append("\n").append("errorReason: ").append(this.errorReason).append("\n");
        return ss.toString();
    }
}
