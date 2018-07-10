package com.ahyx.wechat.communicationplant.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Author: daimengying
 * @Date: 2018/7/4 17:28
 * @Description:证书信任管理器（用于https请求）
 */
public class MyX509TrustManager  implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
//        return new X509Certificate[0];
        return null;
    }
}
