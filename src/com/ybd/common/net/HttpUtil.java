/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common.net;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;

import com.ybd.common.App;

/**
 * 与服务器交互的http请求类
 * 
 * @author cyf
 * @version $Id: HttpUtil.java, v 0.1 2011-8-24 上午02:18:42 cyf Exp $
 */
public class HttpUtil {

    private static HttpClient customerHttpClient;

    public static HttpClient getHttpClient(int sotimeout, boolean create) {
        if (create) {
            HttpParams params = new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params, true);
            // 超时设置
            /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 5000);
            //最大连接数
            ConnManagerParams.setMaxTotalConnections(params, 20);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, sotimeout);

            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            // 使用线程安全的连接管理来创建HttpClient
            ThreadSafeClientConnManager conMgr = new ThreadSafeClientConnManager(params, schReg);
            return new DefaultHttpClient(conMgr, params);
        } else if (null == customerHttpClient) {
            HttpParams params = new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params, true);
            // 超时设置
            /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 5000);
            //最大连接数
            ConnManagerParams.setMaxTotalConnections(params, 20);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 6000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, sotimeout);

            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            // 使用线程安全的连接管理来创建HttpClient
            ThreadSafeClientConnManager conMgr = new ThreadSafeClientConnManager(params, schReg);
            customerHttpClient = new DefaultHttpClient(conMgr, params);
        }
        return customerHttpClient;
    }

    /**
     * 弹出一个确认消息框
     * 
     * @param ctx 一般在activity中传递this
     * @param msg   消息框上显示的消息
     * @param callback
     *            用户点击确认消息框上的取消时，触发的回调动作
     */
    public static void noNet(final Context ctx, String msg) {
        Builder builder = new Builder(ctx).setTitle(App.INFO);
        builder.setMessage(msg);
        Dialog dialog = builder.setPositiveButton(App.SZ, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                ctx.startActivity(intent);
            }
        }).setNegativeButton(App.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create();
        dialog.show();
    }

    /**
     * 弹出一个确认消息框
     * 
     * @param ctx 一般在activity中传递this
     * @param msg   消息框上显示的消息
     * @param callback
     *            用户点击确认消息框上的取消时，触发的回调动作
     */
    public static void serverError(final Context ctx, String msg) {
        Builder builder = new Builder(ctx).setTitle(App.INFO);
        builder.setMessage(msg);
        Dialog dialog = builder.setPositiveButton(App.SZ, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent();
                intent.setClass(ctx, Configuration.class);
                ctx.startActivity(intent);
            }
        }).setNegativeButton(App.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create();
        dialog.show();
    }

    /**
     * 弹出一个确认消息框(确定和取消按钮)
     * 
     * @param ctx 一般在activity中传递this
     * @param msg   消息框上显示的消息
     * @param callback
     *            用户点击确认消息框上的取消时，触发的回调动作
     */
    public static void serverTimeout(final Context ctx, String msg) {
        Builder builder = new Builder(ctx).setTitle(App.INFO);
        builder.setMessage(msg);
        Dialog dialog = builder.setPositiveButton(App.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNegativeButton(App.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create();
        dialog.show();
    }

}
