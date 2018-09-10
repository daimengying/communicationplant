package com.ahyx.wechat.communicationplant;

import cn.hutool.crypto.digest.DigestUtil;
import com.ahyx.wechat.communicationplant.config.UserAccountConfig;
import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.controller.ChargeController;
import com.ahyx.wechat.communicationplant.dao.ChargeOrderMapper;
import com.ahyx.wechat.communicationplant.domain.ChargeOrder;
import com.ahyx.wechat.communicationplant.service.MessageService;
import com.ahyx.wechat.communicationplant.utils.RestUtils;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.TemplateMessage;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
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
    private UserAccountConfig userAccountConfig;

    @Test
    public void test() {
        System.out.println(userAccountConfig.getAccount()+"====="+userAccountConfig.getApikey());

    }

}