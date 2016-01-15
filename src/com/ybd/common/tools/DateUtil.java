package com.ybd.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ybd.common.L;

public class DateUtil {
    
    public static String getTimeFormat(String format,String time){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2=new SimpleDateFormat(format);
            return sdf2.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            L.v("解析"+time+"出错");
            return "";
        }
    }
    public static String getTimeFormat(String format,Date time){
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2=new SimpleDateFormat(format);
        return sdf2.format(time);
    }
}
