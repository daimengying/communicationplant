package com.ahyx.wechat.communicationplant.utils;

import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * @Author: daimengying
 * @Date: 2018/8/8 14:22
 * @Description:微信p12证书读取
 */
@Component
public class SSLUtil {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    private static SSLUtil sSLUtil;

    @Autowired
    WeChatAccountConfig weChatAccountConfig;

    @PostConstruct
    public void init() {
        sSLUtil = this;
        sSLUtil.weChatAccountConfig = this.weChatAccountConfig;
    }

    public static SSLConnectionSocketFactory getSSLsf() throws Exception{
        // 1.证书的使用（这个证书使用可以在官方文档证书使用里面有官方demo）
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(sSLUtil.weChatAccountConfig.getKeyPath()));
        try {
            keyStore.load(instream, sSLUtil.weChatAccountConfig.getMchId().toCharArray());
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,sSLUtil.weChatAccountConfig.getMchId().toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        return sslsf;
    }
}
