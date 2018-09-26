package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.domain.ActiveRecord;
import com.ahyx.wechat.communicationplant.domain.RafflePrize;
import com.ahyx.wechat.communicationplant.service.ActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: daimengying
 * @Date: 2018/9/4 11:27
 * @Description: 活动中心
 */
@Controller
@EnableAsync
@RequestMapping("/active")
public class ActiveController {
    @Autowired
    ActiveService activeService;

    /**
     * 手淘拉新
     */
    @RequestMapping("/taoApp")
    public String taoAppIndex( ){
        return "/active/taoApp";
    }



    /**----------------------------------------天天抽奖--------------------------------------------*/

    /**
     * 首页
     * @return
     */
    @GetMapping("/raffleIndex")
    public String raffleIndex( Model model){
        List<ActiveRecord> newRecords=activeService.raffleRecordList("");
        model.addAttribute("newRecords",newRecords);
        return "/active/raffle/index";
    }

    @PostMapping("/prizeList")
    @ResponseBody
    public List<RafflePrize> prizeList(@RequestParam String type) throws Exception{
        return activeService.getPrizeList(Integer.parseInt(type));
    }

    /**
     * 用户中奖记录页
     * @param mobile
     * @return
     */
    @GetMapping("/raffleRecord")
    public String raffleRecord(@RequestParam String mobile , Model model){
        if(!StringUtils.isEmpty(mobile)){
            List<ActiveRecord> recordListByMobile=activeService.raffleRecordList(mobile);
            model.addAttribute("recordListByMobile",recordListByMobile);
        }
        return "/active/raffle/record";
    }

    /**
     * 抽奖实现逻辑
     * @param mobile
     * @return
     */
    @PostMapping("/raffleLogic")
    @ResponseBody
    public Map<String,Object> raffleLogic(@RequestParam String mobile) throws Exception{
        return activeService.raffleLogic(mobile);
    }

//    /**
//     * 获取最新5条中奖记录
//     * @return
//     */
//    @PostMapping("/newRaffleList")
//    @ResponseBody
//    public List<ActiveRecord> newRafflePrizeList(){
//        return activeService.raffleRecordList("");
//    }


}
