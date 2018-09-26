package com.ahyx.wechat.communicationplant.service;

import com.ahyx.wechat.communicationplant.domain.MobileArea;

/**
 * @Author: daimengying
 * @Date: 2018/9/19 13:49
 * @Description:根据手机号获取省市信息
 */
public interface MobileAreaService {

    MobileArea getAreaByMobile(String mobile);
}
