package com.ahyx.wechat.communicationplant.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.ahyx.wechat.communicationplant.dao.ChargeOrderMapper;
import com.ahyx.wechat.communicationplant.domain.ChargeOrder;
import com.ahyx.wechat.communicationplant.service.ChargeService;
import com.ahyx.wechat.communicationplant.utils.RestUtils;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/10 16:34
 * @Description:
 */
@Service
public class ChargeServiceImpl implements ChargeService{
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ChargeOrderMapper chargeOrderMapper;

    @Resource
    RestUtils restUtil;

    @Autowired
    BestPayServiceImpl bestPayService;

    /**
     * 微信支付发起
     * @param chargeOrder
     * @return
     */
    @Override
    public PayResponse create(ChargeOrder chargeOrder) {
        PayRequest payRequest = new PayRequest();
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);//支付方式，微信公众账号支付
        payRequest.setOrderId(chargeOrder.getChargeTaskId());//订单号.
//        payRequest.setOrderName(payName);//订单名字.
        payRequest.setOrderAmount(chargeOrder.getPrice().doubleValue());//订单金额.
        payRequest.setOpenid(chargeOrder.getBuyerOpenid());//微信openid, 仅微信支付时需要
        _logger.info("【微信支付】request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        _logger.info("【微信支付】response={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }

    /**
     * 微信退款
     * @param chargeOrder
     * @return
     */
    @Override
    @Async
    public RefundResponse refund(ChargeOrder chargeOrder) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(chargeOrder.getChargeTaskId());
        refundRequest.setOrderAmount(chargeOrder.getPrice());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        _logger.info("【微信退款】request={}", JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        _logger.info("【微信退款】response={}", JsonUtil.toJson(refundResponse));
        return refundResponse;
    }

    /**
     * 插入订单记录
     * @param chargeOrder
     * @return
     */
    @Override
    @Async
    public Integer insertOrder(ChargeOrder chargeOrder) {
        return chargeOrderMapper.insertSelective(chargeOrder);
    }

    /**
     * 查找单条订单
     * @param orderId
     * @return
     */
    @Override
    public ChargeOrder getOrder(String orderId) {
        Example example = new Example(ChargeOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        return chargeOrderMapper.selectOneByExample(example);
    }

    /**
     * 更新订单状态
     * @param chargeOrder
     * @return
     */
    @Override
    public Integer updateOrderByPk(ChargeOrder chargeOrder) {
        return chargeOrderMapper.updateByPrimaryKeySelective(chargeOrder);
    }

    /**
     * 接收微信支付结果通知          ----------此处很多数据要修改-----------
     * @param notifyData
     * @return
     */
    @Override
    public Map<String,Object> notify(String notifyData) {
        Map<String,Object> result=new HashMap<>();
        ChargeOrder chargeOrder=null;
        try {
            PayResponse payResponse = bestPayService.asyncNotify(notifyData);
            result.put("payResponse",payResponse);
            _logger.info("【微信支付】异步通知, payResponse={}", JsonUtil.toJson(payResponse));
            //1.0   查询订单
            chargeOrder=this.getOrder(payResponse.getOrderId());
            if(chargeOrder==null){
                _logger.error("【微信支付】异步通知, 订单不存在, orderId={}", payResponse.getOrderId());
                result.put("success",false);
                result.put("msg","【微信支付】异步通知,订单不存在");
                return result;
            }
            //2.0  判断金额是否一致。相减小于0.01即认为相等
            double substract=payResponse.getOrderAmount()-chargeOrder.getPrice();
            if(substract>0.01||substract<-0.01){
                _logger.error("【微信支付】异步通知, 订单金额不一致, orderId={}, 微信通知金额={}, 系统金额={}",
                        payResponse.getOrderId(),
                        payResponse.getOrderAmount(),
                        chargeOrder.getPrice());
                result.put("success",false);
                result.put("msg","【微信支付】异步通知,订单金额不一致");
                chargeOrder.setChargeStatus(3);//提交失败
                result.put("chargeOrder",chargeOrder);
            }else{
                //3.0 修改订单支付状态
                chargeOrder.setPaystatus(0);//0已支付 1未支付

            }

        }catch (Exception e){
            result.put("success",false);
            result.put("msg","【微信支付】异步通知,系统异常");
            _logger.error("【微信支付】异步通知,系统异常："+e.getMessage());
            return result;
        }

        try {
            //4.0 提交订单到兴芃流量平台
            //------------------------------------代理商账号先写死
            Map<String, Object> params = new HashMap<>();
            params.put("account","wxpay_test");
            params.put("taskId", chargeOrder.getUpOrderId());
            params.put("packageType", chargeOrder.getPackageType());
            params.put("mobile", chargeOrder.getMobile());
            params.put("amount", chargeOrder.getAmount());
            params.put("range",chargeOrder.getRangeType());
            //加密算法
            StringBuffer resign = new StringBuffer();
            resign.append("account=").append("wxpay_test");
            resign.append("&mobile=").append(chargeOrder.getMobile());
            resign.append("&amount=").append(chargeOrder.getAmount());
            resign.append("&range=").append(chargeOrder.getRangeType());
            resign.append("&key=").append("");//代理商apikey  先写死
            String md5sign = DigestUtil.md5Hex(resign.toString());
            params.put("sign",md5sign);
            params.put("callbackUrl","http://ahyx.free.ngrok.cc/charge/flowChargeNotify"); //-------------先写穿透地址
            params.put("orderId",chargeOrder.getUpOrderId());
            String restCallResult=restUtil.restCallExchange("http://139.129.220.55/v1/charge.action",params);

            //修改订单状态，更新订单记录
            chargeOrder.setChargeStatus(2);//提交成功
            this.updateOrderByPk(chargeOrder);
            result.put("chargeOrder",chargeOrder);
            result.put("upToOrder",restCallResult);
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            result.put("msg","提单到上游失败");
            _logger.error("提单到上游失败："+e.getMessage());
            return result;
        }


    }
}
