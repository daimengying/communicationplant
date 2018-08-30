package com.ahyx.wechat.communicationplant.service.impl;

import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.dao.UserMapper;
import com.ahyx.wechat.communicationplant.domain.User;
import com.ahyx.wechat.communicationplant.service.UserInfoService;
import com.ahyx.wechat.communicationplant.utils.HttpsClient;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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

    @Autowired
    WeChatAccountConfig weChatAccountConfig;
    @Resource
    UserMapper userMapper;


    /**
     * 使用  openID、access_token直接获取用户的信息
     * openID就是用户FromUserName
     * @param openID
     * @param access_token
     * @return
     */
    @Override
    public User getUserInfoSub(String openID, String access_token) {
        String requestUrl= WeChatContant.GET_USERINFO_URL.replace("ACCESS_TOKEN", access_token).replace("OPENID", openID);
        User user=new User();
        try {
            JSONObject userInfoObj= HttpsClient.httpRequest(requestUrl, "GET", null);
            user.setSubscribe(userInfoObj.getInt("subscribe")==1?true:false);
            user.setOpenid(userInfoObj.getString("openid"));
            user.setNickname(userInfoObj.getString("nickname"));
            user.setSex(userInfoObj.getInt("sex"));
            user.setProvince(userInfoObj.getString("province"));
            user.setCity(userInfoObj.getString("city"));
            user.setCountry(userInfoObj.getString("country"));
            user.setHeadImgUrl(userInfoObj.getString("headimgurl"));
            user.setUnionid(userInfoObj.get("unionid")==null?"":userInfoObj.getString("unionid"));
            user.setSubscribeTime(userInfoObj.getLong("subscribe_time"));
        }catch (Exception e) {
            _logger.error("获取用户信息异常："+e.getMessage());
        }
        return user;
    }

    /**
     * 根据openid获取用户
     * @param openid
     * @return
     */
    @Override
    public User getUserByOpenid(String openid) {
        Example example = new Example(User.class);
        Example.Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("openid", openid);
        User user= userMapper.selectOneByExample(example);
        return user;
    }


    /**
     * 新增关注,取消/重新添加关注
     * @param user
     * @return
     */
    @Async
    @Override
    public Integer addOrUpdateUser(User user) {
        if(getUserByOpenid(user.getOpenid())==null){
            return userMapper.insert(user);
        }else {
            Example example = new Example(User.class);
            Example.Criteria createCriteria = example.createCriteria();
            createCriteria.andEqualTo("openid", user.getOpenid());
            return userMapper.updateByExampleSelective(user,example);
        }
    }


    /**
     * 获取用户openId
     * @param code
     * @param state
     * @return
     */
    @Override
    public Map<String,String> getOpenId(String code, String state) {
        Map <String,String> resultMap=new HashMap<>();
        String openId="";
        String webAccessTokenUrl= WeChatContant.GET_WEB_ACCESS_TOEN.replace("APPID",weChatAccountConfig.getAppId())
                .replace("SECRET",weChatAccountConfig.getAppSecret()).replace("CODE",code);
        //通过code获取网页授权access_token
        if(!StringUtils.isEmpty(code)){
            JSONObject  result=HttpsClient.httpRequest(webAccessTokenUrl, "GET", null);
            if(result!=null && !result.has("errcode")){
                String webAccessToken=result.getString("access_token");
                openId=result.getString("openid");
                resultMap.put("openId",openId);
                resultMap.put("webAccessToken",webAccessToken);
            }
        }
        return resultMap;
    }
}
