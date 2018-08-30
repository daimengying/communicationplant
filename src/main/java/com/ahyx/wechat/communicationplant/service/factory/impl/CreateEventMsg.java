package com.ahyx.wechat.communicationplant.service.factory.impl;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.factory.CreateMessage;
import com.ahyx.wechat.communicationplant.utils.MessageUtil;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/5 17:51
 * @Description:事件消息
 */
public class CreateEventMsg implements CreateMessage {

    @Override
    public String sendMsg(Map<String, String> requestMap) {
        //点击菜单id
        String eventKey =requestMap.get("EventKey");
        // xml格式的消息数据
        String respXml = "";
        String respContent="";
        if(!StringUtils.isEmpty(eventKey)){
            switch (eventKey){
                case "1":{
                    respContent = "<a href=\"http://www.kugou.com/song/1nbc2e4.html?frombaidu?frombaidu#hash=0C327228DCD07AB1199B4A556CF9A510&album_id=0\">天蓝蓝.mp3</a>";
                    break;
                }
                case "22" :{
                    respContent=requestMap.get("ScanResult");
                    break;
                }
            }
        }
        //关注和取消关注事件
        String event=requestMap.get("Event");
        if(event.equals(WeChatContant.EVENT_SUBSCRIBE)){
            respContent="欢迎进入充值测试平台！";
        }
        respXml=MessageUtil.sendTextMsg(requestMap,respContent);
        return  respXml;
    }
}
