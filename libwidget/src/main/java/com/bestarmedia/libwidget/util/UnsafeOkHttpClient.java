//package com.bestarmedia.libwidget.util;
//
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.concurrent.TimeUnit;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import okhttp3.OkHttpClient;
//
//public class UnsafeOkHttpClient {
//
//    private UnsafeOkHttpClient() {
//    }
//
//    public static OkHttpClient getUnsafeOkHttpClient() {
//        try {
//            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new java.security.cert.X509Certificate[]{};
//                }
//            }};
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//            builder.hostnameVerifier((hostname, session) -> true);
//            builder.connectTimeout(20, TimeUnit.SECONDS);
//            builder.readTimeout(20, TimeUnit.SECONDS);
//            return builder.build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
