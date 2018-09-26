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
 * @Date: 2018/9/21 13:48
 * @Description:参与活动记录实体类
 */
@Table(name="g_active_record")
@Data
public class ActiveRecord implements Serializable {

    private static final long serialVersionUID = -7131666578490705636L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    private Integer type; //抽奖活动类型。

    private String mobile;

    private Integer prizeLevel;//中奖等级

    private String prizeName;

    private String memo;

    private Boolean received;//是否领取 0未领取 1已领取

    private Date receivedTime;//领取时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date optionTime;

}
