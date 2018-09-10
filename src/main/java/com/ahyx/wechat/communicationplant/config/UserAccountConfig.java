package com.ahyx.wechat.communicationplant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: daimengying
 * @Date: 2018/9/10 16:16
 * @Description:用户账户配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "user")
public class UserAccountConfig {

    private String account;

    private String apikey;

}
