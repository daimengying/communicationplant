package com.ahyx.wechat.communicationplant.service.impl;

import com.ahyx.wechat.communicationplant.dao.MobileAreaMapper;
import com.ahyx.wechat.communicationplant.domain.MobileArea;
import com.ahyx.wechat.communicationplant.service.MobileAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author: daimengying
 * @Date: 2018/9/19 13:52
 * @Description:
 */
@Service
public class MobileAreaServiceImpl implements MobileAreaService {
    @Autowired
    MobileAreaMapper mobileAreaMapper;

    @Override
    public MobileArea getAreaByMobile(String mobile) {
        String mobileStart=mobile.substring(0,7);
        Example example = new Example(MobileArea.class);
        Example.Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("phone", mobileStart);
        MobileArea area=mobileAreaMapper.selectOneByExample(example);
        return area;
    }
}
