package com.leaning.crawler.okhttp;

import okhttp3.Cookie;
import okhttp3.CookieJar;

import java.util.Map;

/**
 * 封装的CookieJar接口
 */
public interface CookieStore extends CookieJar {

    /**
     * 获取Cookie数据
     * @return Cookie数据Map(cookie id -> Cookie对象)
     */
    Map<String, Cookie> getCookieMap();

    /**
     * 获取所有者
     */
    String getOwner();

    /**
     * 保存Cookie
     */
    void saveCookie();

    /**
     * 删除Cookie
     */
    void clearCookie();

}
