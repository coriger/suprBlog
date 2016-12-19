package com.supr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * desc:    时间相关处理工具类
 * author:  ljt
 * date:    2016-07-18 19:17
 */
public class DateUtil {

    /**
     * 根据时间判断星期
     * @param time
     * @return
     */
    public static int getWeek(long time){
        int[] weekOfDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 获取当前时间在一天中的第几分钟
     * @return
     */
	public static int getCurrentMin() {
		int min = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		min = h * 60 + m;
		return min;
	}
	
	/**
     * 获取当前时间的小时
     * @return
     */
	public static String getCurrentHour() {
		SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
		String hour = hourSdf.format(new Date(System.currentTimeMillis()));
		return hour;
	}
    
    public static void main(String[] args){
//        System.out.println(DateUtil.getWeek("2016-07-18"));
//    	System.out.println(isYesterday("2016-11-15 08:32:00"));
    	System.out.println(getDate(parseLong("20161117")));
    }

    /**
     * 根据日期获取小时
     * @param time
     * @return
     */
    public static int getHour(long time) {
        SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
        String hour = hourSdf.format(new Date(time));
        return Integer.parseInt(hour);
    }

    public static String getMin(long time) {
        SimpleDateFormat hourSdf = new SimpleDateFormat("HH:mm");
        return hourSdf.format(new Date(time));
    }
    
    public static String getDate(long time){
    	SimpleDateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateDf.format(new Date(time));
        return date;
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateSdf.format(new Date());
    }

    public static String parseTime(long time) {
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateSdf.format(new Date(time));
    }

    public static String getDay(String orderTime) {
        orderTime = orderTime.replace("-","");
        return orderTime.substring(0,8);
    }

    public static String getSimpleDay(String orderTime) {
        return orderTime.substring(0,10);
    }

    public static String getHour(String orderTime) {
        orderTime = orderTime.replace("-","");
        return orderTime.substring(9,11);
    }

    /**
     * 判断与当前时间相比 是否在24小时之内
     * @param time
     * @return
     */
    public static boolean isIn24Hour(long time) {
        long current = System.currentTimeMillis();
        if(current - time > 1000*60*60*24){
            return false;
        }
        return true;
    }
    
    /**
     * 判断于当前时间相比,是否在指定小时之内
     * @param time
     * @param hour
     * @return
     */
    public static boolean isInHours(long time,int hour) {
        long current = System.currentTimeMillis();
        if(current - time > 1000*60*60*hour){
            return false;
        }
        return true;
    }

    public static long parseLong(String orderTime) {
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateSdf.parse(orderTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取指定日期的差值日期
     * @param timeStr
     * @param day
     * @return
     */
    public static String getOneDay(String timeStr , int day){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//    	Calendar cal = Calendar.getInstance();
//      cal.add(Calendar.DATE, day);
//    	return df.format(new Date(parseLong(timeStr)));
    	
    	long oneDay = 1000 * 60 * 60 * 24;
    	Date time = null;
		try {
			time = df.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String reportTime = df.format(new Date(time.getTime() + oneDay * day));
		return reportTime;
    }
    
    /**
     * 获取上一个月
     */
    public static String getBeforeMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        return df.format(c.getTime());
    }

    public static String getBeforeDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        return df.format(c.getTime());
    }

    public static String getSimpleBeforeDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        return df.format(c.getTime());
    }

    /**
     * 获取当天零点的时间
     * @return
     */
    public static long getCurrentLongTime() {
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date();
        String str =dateSdf.format(date);
        return parseLong(str);
    }

    public static String getDay() {
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateSdf.format(date);
    }
    
    /**
     * 判断当前时间日期是否是今天
     * @param time
     */
    public static boolean isToday(String time){
    	boolean flag = false;
    	time = getDay(time);
    	String today = getDay();
    	flag = time.equals(today);
    	return flag;
    }
    
    /**
     * 判断当前时间日期是否是今天
     * @param time
     */
    public static boolean isYesterday(String time){
    	boolean flag = false;
    	time = getDay(time);
    	String yesterday = getBeforeDay();
    	flag = time.equals(yesterday);
    	return flag;
    }
}
