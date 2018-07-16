package com.ahyx.wechat.communicationplant.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
* @author daimengying
* @version 创建时间：2018年7月10日 16:53
* 
*/
@Table(name="g_charge_order")
@Data
public class ChargeOrder implements Serializable {


	private static final long serialVersionUID = -8219407652919631184L;
	@Id
	@GeneratedValue(generator="JDBC")
	private Integer id;
	
	private String mobile;//手机号

	private String name;//商品名称

	private String buyerOpenid;//下单者的微信openid
	
	private Integer type;//运营商类型   1 移动 2联通 3电信
	
	private Integer rangeType;//流量类型 0 全国 1省内
	
	private Integer packageId;// 流量包ID

	//流量包类型：1 月包 2 红包 3 转移 4 卓望 5 1日包 6 3日包 7 7日包 8 季度包 9 半年包 10 年包 11 共享 12 前向
	private Integer packageType;
	
	private Integer amount;//面值
	
	private Double price;//售价
	
	private Date optionTime;//提交时间
	
	private Integer submitType;//提交类型（固定6 微信充值）
	
	private String chargeTaskId;// 订单号
	
	private Integer chargeStatus;//充值状态  1 未知  2 提交成功  3 提交失败  4 充值成功  5 充值失败
	
	private Date reportTime;//报告时间
	
	private String reportContent;//报告内容
	
	private String memo;//备注
	
	private String error;//错误信息
	
	private Integer paystatus ;//支付状态 0 已支付 1未支付

	private String upOrderId;//上游返回的订单号
	
	private Integer refundFlag;//退款标记  0 未退款 1 已退款
	
}
