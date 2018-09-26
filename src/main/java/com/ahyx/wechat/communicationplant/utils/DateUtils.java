package com.ahyx.wechat.communicationplant.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: daimengying
 * @Date: 2018/9/26 11:15
 * @Description:日期
 */
public class DateUtils {
    static  final  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获取当前时间后一天日期
     * @param date
     * @return
     */
    public static  String getNextDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前时间前一天日期
     * @param date
     * @return
     */
    public static  String getLastDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(calendar.getTime());
    }

}
