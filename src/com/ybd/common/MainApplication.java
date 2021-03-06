/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.ybd.common;

import io.rong.imlib.RongIMClient;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ybd.yl.DemoContext;
import com.ybd.yl.R;

/**
 * 用来初始化一些全局参数
 * 在这里主要用于初始化显示控件的一些东西
 * @author cyf
 * @version $Id: MainApplication.java, v 0.1 2015-11-2 上午10:20:20 cyf Exp $
 */
public class MainApplication extends Application {

    private static DisplayImageOptions defaultOptions;//默认的显示样式，
    private static DisplayImageOptions roundOptions;//圆形的显示器
    private static DisplayImageOptions roundOffOptions;//倒圆角的显示器
    
	@Override
    public void onCreate() {
        super.onCreate();
        //下面是初始化ImageLoader控件
        Context context = getApplicationContext();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
            .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
            .diskCacheFileNameGenerator(new Md5FileNameGenerator())
            .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

        //设置图片加载的属性
        Builder b = new DisplayImageOptions.Builder();
                b.showImageForEmptyUri(R.drawable.tp_jzsb);
                b.showImageOnFail(R.drawable.tp_jzsb);
                b.showImageOnLoading(R.drawable.tp_jzsb);
                b.resetViewBeforeLoading(Boolean.FALSE);
        b.cacheOnDisk(Boolean.FALSE);
        b.cacheInMemory(Boolean.TRUE);
        b.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
        defaultOptions = b.bitmapConfig(Bitmap.Config.RGB_565).build();
        
        roundOptions=b.displayer(new CircleBitmapDisplayer()).build();
        
        roundOffOptions=b.displayer(new RoundedBitmapDisplayer(8)).build();

        //        initEngineManager(this);
        
        //容云初始化
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIMClient.init(this);

            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

                DemoContext.init(this);
            }
        }
    }
	
	/**
     * 获得当前进程的名字（荣云使用）
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
    public static DisplayImageOptions getOptions() {
        return defaultOptions;
    }
    
    public static DisplayImageOptions getRoundOptions(){
        return roundOptions;
    }
    
    public static DisplayImageOptions getRoundOffOptions(){
        return roundOffOptions;
    }
}
