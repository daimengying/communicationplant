package com.ahyx.wechat.communicationplant.service.factory;

import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 18:13
 * @Description:生成消息接口
 */
public interface CreateMessage {
    String sendMsg(Map<String,String> requestMap);
}
