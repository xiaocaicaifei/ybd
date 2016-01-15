/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 广播的处理的 类
 * 
 * @author cyf
 * @version $Id: PropertiesUtil.java, v 0.1 2011-6-29 上午04:03:07 cyf Exp $
 */
public class BroadcaseUtil {

    /**圈子上传成功**/
    public static final String QZ_SCCG="qz_sccg";
    /** 议论-上传成功*/
    public static final String YL_SCCG="yl_sccg";
    /** 加一口成功 */
    public static final String PM_JYK_SUCCESS="pm_jyk_success";
    /**接收聊天消息的广播*/
    public static final String XX_LT="xx_lt";
    /**
     * 根据不同的名字发送广播
     */
    public static void sendBroadcase(String name,Activity activity){
        Intent intent=new Intent(name);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }
    /**
     * 根据不同的名字发送广播
     */
    public static void sendBroadcase(String name,Context activity,String content){
        Intent intent=new Intent(name);
        intent.putExtra("content", content);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }
    
    /**
     * 注册广播
     */
    public static void registBroadcase(Activity activity,BroadcastReceiver receiver,String name) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(name);
        broadcastManager.registerReceiver(receiver, filter);
    }

}
