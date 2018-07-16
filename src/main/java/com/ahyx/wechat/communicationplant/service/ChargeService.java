package com.ahyx.wechat.communicationplant.service;

import com.ahyx.wechat.communicationplant.domain.ChargeOrder;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/10 16:33
 * @Description:
 */
public interface ChargeService {
    //微信支付发起
    PayResponse create(ChargeOrder chargeOrder);

    //微信退款
    RefundResponse refund(ChargeOrder chargeOrder);

    //订单入库
    Integer insertOrder(ChargeOrder chargeOrder);

    //查询订单
    ChargeOrder getOrder(String orderId);

    //更新订单
    Integer updateOrderByPk(ChargeOrder chargeOrder);

    //接收微信支付结果通知
    Map<String,Object> notify(String notifyData);

}
