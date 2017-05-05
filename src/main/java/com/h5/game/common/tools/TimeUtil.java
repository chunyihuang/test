package com.h5.game.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class TimeUtil {
    public static final String DATA_FORMAT = "yyyy-MM-dd";
    public static final String DATA_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化开始日期
     * 日期格式化 返回 yyyy-MM-dd hh:mm:ss 2016-08-20 00:00:00
     *@param
     *@author dengxq
     *@return
     *@throws
     *@version 1.0
     */
    public static Date dateFormatBegin(Date beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT_TIME);
        Calendar calendar = Calendar.getInstance();
        try{
            calendar.setTime(beginDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            String time = sdf.format(calendar.getTime());
            return sdf.parse(time);
        } catch (ParseException e) {
        }
        return calendar.getTime();
    }

    /**
     * 格式化结束日期
     * 日期格式化 返回yyyy-MM-dd hh:mm:ss 2016-08-20 23:59:59
     *@param
     *@author dengxq
     *@return
     *@throws
     *@version 1.0
     */
    public static Date dateFormatEnd(Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT_TIME);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            String time = sdf.format(calendar.getTime());
            sdf.parse(time);
        } catch (ParseException e) {
        }
        return calendar.getTime();
    }

    /**
     * 字符串转日期
     *@param  strDate 字符串日期格式yyyy-MM-dd
     *@author dengxq
     *@return 转换失败返回null
     *@throws
     *@version 1.0
     */
    public static Date stringToDate(String strDate){
        try {
            SimpleDateFormat sdf =  new SimpleDateFormat(DATA_FORMAT);
            return sdf.parse(strDate);
        }catch (ParseException e) {
        }
        return null;
    }

}
