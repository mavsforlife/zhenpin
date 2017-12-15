package com.cang.zhenpin.zhenpincang.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by victor on 2017/12/2.
 * Email: wwmdirk@gmail.com
 */

public class TimeUtil {

    //时间格式转换
    public static String timeChange(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String str = format1.format(date);
        return str;
    }

    public static long timeDifference(String nowtime, String endtime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            //系统时间转化为Date形式
            Date dstart = format.parse(nowtime);
            //活动结束时间转化为Date形式
            Date dend = format.parse(endtime);
            //算出时间差，用ms表示
            diff = dend.getTime() - dstart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //返回时间差
        return diff;
    }

    public static long timeToNow(String startTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        long diff = 0;
        try {
            //系统时间转化为Date形式
            Date dstart = format.parse(startTime);
            //算出时间差，用ms表示
            diff = System.currentTimeMillis() - dstart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //返回时间差
        return diff;
    }

    public static long timeToEnd(String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        long diff = 0;
        try {
            //系统时间转化为Date形式
            Date dEnd = format.parse(endTime);
            //算出时间差，用ms表示
            diff = dEnd.getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //返回时间差
        return diff;
    }
}
