package com.ahyx.wechat.communicationplant.utils;

import com.ahyx.wechat.communicationplant.domain.RafflePrize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: daimengying
 * @Date: 2018/9/21 17:13
 * @Description:抽奖工具类
 */
public class RaffleUtil {

    /**
     * 根据随机数获取奖品
     * @param prizes
     * @return
     */
    public static RafflePrize lottery(List<RafflePrize> prizes) {
        if(prizes.isEmpty()){
            return null;
        }

        //计算总概率
        double sumProbability=prizes.stream().collect(Collectors.summarizingDouble(RafflePrize::getProbability)).getSum();
        if(sumProbability>100){
            return null;
        }

        RaffleUtil util=new RaffleUtil();
        int lotteryIndex=util.getPrizeIndex(prizes,sumProbability);
        RafflePrize resultPrize=prizes.get(lotteryIndex);
        //如果抽中的奖品数量不足，循环获取概率
        Integer prizeCount=resultPrize.getCount();
        while(prizeCount<1){
            lotteryIndex=util.getPrizeIndex(prizes,sumProbability);
            resultPrize=prizes.get(lotteryIndex);
            prizeCount=resultPrize.getCount();
        }
        return resultPrize;
    }

    /**
     * 计算每个奖品的概率区间
     * 例如奖品A概率区间0-10  奖品B概率区间 10-50 奖品C概率区间5-100
     * 每个奖品的中奖率越大，所占的概率区间就越大
     * @param prizes
     * @param sumProbability
     * @return
     */
    public int getPrizeIndex(List<RafflePrize> prizes,double sumProbability){
        Double  tempSumProbability = 0d;
        Double[] arr = new Double[]{tempSumProbability};//将变量定义为数组才能在lambda中使用
        List<Double> sortAwardProbabilityList =prizes.stream().map(award -> {
            arr[0] += award.getProbability();
            return arr[0] / sumProbability;
        }).collect(Collectors.toList());

        //产生0-1之间的随机数,随机数在哪个概率区间内，则是哪个奖品
        double randomDouble = Math.random();
        //加入到概率区间中，排序后，返回的下标则是prizes中中奖的下标
        sortAwardProbabilityList.add(randomDouble);
        Collections.sort(sortAwardProbabilityList);
        return  sortAwardProbabilityList.indexOf(randomDouble);
    }



    public static void main(String[] args) {
        List<RafflePrize> prizes=new ArrayList<>();
        RafflePrize a=new RafflePrize();
        a.setProbability(20d);
        a.setCount(1);
        RafflePrize b=new RafflePrize();
        b.setProbability(70d);
        b.setCount(0);
        RafflePrize c=new RafflePrize();
        c.setProbability(9d);
        c.setCount(2);
        prizes.add(a);
        prizes.add(b);
        prizes.add(c);
        RafflePrize prize=lottery(prizes);
        System.out.println("概率："+prize.getProbability()+"----数量："+prize.getCount());

    }
}
