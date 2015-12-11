/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common;

import android.util.Log;

/**
 * 日志管理
 * 
 * @author cyf
 * @version $Id: L.java, v 0.1 2011-6-8 上午06:21:04 cyf Exp $
 */
public class L {
    private static boolean showLog=true;//是否显示log信息,开放完成后，关闭改选项
    /**
     * 打印debug级别的日志
     * 
     * @param tag 日志打印所在的类名,或者方便查看日志的一个标记
     * @param objs 日志信息，字符串有","隔开即可
     */
    public static void d(String tag, Object... objs) {
        if (C.isDebug) {
            StringBuilder str = new StringBuilder();
            str.append(tag).append(":");
            for (Object ele : objs) {
                str.append(ele);
            }
            Log.d("hb", str.toString());
        }
    }

    /**
     * 打印error级别的日志
     * 
     * @param tag 日志打印所在的类名,或者方便查看日志的一个标记
     * @param msg 错误信息
     */
    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    /**
     * 打印error级别的日志,将整个异常栈打印出来
     * 
     * @param tag 日志打印所在的类名,或者方便查看日志的一个标记
     * @param e 异常信息
     */
    public static void e(String tag, Exception e) {
        Log.e(tag, e.getMessage(), e);
    }
    
    /**
     * 常用的输出日志的打印提示
     */
    public static void v(String msg){
        if(showLog){
            Log.v("dddd", msg);
        }
    }

}
