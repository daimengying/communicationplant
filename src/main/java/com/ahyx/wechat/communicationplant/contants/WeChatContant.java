package com.ahyx.wechat.communicationplant.contants;

/**
 * @Author: daimengying
 * @Date: 2018/7/3 17:17
 * @Description:
 */
public class WeChatContant {
    public static final String APPID="wx8674a83eacb593a4";
    public static final String APPSECRET="789795652542734764b853d21661b1e1";
    public static final String GRANTTYPE="client_credential";
    public static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token";
    // Token
    public static final String TOKEN = "AHYX";

    public static final String GET_USERINFO_URL="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /******************************************消息相关***************************************/
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";
    public static final String RESP_MESSAGE_TYPE_Link = "link";
    public static final Object REQ_MESSAGE_TYPE_TEXT = "text";
    public static final Object REQ_MESSAGE_TYPE_IMAGE = "image";
    public static final Object REQ_MESSAGE_TYPE_VOICE = "voice";
    public static final Object REQ_MESSAGE_TYPE_VIDEO = "video";
    public static final Object REQ_MESSAGE_TYPE_EVENT = "event";

    public static final String FromUserName = "FromUserName";
    public static final String ToUserName = "ToUserName";
    public static final String MsgType = "MsgType";
    public static final String Content = "Content";

    /******************************************菜单相关***************************************/
    public static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    public static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    public static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    public final static String MENU_CLICK = "click"; // click菜单
    public final static String MENU_VIEW = "view"; // url菜单
    public final static String MENU_SCANCODE_WAITMSG = "scancode_waitmsg"; // 扫码带提示

    /******************************************关注/取消关注***************************************/
    public final static String EVENT_SUBSCRIBE = "subscribe";
    public final static String EVENT_UNSUBSCRIBE = "unsubscribe";
}
