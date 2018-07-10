package com.ahyx.wechat.communicationplant.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: daimengying
 * @Date: 2018/7/4 18:27
 * @Description:微信通用接口凭证
 */
public class AccessToken {
    // 获取到的凭证
    @Getter
    @Setter
    private String token;


    // 凭证有效时间，单位：秒
    @Getter
    @Setter
    private int expiresIn;


}
