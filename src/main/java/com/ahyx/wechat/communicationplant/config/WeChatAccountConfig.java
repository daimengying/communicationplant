package com.ahyx.wechat.communicationplant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: daimengying
 * @Date: 2018/7/10 16:08
 * @Description:微信公众号账号设置
 */

/*微信公众号账号设置*/
@Data//使用了lombok依赖，相当于在代码中getter和setter的作用
@Component
//spring boot1.5以上版本@ConfigurationProperties取消location注解,使用@PropertySource来指定自定义的资源目录
@ConfigurationProperties(prefix = "wechat")
public class WeChatAccountConfig {
    private String appId;

    private String appSecret;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String mchKey;

    /**
     * 商户证书路径
     */
    private String keyPath;

    /**
     * 异步通知路径
     */
    private String notifyUrl;

    /**
     * 支付完成后接收流量平台提供的返回结果
     */
    private String callbackUrl;
}
