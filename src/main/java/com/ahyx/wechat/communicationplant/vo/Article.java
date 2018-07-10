package com.ahyx.wechat.communicationplant.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 17:25
 * @Description:
 */
public class Article {
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String picUrl;

    @Getter
    @Setter
    private String url;
}
