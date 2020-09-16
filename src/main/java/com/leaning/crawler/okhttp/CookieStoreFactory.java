package com.leaning.crawler.okhttp;

/**
 * CookieJar工厂类
 */
public class CookieStoreFactory {

    /**
     * 创建内存类型CookieJar
     */
    public static CookieStore createInMemoryCookieStore() {
        return new MemoryCookieStore();
    }

}
