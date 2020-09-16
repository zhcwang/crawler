package com.leaning.crawler.ocr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leaning.crawler.okhttp.OKHttpClientFactory;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CaptchaOCRImpl implements CaptchaOCR {
    private OKHttpClientFactory okHttpClientFactory = OKHttpClientFactory.getInstance();
    private String userName;
    private String passWord;

    public CaptchaOCRImpl(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public CaptchaResponse doCaptureOCR(byte[] image, CaptchaType captchaType) {
        OkHttpClient client = this.okHttpClientFactory.getDefaultClient();
        String url = "http://upload.chaojiying.net/Upload/Processing.php";
        FormBody formBody = this.getDoCaptchaFormBody(image, captchaType);
        Request request = this.getRequest(formBody, "http://upload.chaojiying.net/Upload/Processing.php");
        String result = this.getResponse(client, request);
        return this.getCaptchaResponse(result, true);
    }


    private Request getRequest(FormBody formBody, String url) {
        Request request = (new Builder()).url(url).header("accept", "*/*").header("connection", "Keep-Alive").header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36").header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").post(formBody).build();
        return request;
    }

    private FormBody getDoCaptchaFormBody(byte[] image, CaptchaType captchaType) {
        String imageStr = this.byteToString(image);
        String cjyType = CJYConstants.CJYTYPE.get(captchaType.toString());
        return (new okhttp3.FormBody.Builder()).add("user", this.userName).add("pass", this.passWord).add("codetype", cjyType).add("file_base64", imageStr).build();
    }

    private FormBody getReportFormBody(String imageId) {
        return (new okhttp3.FormBody.Builder()).add("user", this.userName).add("pass", this.passWord).add("softid", "897622").add("id", imageId).build();
    }

    private String getResponse(OkHttpClient client, Request request) {
        Response response = null;
        String result = null;

        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
        }

        return result;
    }

    private CaptchaResponse getCaptchaResponse(String result, boolean isDoCaptcha) {
        CaptchaResponse response = new CaptchaResponse();
        if (result == null) {
            response.setErrorReason("请求远程识别api出错");
        } else {
            JSONObject resultObject = JSON.parseObject(result);
            int flag = resultObject.getIntValue("err_no");
            String errorStr;
            if (flag != 0) {
                errorStr = resultObject.getString("err_str");

                try {
                    errorStr = URLDecoder.decode(errorStr, "utf-8");
                    response.setErrorReason(errorStr);
                } catch (UnsupportedEncodingException var8) {
                    response.setErrorReason("url解码错误");
                }
            } else {
                if (isDoCaptcha) {
                    errorStr = resultObject.getString("pic_id");
                    String captchaStr = resultObject.getString("pic_str");
                    response.setCaptchaId(errorStr);
                    response.setCaptchaStr(captchaStr);
                }

                response.setSuccess(true);
            }
        }

        return response;
    }

    private String byteToString(byte[] image) {
        String encodedText = Base64.encodeBase64String(image);
        return encodedText;
    }

    public static void main(String[] args) throws Exception{

        System.out.println(new String(Base64.decodeBase64("enljMTIzY2p5"), "UTF-8"));
        System.out.println( new String(Base64.decodeBase64("Y2hhb2ppeWluZw=="), "UTF-8"));

    }
}

