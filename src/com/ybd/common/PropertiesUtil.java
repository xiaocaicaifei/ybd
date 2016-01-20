/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * 配置属性的工具类，可以简单的放一些软件配置信息
 * 
 * @author cyf
 * @version $Id: PropertiesUtil.java, v 0.1 2011-6-29 上午04:03:07 cyf Exp $
 */
public class PropertiesUtil {

    /**系统配置的名称**/
    private static final String NAME   = "appYbd";
    /**用户的ID **/
    public static final String USERID="userId";
    /**用户头像的地址 **/
    public static final String HEADIMGURL="headimgurl";
    /**用户的昵称 **/
    public static final String NICKNAME="nickname";
    /** 用户登录的类型*/
    public static final String LOGINTYPE="logintype";//1手机号登录；2微博登录；3微信登录
    /** 用户名 */
    public static final String ACCOUNT="account";
    /** 密码*/
    public static final String PASSWORD="password";
    /** 登录用户的void 用户容联云聊天*/
    public static final String VOIPACCOUNT="voipaccount";
    /**直接上拍上拍*/
    public static final String SCSP="scsp";
    /**起拍价*/
    public static final String QPJ="qpj";
    
    /** 是否是第一次运行项目*/
    public static final String ISFIRST="isfirst";
    /**
     * 根据key读取value
     * 
     * @param key 
     * @return value
     */
    public static String read(Context ctx, String key) {

        try {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, "");
        } catch (Exception e) {
            Log.e("PropertiesUtil:", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 写入配置信息
     * 
     * @param ctx 上下文信息
     * @param key 信息的key
     * @param value 信息的value
     */
    public static void write(Context ctx, String key, String value) {
        try {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
            //获取编辑器
            Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            //提交修改
            editor.commit();
        } catch (Exception e) {
            Log.e("PropertiesUtil:", e.getMessage(), e);
        }
    }

    /**
     * 清除配置信息
     * 
     * @param ctx 上下文信息
     * @param key 信息的key
     */
    public static void remove(Context ctx, String key) {
        try {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
            //获取编辑器
            Editor editor = sharedPreferences.edit();
            editor.remove(key);
            //提交修改
            editor.commit();
        } catch (Exception e) {
            Log.e("PropertiesUtil:", e.getMessage(), e);
        }
    }

}
