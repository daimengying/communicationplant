package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.domain.ChargeOrder;
import com.ahyx.wechat.communicationplant.service.ChargeService;
import com.ahyx.wechat.communicationplant.service.MessageService;
import com.ahyx.wechat.communicationplant.service.UserInfoService;
import com.ahyx.wechat.communicationplant.utils.HttpUtil;
import com.ahyx.wechat.communicationplant.utils.HttpsClient;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.TemplateMessage;
import com.ahyx.wechat.communicationplant.vo.UserInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: daimengying
 * @Date: 2018/7/10 14:41
 * @Description:微信流量充值
 */
@EnableAsync
@Controller
@RequestMapping("/charge")
public class ChargeController {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ChargeService chargeService;
    @Autowired
    MessageService messageService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    WeChatAccountConfig weChatAccountConfig;

    /**
     * 流量充值首页
     * @return
     */
    @RequestMapping("/flow")
    public String chargeIndex(){
        return "/flow/index";
    }

    /**
     * 唤起微信支付弹窗
     * @param params
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public String create(@RequestBody String params, HttpServletRequest req, HttpServletResponse resp){
        JSONObject parObject = JSON.parseObject(params);
        JSONObject result = new JSONObject();
        try {
            //获取用户信息
            UserInfo userInfo=null;
            HttpSession session = req.getSession();
            String openId=session.getAttribute("openId")+"";
            String webAccessToken=session.getAttribute("webAccessToken")+"";
            if(StringUtils.isEmpty(openId)||"null".equals(openId)){
                String getCodeUrl=WeChatContant.GET_CODE_URL.replace("APPID",weChatAccountConfig.getAppId())
                        .replace("REDIRECT_URI",req.getScheme()+"://"+ req.getServerName()+"/getOpenId")
                        .replace("SCOPE","snsapi_userinfo").replace("STATE","");
                session.setAttribute("redirect",req.getScheme()+"://"+ req.getServerName()+"/create");
                resp.sendRedirect(getCodeUrl);
            }else{
                String getWebUserInfoUrl=WeChatContant.GET_WEB_USERINFO_URL.replace("ACCESS_TOKEN", webAccessToken).replace("OPENID",openId);
                net.sf.json.JSONObject userObj=HttpsClient.httpRequest(getWebUserInfoUrl,"GET",null);
                userInfo=(UserInfo) net.sf.json.JSONObject.toBean(userObj, UserInfo.class);
                //1.0 创建订单
                ChargeOrder chargeOrder = new ChargeOrder();
                chargeOrder.setBuyerOpenid(openId);
                chargeOrder.setName(parObject.getString("name"));
//        chargeOrder.setMobile(parObject.getString("mobile"));
//        chargeOrder.setAmount(Integer.parseInt(parObject.get("amount")+""));
                chargeOrder.setPrice(Double.parseDouble(parObject.get("price")+""));
                chargeOrder.setBuyerOpenid(userInfo.getOpenid());
                chargeOrder.setOptionTime(new Date());
                String taskId="wx"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                chargeOrder.setChargeTaskId(taskId);
                chargeOrder.setChargeStatus(1);//默认未知异常
//        chargeOrder.setPackageType(Integer.parseInt(parObject.get("packageType")+""));
//        chargeOrder.setPackageId(Integer.parseInt(parObject.get("packageId")+""));
//        chargeOrder.setRangeType(Integer.parseInt(parObject.get("rangeType")+""));
//        chargeOrder.setType(Integer.parseInt(parObject.get("type")+""));
                chargeService.insertOrder(chargeOrder);

                //2.0  发起微信支付
                PayResponse payResponse = chargeService.create(chargeOrder);
                result.put("success",true);
                result.put("orderId",taskId);
                result.put("payResponse", payResponse);
            }

        }catch (Exception e){
            result.put("success",false);
            _logger.error("发起微信支付失败："+e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    /**
     * 接收微信支付通知，成功则跳转支付结果详情页
     */
    @RequestMapping("/wxNotify")
    public String wxNotify(Model model, @RequestBody String notifyData){
        Map<String,Object> notify=chargeService.notify(notifyData);
        boolean success=Boolean.valueOf(notify.get("success").toString());
        String msg=notify.get("msg").toString();
        ChargeOrder chargeOrder=(ChargeOrder) notify.get("chargeOrder");

        //异步下发提单结果模板消息
        TreeMap<String,TreeMap<String,String>> dataParams = new TreeMap<>();
        TemplateMessage templateMsg = new TemplateMessage();

        AccessToken accessToken= TokenUtil.getInstance();
        if(accessToken!=null){
            if(success){
                //提单成功
                dataParams.put("first", TemplateMessage.item("下单成功", "#000000"));
                dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
                dataParams.put("keyword2",TemplateMessage.item("微信支付", "#000000"));
                dataParams.put("keyword4",TemplateMessage.item(chargeOrder.getPrice()+"元", "#000000"));
                dataParams.put("remark",TemplateMessage.item("订单提交成功，请耐心等待！", "#000000"));

                templateMsg.setTemplate_id(WeChatContant.COMMITORDER_SUCCESS_TEMPLATE_ID);//订单受理通知
                templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                templateMsg.setData(dataParams);
                messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
            }else{
                //提单失败
                dataParams.put("first", TemplateMessage.item("下单失败", "#000000"));
                dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
                dataParams.put("keyword2",TemplateMessage.item("微信支付", "#000000"));
                dataParams.put("keyword4",TemplateMessage.item(chargeOrder.getPrice()+"元", "#000000"));
                dataParams.put("remark",TemplateMessage.item("订单提交失败，失败原因："+msg+"。我们稍后会为您返款，请耐心等待！", "#000000"));

                templateMsg.setTemplate_id(WeChatContant.COMMITORDER_SUCCESS_TEMPLATE_ID);//订单受理通知
                templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                templateMsg.setData(dataParams);
                messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                //微信退款，支付成功状态下发起退款消息
                if(chargeOrder.getPaystatus()==0){
                    RefundResponse refundResponse=chargeService.refund(chargeOrder);
                    if(accessToken!=null&&refundResponse!=null){
                        //异步下发退款通知模板
                        dataParams = new TreeMap<>();
                        dataParams.put("first", TemplateMessage.item("提交失败订单退款", "#000000"));
                        dataParams.put("keyword1",TemplateMessage.item(refundResponse.getOrderId(), "#000000"));
                        dataParams.put("keyword2",TemplateMessage.item(chargeOrder.getName(), "#000000"));
                        dataParams.put("keyword3",TemplateMessage.item(refundResponse.getOrderAmount()+"元", "#000000"));
                        String refundTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        dataParams.put("keyword4",TemplateMessage.item(refundTime, "#000000"));
                        dataParams.put("remark",TemplateMessage.item("您提交失败的订单我们已为您办理退款，感谢您的使用！", "#000000"));
                        templateMsg = new TemplateMessage();
                        templateMsg.setTemplate_id(WeChatContant.REFUND_TEMPLATE_ID);
                        templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                        templateMsg.setData(dataParams);
                        messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                    }
                }
            }

        }

        model.addAttribute("wxNotify",notify);
        return "/flow/detail";
    }

    /**
     * 接收兴芃平台流量充值回调
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/flowChargeNotify")
    @ResponseBody
    public String flowChargeNotify(HttpServletRequest request) throws IOException {
        try {
            String jsonString = HttpUtil.getJsonString(request);
            if(jsonString  == null){
                return null;
            }
            JSONObject jsonObject = JSON.parseObject(jsonString);

            String status=jsonObject.getString("status");
            String upOrderId=jsonObject.getString("taskId");
            String msg=jsonObject.getString("msg");
            String orderId=jsonObject.getString("orderId");
            //修改订单状态
            ChargeOrder chargeOrder=chargeService.getOrder(orderId);
            chargeOrder.setReportTime(new Date());
            chargeOrder.setChargeStatus(Integer.parseInt(status));
            chargeOrder.setReportContent(msg);
            chargeOrder.setUpOrderId(upOrderId);
            chargeService.updateOrderByPk(chargeOrder);
            //异步发起充值结果模板消息，异步退款
            TreeMap<String,TreeMap<String,String>> dataParams = new TreeMap<>();
            TemplateMessage templateMsg = new TemplateMessage();
            AccessToken accessToken= TokenUtil.getInstance();
            if("5".equals(status)){
                if(accessToken!=null){
                    //充值失败，发起模板消息
                    dataParams.put("first", TemplateMessage.item("您的订单充值失败，稍后将为您办理退款，请注意查收！", "#000000"));
                    dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
                    dataParams.put("keyword2",TemplateMessage.item(chargeOrder.getPrice()+"元", "#000000"));
                    dataParams.put("remark",TemplateMessage.item("退款需要1-3个工作日完成，请耐心等待，谢谢！", "#000000"));

                    templateMsg.setTemplate_id(WeChatContant.CHARGE_FAIL_TEMPLATE_ID);
                    templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                    templateMsg.setData(dataParams);
                    messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                }

                //微信退款，支付成功状态下发起退款消息
                if(chargeOrder.getPaystatus()==0){
                    RefundResponse refundResponse=chargeService.refund(chargeOrder);
                    if(accessToken!=null&&refundResponse!=null){
                        //异步下发退款通知模板
                        dataParams = new TreeMap<>();
                        dataParams.put("first", TemplateMessage.item("充值失败订单退款", "#000000"));
                        dataParams.put("keyword1",TemplateMessage.item(refundResponse.getOrderId(), "#000000"));
                        dataParams.put("keyword2",TemplateMessage.item(chargeOrder.getName(), "#000000"));
                        dataParams.put("keyword3",TemplateMessage.item(refundResponse.getOrderAmount()+"元", "#000000"));
                        String refundTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        dataParams.put("keyword4",TemplateMessage.item(refundTime, "#000000"));
                        dataParams.put("remark",TemplateMessage.item("您的充值失败订单我们已为您办理退款，感谢您的使用！", "#000000"));
                        templateMsg = new TemplateMessage();
                        templateMsg.setTemplate_id(WeChatContant.REFUND_TEMPLATE_ID);
                        templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                        templateMsg.setData(dataParams);
                        messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                    }
                }
            }
        }catch (Exception e){
            _logger.error("提交到流量平台充值失败", e);
        }
        return "ok";
    }

}
