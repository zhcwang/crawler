package com.leaning.crawler.okhttp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 内存类型CookieJar，不具备持久化能力
 */
public class MemoryCookieStore extends BaseCookieStore {

    public MemoryCookieStore() {}

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            cookieMap.put(generateCookieId(cookie), cookie);
        }
    }

    /**
     * 设置手动登录的Cookie列表
     * @param cookies Cookie数据JSON数组
     */
    public void setCookies(JSONArray cookies) {
        if (cookies == null) {
            return;
        }
        for (int i=0; i<cookies.size(); i++) {
            Cookie.Builder cookieBuilder = new Cookie.Builder();
            JSONObject cookieObject = cookies.getJSONObject(i);
            String domain = cookieObject.getString("domain");
            // 去除domain前的"."
            if (StringUtils.isNotBlank(domain)) {
                if (domain.startsWith(".")) {
                    domain = domain.substring(1);
                }
            }
            Boolean hostOnly = cookieObject.getBoolean("hostOnly");
            if (hostOnly) {
                cookieBuilder.hostOnlyDomain(domain);
            } else {
                cookieBuilder.domain(domain);
            }
            String name = cookieObject.getString("name");
            cookieBuilder.name(name);
            String value = cookieObject.getString("value");
            cookieBuilder.value(value);
            String path = cookieObject.getString("path");
            cookieBuilder.path(path);
            Boolean session = cookieObject.getBoolean("session");
            if (!session) {
                double expiresAt = cookieObject.getDoubleValue("expirationDate");
                // 导出的过期日期以秒为单位
                cookieBuilder.expiresAt((long) expiresAt * 1000);
            }
            Boolean httpOnly = cookieObject.getBoolean("httpOnly");
            if (httpOnly) {
                cookieBuilder.httpOnly();
            }
            Boolean secure = cookieObject.getBoolean("secure");
            if (secure) {
                cookieBuilder.secure();
            }
            Cookie cookie = cookieBuilder.build();
            cookieMap.put(generateCookieId(cookie), cookie);
        }
    }

    /**
     * 设置手动登录的Cookie列表
     *
     * @param seleniumCookies Cookie数据JSON数组
     * @param url 测试时发现ie8返回domain为空，okhttp框架不允许，因此新增一个url参数，如果domain为空，去host
     */
    public void setCookies(List<org.openqa.selenium.Cookie> seleniumCookies, String url) {
        if (seleniumCookies == null) {
            return;
        }
        for (int i = 0; i < seleniumCookies.size(); i++) {
            Cookie.Builder cookieBuilder = new Cookie.Builder();
            org.openqa.selenium.Cookie seleniumCookie = seleniumCookies.get(i);
            String domain = seleniumCookie.getDomain();
            if (StringUtils.isNotEmpty(domain)){
                cookieBuilder.domain(domain);
            } else {
                URL u = null;
                try {
                    u = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                cookieBuilder.domain(u.getHost());
            }
            cookieBuilder.name(seleniumCookie.getName());
            cookieBuilder.value(seleniumCookie.getValue());
            cookieBuilder.path(seleniumCookie.getPath());
            Date expiry = seleniumCookie.getExpiry();
            if(expiry != null){
                cookieBuilder.expiresAt(expiry.getTime());
            }
            if (seleniumCookie.isHttpOnly()) cookieBuilder.httpOnly();
            if (seleniumCookie.isSecure()) cookieBuilder.secure();
            Cookie cookie = cookieBuilder.build();
            cookieMap.put(generateCookieId(cookie), cookie);
        }
    }

    @Override
    public String getOwner() {
        return "";
    }

    @Override
    public void saveCookie() {
    }

    @Override
    public void clearCookie() {
    }
}
