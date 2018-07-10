package com.ahyx.wechat.communicationplant.service.factory.impl;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.factory.CreateMessage;
import com.ahyx.wechat.communicationplant.utils.MessageUtil;
import com.ahyx.wechat.communicationplant.utils.WechatUtil;
import com.ahyx.wechat.communicationplant.vo.Article;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 18:25
 * @Description:图文消息传送
 */
//@Component
public class CreateArticleMsg implements CreateMessage {
    @Override
    public String sendMsg(Map<String, String> requestMap) {
        String mes = requestMap.get(WeChatContant.Content).toString();
        // xml格式的消息数据
        String respXml = null;
        if (WechatUtil.isQqFace(mes)){
            respXml =MessageUtil.sendTextMsg(requestMap,mes);
        }else if (mes != null && mes.length() < 2) {
            //小于2个字符，展示如下
            List<Article> items = new ArrayList<>();
            Article item = new Article();
            item.setTitle("照片墙");
            item.setDescription("阿狸照片墙");
            item.setPicUrl("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E9%98%BF%E7%8B%B8%E7%85%A7%E7%89%87&hs=2&pn=1&spn=0&di=51515880680&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=3230110708%2C1421438514&os=2178416314%2C3365948195&simid=0%2C0&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=%E9%98%BF%E7%8B%B8%E7%85%A7%E7%89%87&objurl=http%3A%2F%2Fp3.gexing.com%2FG1%2FM00%2F3E%2F97%2FrBACFFMoB3TxzrxrAAA7KmAqHW4129_600x.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B2jxtg2_z%26e3Bv54AzdH3Ffiwtp7AzdH3Fdc89ddd_z%26e3Bip4s&gsm=0&islist=&querylist=");
            item.setUrl("https://www.gexing.com/shaitu/2514222.html");
            items.add(item);

            item = new Article();
            item.setTitle("百度");
            item.setDescription("百度一下");
            item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
            item.setUrl("http://www.baidu.com");
            items.add(item);
            respXml = MessageUtil.sendArticleMsg(requestMap, items);
        } else if ("我的信息".equals(mes)) {
//            Map<String, String> userInfo = getUserInfo(requestMap.get(WeChatContant.FromUserName));
//            System.out.println(userInfo.toString());
//            String nickname = userInfo.get("nickname");
//            String city = userInfo.get("city");
//            String province = userInfo.get("province");
//            String country = userInfo.get("country");
//            String headimgurl = userInfo.get("headimgurl");
//            List<Article> items = new ArrayList<>();
//            Article item = new Article();
//            item.setTitle("你的信息");
//            item.setDescription("昵称:" + nickname + "  地址:" + country + " " + province + " " + city);
//            item.setPicUrl(headimgurl);
//            item.setUrl("http://www.baidu.com");
//            items.add(item);
//            respXml = WechatUtil.sendArticleMsg(requestMap, items);
        }else if("100".equals(mes)){
            List<Article> articleList = new ArrayList<>();
            //单图文测试
            Article item = new Article();
            item.setTitle("照片墙");
            item.setDescription("风景");
            item.setPicUrl("http://img.zcool.cn/community/0142135541fe180000019ae9b8cf86.jpg@1280w_1l_2o_100sh.png");
            item.setUrl("http://www.baidu.com");
            articleList.add(item);
            respXml = MessageUtil.sendArticleMsg(requestMap, articleList);
        }else {
            respXml =MessageUtil.sendTextMsg(requestMap,"微信公众号测试1");
        }
        return respXml;
    }
}
