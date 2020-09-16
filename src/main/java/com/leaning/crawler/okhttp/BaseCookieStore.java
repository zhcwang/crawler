package com.leaning.crawler.okhttp;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CookieJar接口实现基类
 */
public abstract class BaseCookieStore implements CookieStore {


    /**
     * cookie id(domain + path + name + httponly + secure) -> Cookie对象
     */
    protected Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

    protected BaseCookieStore() {}

    protected BaseCookieStore(List<Cookie> cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(generateCookieId(cookie), cookie);
            }
        }
    }

    /**
    * 生成cookie id(domain + path + name + httponly + secure)
    */
    protected String generateCookieId(Cookie cookie) {
        StringBuilder cookieId = new StringBuilder();
        cookieId.append(cookie.domain())
                .append(":")
                .append(cookie.path())
                .append(":")
                .append(cookie.name())
                .append(":")
                .append(cookie.httpOnly())
                .append(":")
                .append(cookie.secure());
        return cookieId.toString();
    }

    @Override
    public Map<String, Cookie> getCookieMap() {
        return cookieMap;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookieList = new ArrayList<Cookie>();
        for (Cookie cookie : cookieMap.values()) {
            if (cookie.matches(url)) {
                cookieList.add(cookie);
            }
        }
        return cookieList;
    }
}
