package com.ahyx.wechat.communicationplant.service.impl;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.UserInfoService;
import com.ahyx.wechat.communicationplant.utils.HttpsClient;
import com.ahyx.wechat.communicationplant.vo.UserInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/6 18:00
 * @Description:
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 使用  openID、access_token直接获取用户的信息
     * openID就是用户FromUserName
     * @param openID
     * @param access_token
     * @return
     */
    @Override
    public UserInfo getUserInfoSub(String openID, String access_token) {
        String requestUrl= WeChatContant.GET_USERINFO_URL.replace("ACCESS_TOKEN", access_token).replace("OPENID", openID);
        UserInfo user=new UserInfo();
        try {
            JSONObject userInfoObj= HttpsClient.httpRequest(requestUrl, "GET", null);
            user.setOpenid(userInfoObj.getString("openid"));
            user.setNickname(userInfoObj.getString("nickname"));
            user.setSex(userInfoObj.getInt("sex"));
            user.setProvince(userInfoObj.getString("province"));
            user.setCity(userInfoObj.getString("city"));
            user.setCountry(userInfoObj.getString("country"));
            user.setHeadImgUrl(userInfoObj.getString("headimgurl"));
            user.setUnionId(userInfoObj.get("unionid")==null?"":userInfoObj.getString("unionid"));
            user.setSubscribeTime(userInfoObj.getLong("subscribe_time"));
            }catch (Exception e) {
            _logger.error("获取用户信息异常："+e.getMessage());
        }
        return user;
    }
}
