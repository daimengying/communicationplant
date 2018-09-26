package com.ahyx.wechat.communicationplant.service.impl;

import com.ahyx.wechat.communicationplant.dao.ActiveRecordMapper;
import com.ahyx.wechat.communicationplant.dao.RafflePrizeMapper;
import com.ahyx.wechat.communicationplant.domain.ActiveRecord;
import com.ahyx.wechat.communicationplant.domain.RafflePrize;
import com.ahyx.wechat.communicationplant.service.ActiveService;
import com.ahyx.wechat.communicationplant.utils.DateUtils;
import com.ahyx.wechat.communicationplant.utils.RaffleUtil;
import com.github.pagehelper.PageRowBounds;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: daimengying
 * @Date: 2018/9/21 15:06
 * @Description:各种活动的逻辑
 */
@Service
public class ActiveServiceImpl implements ActiveService {
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    ActiveRecordMapper activeRecordMapper;
    @Resource
    RafflePrizeMapper rafflePrizeMapper;


    /**
     * 获取活动奖品列表
     * @param type
     * @return
     */
    @Override
    public List<RafflePrize> getPrizeList(Integer type) {
        Example example = new Example(RafflePrize.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",type);
        criteria.andEqualTo("status",0);
        return rafflePrizeMapper.selectByExample(example);
    }

    /**
     * 条件查询用户中奖记录，如果号码为空取最新5条
     * @param mobile
     * @return
     */
    @Override
    public List<ActiveRecord> raffleRecordList(String mobile) {
        Example example = new Example(ActiveRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",1);
        example.setOrderByClause(" prize_level,option_time desc");
        if (!StringUtils.isEmpty(mobile)){
            criteria.andEqualTo("mobile",mobile);
            return  activeRecordMapper.selectByExampleAndRowBounds(example,new PageRowBounds(0,5));
        }
        return activeRecordMapper.selectByExample(example);
    }


    /**
     * 更新奖品表，主要更新奖品库存
     * @param prize
     * @return
     */
    @Override
    @Async
    public Integer updateRafflePrize(RafflePrize prize) {
        return rafflePrizeMapper.updateByPrimaryKeySelective(prize);
    }

    /**
     * 插入一条抽奖记录。异步
     * @param raffleRecord
     * @return
     */
    @Async
    @Override
    public Integer insertRaffleRecord(ActiveRecord raffleRecord) {
        return activeRecordMapper.insertSelective(raffleRecord);
    }

    /**
     * 抽奖核心逻辑
     * @param mobile
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> raffleLogic(String mobile) throws Exception{
        Map<String,Object> result = new HashMap<>();
        //1.0 同一手机号每天限定抽奖次数
        Example example = new Example(ActiveRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",1);
        criteria.andEqualTo("mobile",mobile);
        criteria.andBetween("optionTime", formatter.format(new Date()), DateUtils.getNextDate(new Date()));
        List<ActiveRecord> recordsByDay=activeRecordMapper.selectByExample(example);
        //暂定为每天限制抽奖一次
        if(recordsByDay!=null&&recordsByDay.size()>0){
            result.put("code","-1001");//今日有抽奖记录
            result.put("success",false);
            return result;
        }
        //2.0 有抽奖机会，实现抽奖逻辑
        example = new Example(RafflePrize.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("type",1);
        criteria.andEqualTo("status",0);
        List<RafflePrize> prizes=rafflePrizeMapper.selectByExample(example);
        //抽中的奖品
        RafflePrize getPrize=RaffleUtil.lottery(prizes);
        result.put("code","0");//抽奖成功
        result.put("success",true);
        result.put("prizeLevel",getPrize.getLevel());
        result.put("prizeName",getPrize.getName());
        //3.0 抽奖记录入库。异步
        ActiveRecord  insertRecord=new ActiveRecord();
        insertRecord.setType(1);
        insertRecord.setMobile(mobile);
        insertRecord.setPrizeLevel(getPrize.getLevel());
        insertRecord.setPrizeName(getPrize.getName());
        insertRecord.setOptionTime(new Date());
        insertRaffleRecord(insertRecord);
        //4.0 修改商品库存。异步
        getPrize.setCount(getPrize.getCount()-1);
        this.updateRafflePrize(getPrize);
        return result;
    }



}
