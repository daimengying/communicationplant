package com.ahyx.wechat.communicationplant.service.factory.impl;

import com.ahyx.wechat.communicationplant.service.factory.CreateMessage;
import com.ahyx.wechat.communicationplant.service.factory.MessageFactory;


/**
 * @Author: daimengying
 * @Date: 2018/7/3 18:35
 * @Description:图文消息工厂
 */

public class ArticleMsgFactory implements MessageFactory {
    @Override
    public CreateMessage createMsg() {
        return new CreateArticleMsg();
    }
}
