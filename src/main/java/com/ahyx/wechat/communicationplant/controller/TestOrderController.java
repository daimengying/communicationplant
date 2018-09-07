package com.ahyx.wechat.communicationplant.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.domain.ChargeOrder;
import com.ahyx.wechat.communicationplant.domain.User;
import com.ahyx.wechat.communicationplant.service.ChargeService;
import com.ahyx.wechat.communicationplant.service.MessageService;
import com.ahyx.wechat.communicationplant.utils.*;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.TemplateMessage;
import com.ahyx.wechat.communicationplant.vo.UnifiedRefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: daimengying
 * @Date: 2018/8/6 17:06
 * @Description:
 */
@Controller
public class TestOrderController  {
    @Autowired
    ChargeService chargeService;
    @Resource
    RestUtils restUtil;
    @Autowired
    private WeChatAccountConfig weChatAccountConfig;
    @Autowired
    MessageService messageService;

    public static void main(String[] args) {
        JSONObject result=new JSONObject();
        User user=new User();
        user.setCity("HS");
        user.setSex(2);
        result.put("user",user);
        result.put("time","1577777777");
        System.out.println(result.toString());
        User resultUser= (User) JSONObject.toBean((JSONObject) result.get("user"),User.class);
        System.out.println(resultUser.getCity());
    }

    @RequestMapping("/invokeCharge")
    @ResponseBody
    public String submitOrder(){
        AccessToken accessToken= TokenUtil.getInstance();
        Example example = new Example(ChargeOrder.class,true,false);
        Example.Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("paystatus", 0);
        createCriteria.andEqualTo("refundFlag", 0);
        List<Integer> chargeStatus=new ArrayList<>();
        chargeStatus.add(1);
        chargeStatus.add(3);
        chargeStatus.add(5);
        createCriteria.andIn("chargeStatus",chargeStatus);
        //查找到所有订单失败、已支付且为退款的订单
        List<ChargeOrder> needRefundList = chargeService.listByExample(example);
        if(needRefundList!=null){
            needRefundList.forEach(chargeOrder -> {
                String refundNo="refund"+String.valueOf(new Date().getTime());//退款单号
                UnifiedRefundRequest req = new UnifiedRefundRequest();
                req.setAppid(weChatAccountConfig.getAppId());
                req.setMch_id(weChatAccountConfig.getMchId());
                req.setNonce_str(RandomUtil.generateStringByKey(32,555555));
                req.setOut_trade_no(chargeOrder.getChargeTaskId());
                req.setOut_refund_no(refundNo);
                req.setRefund_fee((int)(chargeOrder.getPayMoney()*100));
                req.setTotal_fee((int)(chargeOrder.getPayMoney()*100));
                Map<String,Object>reqMap= null;
                SSLConnectionSocketFactory sslsf=null;
                CloseableHttpClient httpclient =null;
                try {
                    sslsf= SSLUtil.getSSLsf();
                    httpclient= HttpClients.custom()
                            .setSSLSocketFactory(sslsf)
                            .build();
                    reqMap = ObjectUtils.objectToTreeMap(req);
                    StringBuilder sb = new StringBuilder();
                    for (Iterator<String> it = reqMap.keySet().iterator(); it.hasNext();) {
                        String key=it.next();
                        Object value = reqMap.get(key);
                        // sign不参与签名
                        if ("sign".equals(key)) {
                            continue;
                        }

                        sb.append(key+"=" + value+"&");
                    }
                    String sign= DigestUtil.md5Hex(sb.toString() +"key="+ weChatAccountConfig.getMchKey()).toUpperCase();
                    req.setSign(sign);
                    String xml = req.toXml(req);
                    StringEntity entity = new StringEntity(xml, "UTF-8");
                    //http请求创建
                    HttpPost httppost = new HttpPost(WeChatContant.REFUND_URL);
                    httppost.setEntity(entity);
                    //退款接口请求结果接受和处理
                    CloseableHttpResponse responseEntry = httpclient.execute(httppost);
                    HttpEntity resEntity = responseEntry.getEntity();
                    String returnObj = (resEntity == null) ? null : EntityUtils.toString(resEntity, Consts.UTF_8);
                    EntityUtils.consume(entity);
                    Map<String,Object>returnMap=XmlUtils.readStringXmlOut(returnObj);
                    if("SUCCESS".equals(returnMap.get("return_code"))&&"SUCCESS".equals(returnMap.get("result_code"))){
                        //更新退款状态
                        chargeOrder.setRefundFlag(1);
                        chargeOrder.setOutRefundNo(refundNo);
                        chargeService.updateOrderByPk(chargeOrder);
                        //异步下发退款通知模板
                        TreeMap<String,TreeMap<String,String>> dataParams = new TreeMap<>();
                        TemplateMessage templateMsg = new TemplateMessage();
                        dataParams.put("first", TemplateMessage.item("提交失败的订单退款", "#000000"));
                        dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getChargeTaskId(), "#000000"));
                        dataParams.put("keyword2",TemplateMessage.item(chargeOrder.getName(), "#000000"));
                        dataParams.put("keyword3",TemplateMessage.item(chargeOrder.getPayMoney()+"元", "#000000"));
                        String refundTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        dataParams.put("keyword4",TemplateMessage.item(refundTime, "#000000"));
                        dataParams.put("remark",TemplateMessage.item("您提交失败的订单我们已为您办理退款，感谢您的使用！", "#000000"));

                        templateMsg.setTemplate_id(WeChatContant.REFUND_TEMPLATE_ID);
                        templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                        templateMsg.setData(dataParams);
                        messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                }finally {
                    if(httpclient!=null){
                        try {
                            httpclient.close();
                        } catch (IOException e) {
                        }
                    }
                }

            });
        }
        return "ok";
    }

}
