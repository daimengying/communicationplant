package com.ahyx.wechat.communicationplant.service;

import com.ahyx.wechat.communicationplant.domain.User;

import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/6 17:59
 * @Description:获取用户信息
 */
public interface UserInfoService {
    User getUserInfoSub(String openID, String access_token);

    Map<String,String> getOpenId(String code, String state);

    Integer addOrUpdateUser(User user);

    User  getUserByOpenid(String openid);

}
