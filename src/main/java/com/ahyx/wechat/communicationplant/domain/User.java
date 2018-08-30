package com.ahyx.wechat.communicationplant.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: daimengying
 * @Date: 2018/8/30 14:24
 * @Description:
 */
@Table(name="g_users")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -3340951791185432524L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    private String openid;

    private String city;

    private String country;

    private Integer groupid;

    private String headImgUrl;

    private String language;

    private String nickname;

    private String privilege;

    private String province;

    private Integer qrScene;

    private String qrSceneStr;

    private String remark;

    private Integer sex;

    private Boolean subscribe;

    private String subscribeScene;

    private Long subscribeTime;

    private String tagidList;

    private String unionid;


}
