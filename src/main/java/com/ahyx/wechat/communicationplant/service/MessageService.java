package com.ahyx.wechat.communicationplant.service;


import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 17:30
 * @Description:自定义消息
 */
public interface MessageService {
    String processRequest(HttpServletRequest request);

    //发送模板消息
    String sendTemplate(String accessToken, JSONObject data);
}
