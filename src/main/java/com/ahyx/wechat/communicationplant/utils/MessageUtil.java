package com.ahyx.wechat.communicationplant.utils;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.vo.Article;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: daimengying
 * @Date: 2018/7/4 16:11
 * @Description:消息工具类
 */
@Component
public class MessageUtil {
    /**
     * 回复文本消息
     * @param requestMap
     * @param content
     * @return
     */
    public static String sendTextMsg(Map<String,String> requestMap, String content){

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("ToUserName", requestMap.get(WeChatContant.FromUserName));
        map.put("FromUserName",  requestMap.get(WeChatContant.ToUserName));
        map.put("MsgType", WeChatContant.RESP_MESSAGE_TYPE_TEXT);
        map.put("CreateTime", new Date().getTime());
        map.put("Content", content);
        return  WechatUtil.mapToXML(map);
    }
    /**
     * 回复图文消息
     * @param requestMap
     * @param items
     * @return
     */
    public static String sendArticleMsg(Map<String,String> requestMap,List<Article> items){
        if(items == null || items.size()<1){
            return "";
        }
        Map<String,Object> map=new HashMap<>();
        map.put("ToUserName", requestMap.get(WeChatContant.FromUserName));
        map.put("FromUserName", requestMap.get(WeChatContant.ToUserName));
        map.put("MsgType", WeChatContant.RESP_MESSAGE_TYPE_NEWS);
        map.put("CreateTime", new Date().getTime());
        List<Map<String,Object>> articles=new ArrayList<>();
        for(Article itembean : items){
            Map<String,Object> item=new HashMap<>();
            Map<String,Object> itemContent=new HashMap<>();
            itemContent.put("Title", itembean.getTitle());
            itemContent.put("Description", itembean.getDescription());
            itemContent.put("PicUrl", itembean.getPicUrl());
            itemContent.put("Url", itembean.getUrl());
            item.put("item",itemContent);
            articles.add(item);
        }
        map.put("Articles", articles);
        map.put("ArticleCount", articles.size());
        return WechatUtil.mapToXML(map);
    }

}
