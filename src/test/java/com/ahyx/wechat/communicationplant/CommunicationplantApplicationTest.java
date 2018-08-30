package com.ahyx.wechat.communicationplant;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.controller.ChargeController;
import com.ahyx.wechat.communicationplant.domain.ChargeOrder;
import com.ahyx.wechat.communicationplant.service.MessageService;
import com.ahyx.wechat.communicationplant.utils.RestUtils;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.TemplateMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.TreeMap;

/**
 * @Author: daimengying
 * @Date: 2018/8/8 18:44
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunicationplantApplicationTest {

//    @Test
//    public void test () {
//        UnifiedRefundRequest unifiedRefundRequest = new UnifiedRefundRequest();
//        String s = unifiedRefundRequest.toXml(unifiedRefundRequest);
//        System.out.println(s);
//    }

//    @Autowired
//    private ChargeController chargeController;
//
//    @Test
//    public void wxNotifyTest () {
//        String msg = "<xml><appid><![CDATA[wx80b2de4273a69ef2]]></appid>\n" +
//                "<bank_type><![CDATA[CFT]]></bank_type>\n" +
//                "<cash_fee><![CDATA[1]]></cash_fee>\n" +
//                "<fee_type><![CDATA[CNY]]></fee_type>\n" +
//                "<is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
//                "<mch_id><![CDATA[1509501031]]></mch_id>\n" +
//                "<nonce_str><![CDATA[SLAU6Ha0RDhghlMM]]></nonce_str>\n" +
//                "<openid><![CDATA[oV0lE0YGg4I3AC6shwNNPG22Y34g]]></openid>\n" +
//                "<out_trade_no><![CDATA[wx20180809121528948]]></out_trade_no>\n" +
//                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
//                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
//                "<sign><![CDATA[0D9ACF1C4E560997AAF83565E4C9A000]]></sign>\n" +
//                "<time_end><![CDATA[20180809121536]]></time_end>\n" +
//                "<total_fee>1</total_fee>\n" +
//                "<trade_type><![CDATA[JSAPI]]></trade_type>\n" +
//                "<transaction_id><![CDATA[4200000175201808095665019493]]></transaction_id>\n" +
//                "</xml>";
//
//        chargeController.wxNotify(null, msg);
//    }

    @Autowired
    MessageService messageService;

    @Test
    public void test() {
        TreeMap<String,TreeMap<String,String>> dataParams = new TreeMap<>();
        TemplateMessage templateMsg = new TemplateMessage();

        ChargeOrder chargeOrder=new ChargeOrder();
        chargeOrder.setMobile("15605655465");
        chargeOrder.setPayMoney(5.22);
        dataParams.put("first", TemplateMessage.item("您的订单已被处理，正在充值中。", "#000000"));
        dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
        dataParams.put("keyword2",TemplateMessage.item("微信支付", "#000000"));
        dataParams.put("keyword4",TemplateMessage.item(chargeOrder.getPayMoney()+"元", "#000000"));
        dataParams.put("remark",TemplateMessage.item("充值存在延迟，请耐心等待！", "#000000"));

        templateMsg.setTemplate_id(WeChatContant.COMMITORDER_SUCCESS_TEMPLATE_ID);//订单受理通知
        templateMsg.setTouser(chargeOrder.getBuyerOpenid());
        templateMsg.setData(dataParams);
        messageService.sendTemplate("13_yFGDrzZ0N1RUUeXKzhoMy0m9FnZgbvS4UfHQFw-5R8yCpnLzc1PZQ8NrZXvgliQkX_RmXvb6V9yyKoGQKqdMDhqIqvO7U9Oc83LDrATMglQIraYtQ64GQl0BeknihE1mRxHHYw2SZ8WMbOpiDGPhAAALOX", net.sf.json.JSONObject.fromObject(templateMsg));
    }

}