package com.ahyx.wechat.communicationplant.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.TreeMap;

/**
 * @Author: daimengying
 * @Date: 2018/7/12 16:08
 * @Description:模板消息
 */
public class TemplateMessage {
    @Getter
    @Setter
    private String touser; //接收者openid

    @Getter
    @Setter
    private String template_id; //模板ID

    @Getter
    @Setter
    private String url; //模板跳转链接
    // "miniprogram":{ 未加入
    // "appid":"xiaochengxuappid12345",
    // "pagepath":"index?foo=bar"
    // },

    @Getter
    @Setter
    private TreeMap<String, TreeMap<String, String>> data; //data数据

    /**
     * 参数
     * @param value
     * @param color 可不填
     * @return
     */
    public static TreeMap<String, String> item(String value, String color) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("value", value);
        params.put("color", color);
        return params;
    }

}
