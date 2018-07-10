package com.ahyx.wechat.communicationplant.service;

import com.ahyx.wechat.communicationplant.vo.UserInfo;

/**
 * @Author: daimengying
 * @Date: 2018/7/6 17:59
 * @Description:获取用户信息
 */
public interface UserInfoService {
    UserInfo getUserInfoSub(String openID, String access_token);
}
