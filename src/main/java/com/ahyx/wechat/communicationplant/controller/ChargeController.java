package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.config.WeChatAccountConfig;
import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.domain.ChargeOrder;
import com.ahyx.wechat.communicationplant.domain.Commodity;
import com.ahyx.wechat.communicationplant.service.ChargeService;
import com.ahyx.wechat.communicationplant.service.MessageService;
import com.ahyx.wechat.communicationplant.service.UserInfoService;
import com.ahyx.wechat.communicationplant.service.impl.CommodityServiceImpl;
import com.ahyx.wechat.communicationplant.utils.HttpsClient;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.utils.XmlUtils;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import com.ahyx.wechat.communicationplant.vo.TemplateMessage;
import com.ahyx.wechat.communicationplant.vo.UserInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lly835.bestpay.model.PayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    CommodityServiceImpl commodityService;

    /**
     * 流量充值首页
     * @return
     */
    @RequestMapping("/flow")
    public String chargeIndex( Model model){
        Example example = new Example(Commodity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",0);
        criteria.andEqualTo("type",1);
        criteria.andEqualTo("operatorType",0);
        List<Commodity>commodities=commodityService.getCommodityList(example);
        model.addAttribute("commodities",commodities);
        return "/flow/index";
    }

    /**
     * 唤起微信支付弹窗
     * @return
     */
    @GetMapping("/create")
    public String create( Model model,HttpServletRequest req, HttpServletResponse resp){
        JSONObject result = new JSONObject();
        String commodityId=req.getParameter("commodityId")+"";
        String phoneNum=req.getParameter("phoneNum")+"";
        Commodity commodity=commodityService.getCommodity(Integer.parseInt(commodityId));
        //获取用户信息
        UserInfo userInfo=null;
        try {
            HttpSession session = req.getSession();
            String openId=session.getAttribute("openId")+"";
            String webAccessToken=session.getAttribute("webAccessToken")+"";
            if(StringUtils.isEmpty(openId)||"null".equals(openId)){
                String getCodeUrl=WeChatContant.GET_CODE_URL.replace("APPID",weChatAccountConfig.getAppId())
                        .replace("REDIRECT_URI", URLEncoder.encode(req.getScheme()+"://"+ req.getServerName()+"/wechat/getOpenId","utf-8"))
                        .replace("SCOPE","snsapi_userinfo").replace("STATE","");
                session.setAttribute("redirect",req.getScheme()+"://"+ req.getServerName()+"/charge/create?commodityId="+commodityId+"&phoneNum="+phoneNum);
                resp.sendRedirect(getCodeUrl);
                return null;
            }else{
                String getWebUserInfoUrl=WeChatContant.GET_WEB_USERINFO_URL.replace("ACCESS_TOKEN", webAccessToken).replace("OPENID",openId);
                net.sf.json.JSONObject userObj=HttpsClient.httpRequest(getWebUserInfoUrl,"GET",null);
                userInfo=(UserInfo) net.sf.json.JSONObject.toBean(userObj, UserInfo.class);
                result.put("success",true);
                result.put("openId",openId);
                result.put("phoneNum",phoneNum);
                model.addAttribute("commodity",net.sf.json.JSONObject.fromObject(commodity));
            }

        }catch (Exception e){
            result.put("success",false);
            _logger.error("订单详情失败："+e.getMessage());
        }
        model.addAttribute("orderDetail",result);
        model.addAttribute("userInfo",net.sf.json.JSONObject.fromObject(userInfo).toString());
        return "/flow/detail";
    }

    @RequestMapping("/invokeCharge")
    @ResponseBody
    public String invokeWechatCharge(@RequestBody String params){
        JSONObject parObject = JSON.parseObject(params);
        net.sf.json.JSONObject userObj=net.sf.json.JSONObject.fromObject(parObject.getString("userInfo"));
        net.sf.json.JSONObject orderDetail=net.sf.json.JSONObject.fromObject(parObject.getString("orderDetail"));
        net.sf.json.JSONObject commodityObj=net.sf.json.JSONObject.fromObject(parObject.getString("commodity"));

        UserInfo userInfo=(UserInfo)net.sf.json.JSONObject.toBean(userObj,UserInfo.class);
        Commodity commodity=(Commodity)net.sf.json.JSONObject.toBean(commodityObj,Commodity.class);
        Map<String,Object>result=new HashMap<>();
        try {
            //1.0 创建订单
            ChargeOrder chargeOrder = new ChargeOrder();
            chargeOrder.setOperatorType(commodity.getOperatorType());
            chargeOrder.setBuyerOpenid(orderDetail.getString("openId"));
            chargeOrder.setName(commodity.getName());
            chargeOrder.setMobile(orderDetail.getString("phoneNum"));
            chargeOrder.setAmount(commodity.getAmount());
            chargeOrder.setPrice(commodity.getMoney());
            chargeOrder.setOptionTime(new Date());
            String taskId="wx"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            chargeOrder.setChargeTaskId(taskId);
            chargeOrder.setChargeStatus(1);//默认未知异常
            chargeOrder.setPackageType(commodity.getPackageType());
            chargeOrder.setRangeType(commodity.getRangeType());
            chargeOrder.setType(commodity.getType());
            chargeOrder.setOperatorType(commodity.getOperatorType());
            chargeService.insertOrder(chargeOrder);
            //2.0  发起微信支付
            PayResponse payResponse = chargeService.create(chargeOrder);
            result.put("payResponse", payResponse);
            result.put("success",true);
            result.put("orderId",taskId);
        }catch (Exception e){
            result.put("success",false);
            _logger.error("发起微信支付失败："+e.getMessage());
        }
        return JSON.toJSONString(result);
    }


    /**
     * 接收微信支付通知，成功则跳转支付结果详情页
     */
    @PostMapping("/wxNotify")
    @ResponseBody
    public String wxNotify(Model model, @RequestBody String notifyData){
        Map<String,Object> notify=chargeService.notify(notifyData);
        boolean success=Boolean.valueOf(notify.get("success").toString());
        ChargeOrder chargeOrder=(ChargeOrder) notify.get("chargeOrder");
        PayResponse payResponse =(PayResponse) notify.get("payResponse");
        if(payResponse.getOrderAmount()==chargeOrder.getPayMoney()&&chargeOrder.getWxnotifyFlag()==0){
            //异步下发提单结果模板消息
            TreeMap<String,TreeMap<String,String>> dataParams = new TreeMap<>();
            TemplateMessage templateMsg = new TemplateMessage();

            AccessToken accessToken= TokenUtil.getInstance();
            if(accessToken!=null){
                if(success){
                    //提单成功
                    dataParams.put("first", TemplateMessage.item("您的订单已被处理，正在充值中。", "#000000"));
                    dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
                    dataParams.put("keyword2",TemplateMessage.item("微信支付", "#000000"));
                    dataParams.put("keyword3",TemplateMessage.item(payResponse.getOrderAmount()+"元", "#000000"));
                    dataParams.put("remark",TemplateMessage.item("充值存在延迟，请耐心等待！", "#000000"));

                    templateMsg.setTemplate_id(WeChatContant.COMMITORDER_SUCCESS_TEMPLATE_ID);//订单受理通知
                    templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                    templateMsg.setData(dataParams);
                    messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                }else{
                    //提单失败
                    dataParams.put("first", TemplateMessage.item("您的订单提交失败", "#000000"));
                    dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
                    dataParams.put("keyword2",TemplateMessage.item("微信支付", "#000000"));
                    dataParams.put("keyword3",TemplateMessage.item(payResponse.getOrderAmount()+"元", "#000000"));
                    dataParams.put("remark",TemplateMessage.item("订单提交失败，失败原因："+notify.get("msg").toString()+"。我们稍后会为您返款，请耐心等待！", "#000000"));

                    templateMsg.setTemplate_id(WeChatContant.COMMITORDER_SUCCESS_TEMPLATE_ID);//订单受理通知
                    templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                    templateMsg.setData(dataParams);
                    messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                }
                chargeOrder.setWxnotifyFlag(1);
                chargeService.updateOrderByPk(chargeOrder);
            }

        }
        Map<String,Object>result=new HashMap<>();
        result.put("return_code","SUCCESS");
        result.put("return_msg","OK");
        return XmlUtils.mapToXmlString(result);
    }


    @GetMapping("/result")
    public String flowChargeResult(Model model,HttpServletRequest req){
        String orderId=req.getParameter("orderId");
        ChargeOrder chargeOrder=chargeService.getOrder(orderId);
        model.addAttribute("chargeOrder",chargeOrder);
        return "flow/result";
    }

    /**
     * 接收兴芃平台流量充值回调
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/flowChargeNotify")
    @ResponseBody
    public String flowChargeNotify(String status,String taskId,String msg,String orderId) throws IOException {
//        String status=request.getParameter("status");
//        String upOrderId=request.getParameter("taskId");//上游单号
//        String msg= new String(request.getParameter("msg").getBytes("iso-8859-1"), "utf-8");
//        String orderId=request.getParameter("orderId");
        try {
            //修改订单状态
            ChargeOrder chargeOrder=chargeService.getOrder(orderId);
            chargeOrder.setReportTime(new Date());
            chargeOrder.setChargeStatus(status.equals("3")?5:Integer.parseInt(status));//兴芃提交到上游失败也认为充值失败
            chargeOrder.setReportContent(new String(msg.getBytes("iso-8859-1"), "utf-8"));
            chargeOrder.setMemo("上游返回："+new String(msg.getBytes("iso-8859-1"), "utf-8"));
            chargeOrder.setUpOrderId(taskId);
            chargeService.updateOrderByPk(chargeOrder);
            //异步发起充值结果模板消息，异步退款
            TreeMap<String,TreeMap<String,String>> dataParams = new TreeMap<>();
            TemplateMessage templateMsg = new TemplateMessage();
            AccessToken accessToken= TokenUtil.getInstance();
            if(accessToken!=null){
                if("5".equals(status)){
                    //充值失败，发起模板消息
                    dataParams.put("first", TemplateMessage.item("订单充值失败通知", "#000000"));
                    dataParams.put("number",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
                    dataParams.put("value",TemplateMessage.item(chargeOrder.getPayMoney()+"元", "#000000"));
                    dataParams.put("remark",TemplateMessage.item("退款需要1-3个工作日完成，请耐心等待，谢谢！", "#000000"));

                    templateMsg.setTemplate_id(WeChatContant.CHARGE_FAIL_TEMPLATE_ID);
                    templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                    templateMsg.setData(dataParams);
                    messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));

                }else if("4".equals(status)){
                    //充值成功
                    dataParams.put("first", TemplateMessage.item("充值成功通知", "#000000"));
                    dataParams.put("keyword1",TemplateMessage.item(chargeOrder.getMobile(), "#000000"));
                    dataParams.put("keyword2",TemplateMessage.item(chargeOrder.getPrice()+"元", "#000000"));
                    dataParams.put("keyword3",TemplateMessage.item(chargeOrder.getPrice()+"元", "#000000"));
                    String optionTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chargeOrder.getOptionTime());
                    dataParams.put("keyword4",TemplateMessage.item(optionTime, "#000000"));
                    dataParams.put("remark",TemplateMessage.item("订单受充值成功，感谢使用！", "#000000"));
                    templateMsg.setTemplate_id(WeChatContant.CHARGE_SUCCESS_TEMPLATE_ID);
                    templateMsg.setTouser(chargeOrder.getBuyerOpenid());
                    templateMsg.setData(dataParams);
                    messageService.sendTemplate(accessToken.getToken(), net.sf.json.JSONObject.fromObject(templateMsg));
                }
            }
        }catch (Exception e){
            _logger.error("兴芃平台回调失败", e);
        }
        return "ok";
    }

}
