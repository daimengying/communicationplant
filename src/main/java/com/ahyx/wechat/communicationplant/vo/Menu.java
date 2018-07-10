package com.ahyx.wechat.communicationplant.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @Author: daimengying
 * @Date: 2018/7/4 17:42
 * @Description:
 */
public class Menu {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String accountId;

    @Getter
    @Setter
    private String parentId;

    @Getter
    @Setter
    private String parentName;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String key;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private Integer sort;

    @Getter
    @Setter
    private String createdBy;

    @Getter
    @Setter
    private Timestamp createdTime;
}
