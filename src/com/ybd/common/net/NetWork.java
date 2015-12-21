/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common.net;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ybd.common.App;
import com.ybd.common.L;
import com.ybd.common.tools.DialogUtil;

/**
 * 处理与服务器进行的数据交互
 * 
 * @author zn
 * @version $Id: NetWork.java, v 0.1 2011-6-8 上午07:34:51 cyf Exp $
 */
public class NetWork {
    /**
     * 判断当前手机是否有网络连接
     * 
     * @param context 上下文信息
     * @return true,有可用的网络连接；false,没有可用的网络连接
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();//得到所有网络连接情况的信息
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {//只要有一种连接状态连接成功，就代表成功
                    /************** 切换内网(正式使用时，去掉注释) ****************/
                    //                     APNUtil apn = new APNUtil((Activity) context);
                    //                     apn.setCurrentApnToZZHBT();
                    /************** 切换内网 ****************/
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 提交数据,可以向服务器多次请求，弹出一个提示框，注意：<br>
     * <li>这几次请求必须是独立的，没有依赖关系
     * 
     * @param ctx  当前acticity
     * @param netWork  数据提交的对象
     */
    public static void submit(Context ctx, INetWork... netWork) {
        //如果手机中没有可用的连接，需要在设置--无线和网络中设置
        if (!NetWork.isNetworkAvailable(ctx)) {
            HttpUtil.noNet(ctx, App.NET_NOUSE);
            return;
        }
        ProgressDialog dialog = DialogUtil.create(ctx, App.LOADATA);
        try {
            DialogControl con = new DialogControl(netWork.length, dialog);
            for (int i = 0; i < netWork.length; i++) {
                INetWork ele = netWork[i];
                L.d("submit", netWork.getClass());
                con.setCurReqNum(i + 1);
                UploadData up = new UploadData(ctx, App.LOADATA, ele, con, 25000, false);
                up.execute();
            }
        } catch (Exception e) {
            //发生异常时关掉提示框
            L.e("NetWork", e);
            dialog.cancel();
        }

    }
    

    /**
     * 提交数据,可以向服务器多次请求，弹出一个提示框，注意：<br>
     * <li>这几次请求必须是独立的，没有依赖关系
     * 
     * @param ctx  当前acticity
     * @param netWork  数据提交的对象
     * @param isShowProgress 是否显示滚动条
     */
    public static void submit(Context ctx, boolean isShowProgress, INetWork... netWork) {
        //如果手机中没有可用的连接，需要在设置--无线和网络中设置
        if (!NetWork.isNetworkAvailable(ctx)) {
            HttpUtil.noNet(ctx, App.NET_NOUSE);
            return;
        }
        ProgressDialog dialog = DialogUtil.create(ctx, App.LOADATA);
        try {
            DialogControl con = new DialogControl(netWork.length, dialog);
            for (int i = 0; i < netWork.length; i++) {
                INetWork ele = netWork[i];
                L.d("submit", netWork.getClass());
                con.setCurReqNum(i + 1);
                UploadData up = new UploadData(ctx, App.LOADATA, ele, con, 25000, false,
                    !isShowProgress);
                up.execute();
            }
        } catch (Exception e) {
            //发生异常时关掉提示框
            L.e("NetWork", e);
            dialog.cancel();
        }

    }

    /**
     * 提交数据,UI中直接调用此方法,提供一个默认的提示信息{@link App#LOADATA}
     * 
     * @param ctx  当前acticity
     * @param netWork  数据提交的对象
     */
    public static void submit(Context ctx, INetWork netWork) {
        NetWork.submit(ctx, App.SUBMIT, netWork);
    }

    /**
     * 提交数据,UI中直接调用此方法,提供一个默认的提示信息{@link App#LOADATA}
     * 不弹出提示框
     * 
     * @param ctx  当前acticity
     * @param netWork  数据提交的对象
     */
    public static void submitNoDialog(Context ctx, INetWork netWork) {
        try {
            //如果手机中没有可用的连接，需要在设置--无线和网络中设置
            if (!NetWork.isNetworkAvailable(ctx)) {
                HttpUtil.noNet(ctx, App.NET_NOUSE);
                return;
            }
            DialogControl dialogControl = new DialogControl(1, null);
            UploadData up = new UploadData(ctx, null, netWork, dialogControl, 25000, false);
            up.execute();
        } catch (Exception e) {
            L.e("NetWork", e);
        }
    }

    /**
     * 提交数据,UI中直接调用此方法
     * 
     * @param ctx  当前acticity
     * @param loadMsg  查询数据时的提示信息
     * @param netWork  数据提交的对象
     */
    public static void submit(Context ctx, String loadMsg, INetWork netWork) {
        try {
            //如果手机中没有可用的连接，需要在设置--无线和网络中设置
            if (!NetWork.isNetworkAvailable(ctx)) {
                HttpUtil.noNet(ctx, App.NET_NOUSE);
                return;
            }

            DialogControl dialogControl = null;
            UploadData up = null;
            dialogControl = new DialogControl(1, DialogUtil.create(ctx, loadMsg));
            if (null != netWork.getSubmitData().getPath()) {
                up = new UploadData(ctx, loadMsg, netWork, dialogControl, 300000, false);
            } else {
                up = new UploadData(ctx, loadMsg, netWork, dialogControl, 25000, false);
            }

            up.execute();
        } catch (Exception e) {
            L.e("NetWork", e);
        }

    }

    /**
     * 提交数据,{@link Service}中直接调用此方法
     * 
     * @param ctx  当前acticity
     * @param netWork  数据提交的对象
     */
    public static void submitServices(Context ctx, INetWork netWork) {
        try {
            //如果手机中没有可用的连接，需要在设置--无线和网络中设置
            if (!NetWork.isNetworkAvailable(ctx)) {
                HttpUtil.noNet(ctx, App.NET_NOUSE);
                return;
            }
            UploadData up = new UploadData(ctx, null, netWork, null, 25000, false, true);
            up.execute();
        } catch (Exception e) {
            L.e("NetWork", e);
        }

    }

    /**
     * (下载,UI中直接调用此方法)
     * 
     * @param ctx 当前acticity
     * @param url 下载文件的地址
     * @param localPath 可为空，默认FileUtil.getPath()
     * @param fileName 下载后另存的文件名
     */
    //    public static void downloadAndOpen(final Activity ctx, Data url, DirType dir, String fileName) {
    //        downloadAndOpen(ctx, url, dir, fileName, 600000);
    //    }

    /**
     * (下载,UI中直接调用此方法)
     * 
     * @param ctx 当前acticity
     * @param url 下载文件的地址
     * @param localPath 可为空，默认FileUtil.getPath()
     * @param fileName 下载后另存的文件名
     * @param fileName 下载后另存的文件名
     */
    //    public static void downloadAndOpen(final Activity ctx, Data url, DirType dir, String fileName,
    //                                       int timeOut) {
    //        String localPath = FileUtil.getFileSaveDir(dir);
    //        DownLoadFile d = new DownLoadFile(ctx, url, localPath, fileName, true, timeOut);
    //        d.execute();
    //
    //    }

    /**
     * (下载,UI中直接调用此方法)
     * 
     * @param ctx 当前acticity
     * @param url 下载文件的地址
     * @param localPath 可为空，默认FileUtil.getPath()
     * @param fileName 下载后另存的文件名
     */
    //    public static void download(final Activity ctx, Data url, DirType dir, String fileName) {
    //        String localPath = FileUtil.getFileSaveDir(dir);
    //        DownLoadFile d = new DownLoadFile(ctx, url, localPath, fileName, false, 600000);
    //        d.execute();
    //    }

}
