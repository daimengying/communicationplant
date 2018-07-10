package com.ahyx.wechat.communicationplant.service.factory.impl;

import com.ahyx.wechat.communicationplant.service.factory.CreateMessage;
import com.ahyx.wechat.communicationplant.service.factory.MessageFactory;

/**
 * @Author: daimengying
 * @Date: 2018/7/5 17:50
 * @Description:事件推送工厂
 */
public class EventMsgFactory implements MessageFactory {
    @Override
    public CreateMessage createMsg() {
        return new CreateEventMsg();
    }
}
