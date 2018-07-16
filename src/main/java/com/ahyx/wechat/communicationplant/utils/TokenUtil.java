package com.ahyx.wechat.communicationplant.utils;

import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 11:41
 * @Description:获取微信access_token的工具类
 */
@Component
public class TokenUtil {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestUtils restUtils;
    @Autowired
    WeChatAccountConfig weChatAccountConfig;

    // 第三方用户唯一凭证
    private  static AccessToken accessToken ;

    public static AccessToken getInstance(){
        return accessToken;
    }

    @Scheduled(fixedDelay = 2*3600*1000)
    public  void  getAccessToken(){
        Map<String ,String> reqMap =new HashMap();
        reqMap.put("grant_type", WeChatContant.GRANTTYPE);
        reqMap.put("appid",weChatAccountConfig.getAppId());
        reqMap.put("secret",weChatAccountConfig.getAppSecret());
        //返回格式{"access_token":"ACCESS_TOKEN","expires_in":7200}
        try {
            if(accessToken==null){
                synchronized(AccessToken.class){
                    if(accessToken==null){
                        accessToken = new AccessToken();
                        String restCallResult=restUtils.restGetForEntity(    WeChatContant.ACCESS_TOKEN_URL,reqMap);
                        if (!StringUtils.isEmpty(restCallResult)) {
                            JSONObject accessJson=JSONObject.fromObject(restCallResult);
                            accessToken.setToken(accessJson.getString("access_token"));
                            accessToken.setExpiresIn(accessJson.getInt("expires_in"));
                        }
                    }
                }
            }

        }catch (Exception e){
            _logger.error("获取access_token失败");
        }

    }

}
