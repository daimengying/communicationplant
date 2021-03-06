package com.ahyx.wechat.communicationplant.vo;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

import java.util.List;

/**
 * @Author: daimengying
 * @Date: 2018/7/6 18:09
 * @Description:用户信息
 */
public class UserInfo {
    @Getter
    @Setter
    private String openid;//用户的标识，对当前公众号唯一

    @Getter
    @Setter
    private Integer subscribe;

    @Getter
    @Setter
    private String nickname;

    @Getter
    @Setter
    private Integer sex;//用户性别，1 男性，2 女性，0 未知

    @Getter
    @Setter
    private String language;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String province;

    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private String headImgUrl;

    @Getter
    @Setter
    private Long subscribeTime;

    @Getter
    @Setter
    private JSONArray privilege;

    @Getter
    @Setter
    private String unionid;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。

    @Getter
    @Setter
    private String remark;

    @Getter
    @Setter
    private Integer groupid;//用户所在的分组ID（兼容旧的用户分组接口）

    @Getter
    @Setter
    private List<Integer>tagidList;

    @Getter
    @Setter
    private String subscribeScene;

    @Getter
    @Setter
    private Integer qrScene;//二维码扫码场景（自定义）

    @Getter
    @Setter
    private String qrSceneStr;//二维码扫码场景描述（自定义）

}
