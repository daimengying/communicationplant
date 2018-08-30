package com.ahyx.wechat.communicationplant.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: daimengying
 * @Date: 2018/8/27 15:13
 * @Description:
 */
@Table(name="g_commodity")
@Data
public class Commodity implements Serializable {


    private static final long serialVersionUID = 6426067253800219219L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    private String name;//商品名称

    private Integer type;//充值类型：1 流量 2 话费

    private Integer amount;//面值

    private Double money;//基础面值

    private Integer operatorType;//运营商类型  0 全网 1 移动 2联通 3电信

    private Integer packageType;//流量包类型：1 月包 2 红包 3 转移 4 卓望 5 1日包 6 3日包 7 7日包 8 季度包 9 半年包 10 年包 11 共享 12 前向

    private String memo;

    private Date optionTime;//提交时间

    private Integer rangeType;//流量类型 0 全国 1省内（改革后只有全国）

    private String locations;//地区

    private  Integer status;
}
