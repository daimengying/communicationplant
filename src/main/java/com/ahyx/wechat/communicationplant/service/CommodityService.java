package com.ahyx.wechat.communicationplant.service;

import com.ahyx.wechat.communicationplant.domain.Commodity;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: daimengying
 * @Date: 2018/8/27 15:22
 * @Description:商品服务
 */
public interface CommodityService {
    //获取商品列表
    List <Commodity> getCommodityList(Example example);

    //根据ID查找商品
    Commodity getCommodity(Integer id);

}
