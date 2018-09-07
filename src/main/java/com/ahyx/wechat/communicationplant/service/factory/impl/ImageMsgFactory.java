package com.ahyx.wechat.communicationplant.service.factory.impl;

import com.ahyx.wechat.communicationplant.service.factory.CreateMessage;
import com.ahyx.wechat.communicationplant.service.factory.MessageFactory;

/**
 * @Author: daimengying
 * @Date: 2018/9/4 09:52
 * @Description:图片消息工厂
 */
public class ImageMsgFactory implements MessageFactory {

    @Override
    public CreateMessage createMsg() {
        return new CreateImageMsg();
    }
}
