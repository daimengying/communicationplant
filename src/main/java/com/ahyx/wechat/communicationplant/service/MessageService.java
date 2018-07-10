package com.ahyx.wechat.communicationplant.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 17:30
 * @Description:自定义消息
 */
public interface MessageService {
    String processRequest(HttpServletRequest request);
}
