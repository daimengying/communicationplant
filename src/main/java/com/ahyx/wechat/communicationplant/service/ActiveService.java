package com.ahyx.wechat.communicationplant.service;

import com.ahyx.wechat.communicationplant.domain.ActiveRecord;
import com.ahyx.wechat.communicationplant.domain.RafflePrize;

import java.util.List;
import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/9/21 14:54
 * @Description:
 */
public interface ActiveService {
    /**
     * 抽奖相关
     * @param mobile
     * @return
     */
    //根据手机号查询当天抽奖记录
    List<ActiveRecord> raffleRecordList(String mobile);

    //获取奖品列表
    List<RafflePrize> getPrizeList(Integer type);

    //抽奖逻辑
    Map<String,Object> raffleLogic(String mobile) throws Exception;

    //更新奖品表
    Integer updateRafflePrize(RafflePrize prize);

    //插入抽奖记录
    Integer insertRaffleRecord(ActiveRecord raffleRecord);
}
