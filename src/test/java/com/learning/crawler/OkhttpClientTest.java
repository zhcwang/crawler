package com.learning.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leaning.crawler.okhttp.OKHttpClientFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;


public class OkhttpClientTest {

    @Test
    public void testSendRequest() throws Exception {
        OkHttpClient client = OKHttpClientFactory.getInstance().getDefaultClient();
        Request request = new Request.Builder()
                .url("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%A4%A7%E8%BF%9E&fenlei=256&rsv_pq=be20cfe6000986a1&rsv_t=368cebZjjQOkz%2FRoh5A83RaVpAREIUhw7jMEtPIoHwAP2DCDpS3NSX36FNI&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_sug3=14&rsv_sug1=16&rsv_sug7=101&rsv_sug2=0&rsv_btype=i&prefixsug=%25E5%25A4%25A7%25E8%25BF%259E&rsp=5&inputT=3165&rsv_sug4=3885")
                .get()
                .header("Accept", "*/*")
                .header("Accept-Encoding", "deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Host", "www.baidu.com")
                .header("Referer", "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%A4%A7%E8%BF%9E&fenlei=256&oq=%25E5%25A4%25A7%25E8%25BF%259E&rsv_pq=82b5064e00097124&rsv_t=529c1eyDsUPshEn%2BXpa5C7xzd2Yz1tkYfYkfbXcLQDSLli3V2%2FRv3NICxdM&rqlang=cn&rsv_enter=0&rsv_dl=tb&rsv_btype=t&inputT=4216&rsv_sug3=15&rsv_sug1=17&rsv_sug7=100&rsv_sug4=4325&rsv_sug=1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("X-Requested-With", "XMLHttpRequest")
                .build();
        Response resp = client.newCall(request).execute();
        String response = resp.body().string();
        System.out.println(response);
    }

    private void parseHtml(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(".c-tools");
        for (Element element : elements) {
            String info = element.attr("data-tools");
            JSONObject jsonObject = JSON.parseObject(info);
            if (jsonObject != null) {
                System.out.println(String.format("title: %s, url: %s", jsonObject.getString("title"), jsonObject.getString("url")));
            }
        }
    }

}
