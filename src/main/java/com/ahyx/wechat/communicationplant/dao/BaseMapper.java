package com.ahyx.wechat.communicationplant.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Author: daimengying
 * @Date: 2018/5/18 14:45
 * @Description:被继承的Mapper，一般业务Mapper继承它
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
