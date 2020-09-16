package com.leaning.crawler.okhttp;

import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class OKHttpClientFactory {

    private static OKHttpClientFactory instance = new OKHttpClientFactory();

    private OkHttpClient client;

    private OKHttpClientFactory() {
        ConnectionPool pool = new ConnectionPool(100, 5, TimeUnit.MINUTES);

        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        Builder builder = new Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(pool)
                .cookieJar(CookieStoreFactory.createInMemoryCookieStore())
                .proxy(Proxy.NO_PROXY);

        try {
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        client = builder.build();
    }

    public static OKHttpClientFactory getInstance() {
        return instance;
    }

    public OkHttpClient getDefaultClient() {
        return client;
    }

    /**
     * 获取携带等候cookie的httpclient
     * @param cookieJar
     * @return
     */
    public OkHttpClient getLoginClient(CookieJar cookieJar) {
         Builder builder = client.newBuilder();
         if(cookieJar != null){
             builder.cookieJar(cookieJar);
         }
         return builder.build();
    }

    public OkHttpClient getLoginClientWithProxy(CookieJar cookieJar, String host, int port, String user, String password) {
    	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        Authenticator proxyAuthenticator = null;
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password)) {
            final String PROXY_USERNAME = user;
            final String PASSWORD = password;
            proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    if (response.request().header("Proxy-Authorization") != null) {
                        // 代理认证已失败，不重复尝试
                        return null;
                    }
                    String credential = Credentials.basic(PROXY_USERNAME, PASSWORD);
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };
        }
        Builder builder;
        if(cookieJar != null){
            builder = client.newBuilder().cookieJar(cookieJar).proxy(proxy);
        } else {
            builder = client.newBuilder().proxy(proxy);
        }
        return proxyAuthenticator == null ? builder.build() : builder.proxyAuthenticator(proxyAuthenticator).build();
    }

    public OkHttpClient getProxyClient(String host, int port, String user, String password) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        Authenticator proxyAuthenticator = null;
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password)) {
            final String PROXY_USERNAME = user;
            final String PASSWORD = password;
            proxyAuthenticator = (route, response) -> {
                if (response.request().header("Proxy-Authorization") != null) {
                    // 代理认证已失败，不重复尝试
                    return null;
                }
                String credential = Credentials.basic(PROXY_USERNAME, PASSWORD);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            };
        }
        Builder builder = client.newBuilder().proxy(proxy);
        return proxyAuthenticator == null ? builder.build() : builder.proxyAuthenticator(proxyAuthenticator).build();
    }
}
