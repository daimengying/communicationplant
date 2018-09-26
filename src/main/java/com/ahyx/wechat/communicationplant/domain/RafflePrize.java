package com.ahyx.wechat.communicationplant.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: daimengying
 * @Date: 2018/9/21 10:13
 * @Description:抽奖奖品实体类
 */
@Table(name="g_raffle_prize")
@Data
public class RafflePrize implements Serializable {

    private static final long serialVersionUID = -4258235424532785157L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    private Integer type; //抽奖活动类型。

    private String name; //奖品名称

    private Integer count;//奖品数量

    private Integer level;//奖品等级

    private Double probability;//出现概率

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date optionTime;

    private Integer status;
}
