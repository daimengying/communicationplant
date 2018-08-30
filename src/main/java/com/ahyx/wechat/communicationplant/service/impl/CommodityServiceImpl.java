package com.ahyx.wechat.communicationplant.service.impl;

import com.ahyx.wechat.communicationplant.dao.CommodityMapper;
import com.ahyx.wechat.communicationplant.domain.Commodity;
import com.ahyx.wechat.communicationplant.service.CommodityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: daimengying
 * @Date: 2018/8/27 15:27
 * @Description:
 */
@Service
public class CommodityServiceImpl implements CommodityService {

    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CommodityMapper commodityMapper;

    /**
     * 条件查询商品列表
     * @param example
     * @return
     */
    @Override
    public List<Commodity> getCommodityList(Example example) {
        return commodityMapper.selectByExample(example);
    }

    /**
     * 根据ID查找商品
     * @param id
     * @return
     */
    @Override
    public Commodity getCommodity(Integer id) {
        return commodityMapper.selectByPrimaryKey(id);
    }
}
