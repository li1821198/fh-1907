package com.fh.shop.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static  final  String  Y_M_D="yyyy-MM-dd";
    public static  final  String  FULL_TIME="yyyy-MM-dd HH:mm:ss";
    public static  final  String  FULL_TIMEINFO="yyyyMMddHHmmss";

      public static String addMinutes(Date date ,int minute,String pattern){
          Calendar instance = Calendar.getInstance();
          instance.setTime(date);
          instance.add(Calendar.MINUTE,minute);
          Date time = instance.getTime();
          return date2str(time,pattern);

      }
    /*日期date转String*/
    public static  String date2str(Date  date,String patten){
        if(date==null){
           return  "";
        }
        SimpleDateFormat  sim=new SimpleDateFormat(patten);
        return sim.format(date);
    }
   /*日期String转Date*/
     public  static  Date str2date(String date,String patten){
         if (StringUtils.isEmpty(date)){
             return  null;
         }
         SimpleDateFormat sim = new SimpleDateFormat(patten);
         Date date1 = null;
         try {
             date1 =sim.parse(date);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return date1;
     }

}
