package com.ahyx.wechat.communicationplant.contants;


/**
 * @Author: daimengying
 * @Date: 2018/7/3 17:17
 * @Description:
 */
public class WeChatContant {
//    public static final String APPID="wx8674a83eacb593a4";
//    public static final String APPSECRET="789795652542734764b853d21661b1e1";
//    public static final String APPID="wx80b2de4273a69ef2";
//    public static final String APPSECRET="44210325e8b1a0c621d7084a0c0099fb";
    public static final String GRANTTYPE="client_credential";
    public static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token";
    // Token
    public static final String TOKEN = "AHYX";

    /******************************************用户信息***************************************/
    public static final String GET_USERINFO_URL="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static final String GET_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    //通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token不同
    public static final String GET_WEB_ACCESS_TOEN="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static final String GET_WEB_USERINFO_URL="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static final String DEVELOPMENT_OPNEID="gh_73f66f01788d ";

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

    /******************************************模板消息***************************************/
    public static final String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";//发送模板消息
    public static final String CHARGE_FAIL_TEMPLATE_ID="B_4T7UCuu5n-T-T1hlZ1GxhS1WgDH-RF86plwbsOHio"; //充值失败模板
    public static final String CHARGE_SUCCESS_TEMPLATE_ID="4xPPUa3HZtGTTrMZpQml5vQ-LO9KmePLwBU0UKCPvQM";//充值成功模板
    public static final String REFUND_TEMPLATE_ID="O0950-g--S0k_bsDWyrCDHu6WZBf5M_LASUuCLZM1uc";//退款通知
    public static final String COMMITORDER_SUCCESS_TEMPLATE_ID="mwhbwco4ZdosxKuWTGltlB-_NinGMhVBjPpDOcfxG-M";//提单成功

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

    /******************************************微信退款***************************************/
    public final static String REFUND_URL="https://api.mch.weixin.qq.com/secapi/pay/refund";
}
