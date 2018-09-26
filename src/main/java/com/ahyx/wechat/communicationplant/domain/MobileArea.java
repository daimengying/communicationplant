package com.ahyx.wechat.communicationplant.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: daimengying
 * @Date: 2018/9/19 11:46
 * @Description:
 */
@Table(name="g_mobile_area")
@Data
public class MobileArea implements Serializable {

    private static final long serialVersionUID = -3481572437467798347L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    private String phone;

    private String province;

    private String city;

    private String serviceProvider;
}
