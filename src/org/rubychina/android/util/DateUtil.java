/*Copyright (C) 2012 Longerian (http://www.longerian.me)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package org.rubychina.android.util;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/** 
     * 获取日期中的年 
     * @param date 日期 
     * @return 年份 
     */ 
    public static String getYear(Date date){ 
       DateFormat fYear = new SimpleDateFormat("yyyy"); 
       return fYear.format(date).toString(); 
    } 

    /** 
     * 获取日期中的月 
     * @param date 日期 
     * @return 月份 
     */ 
    public static String getMonth(Date date){ 
        DateFormat fMonth=new SimpleDateFormat("MM"); 
        return fMonth.format(date).toString(); 
    } 

    /** 
     * 获取日期中天 
     * @param date 日期 
     * @return 天 
     */ 
    public static String getDay(Date date){ 
        DateFormat fDay=new SimpleDateFormat("dd"); 
        return fDay.format(date).toString(); 
    } 

    /** 
     * 获取日期中的小时
     * @param date 日期 
     * @return 时间 
     */ 
    public static String getHour(Date date){ 
        DateFormat fHour=new SimpleDateFormat("HH"); 
        return fHour.format(date).toString(); 
    } 
    
    /** 
     * 获取日期中的小时
     * @param date 日期 
     * @return 时间 
     */ 
    public static String getMinute(Date date){ 
    	DateFormat fMinute=new SimpleDateFormat("mm"); 
    	return fMinute.format(date).toString(); 
    } 
    
    /** 
     * 获取日期中的小时
     * @param date 日期 
     * @return 时间 
     */ 
    public static String getSecond(Date date){ 
    	DateFormat fSecond=new SimpleDateFormat("ss"); 
    	return fSecond.format(date).toString(); 
    } 

    public static int compareYear(long endTimeInMillis, long startTimeInMillis) {
    	if(endTimeInMillis < startTimeInMillis) {
    		throw new InvalidParameterException("endTimeInMillis must >= startTimeInMillis");
    	}
    	Date delta = new Date(endTimeInMillis - startTimeInMillis);
		int year = Integer.valueOf(getYear(delta));
		return year - 1970;
    }
    public static int compareMonth(long endTimeInMillis, long startTimeInMillis) {
    	if(endTimeInMillis < startTimeInMillis) {
    		throw new InvalidParameterException("endTimeInMillis must >= startTimeInMillis");
    	}
    	Date delta = new Date(endTimeInMillis - startTimeInMillis);
    	int month = Integer.valueOf(getMonth(delta));
    	return month - 1;
    }
    public static int compareDay(long endTimeInMillis, long startTimeInMillis) {
    	if(endTimeInMillis < startTimeInMillis) {
    		throw new InvalidParameterException("endTimeInMillis must >= startTimeInMillis");
    	}
    	Date delta = new Date(endTimeInMillis - startTimeInMillis);
    	int day = Integer.valueOf(getDay(delta));
    	return day - 1;
    }
    public static int compareHour(long endTimeInMillis, long startTimeInMillis) {
    	if(endTimeInMillis < startTimeInMillis) {
    		throw new InvalidParameterException("endTimeInMillis must >= startTimeInMillis");
    	}
    	Date delta = new Date(endTimeInMillis - startTimeInMillis);
    	int hour = Integer.valueOf(getHour(delta));
    	return hour - 0 - 8; //中国时区
    }
    public static int compareMinute(long endTimeInMillis, long startTimeInMillis) {
    	if(endTimeInMillis < startTimeInMillis) {
    		throw new InvalidParameterException("endTimeInMillis must >= startTimeInMillis");
    	}
    	Date delta = new Date(endTimeInMillis - startTimeInMillis);
    	int minute = Integer.valueOf(getMinute(delta));
    	return minute - 0;
    }
    public static int compareSecond(long endTimeInMillis, long startTimeInMillis) {
    	if(endTimeInMillis < startTimeInMillis) {
    		throw new InvalidParameterException("endTimeInMillis must >= startTimeInMillis");
    	}
    	Date delta = new Date(endTimeInMillis - startTimeInMillis);
    	int second = Integer.valueOf(getSecond(delta));
    	return second - 0;
    }
    
}
