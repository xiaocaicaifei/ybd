/**
// * hnjz.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.ybd.common;

/**
 * 配置项
 * 
 * @author cyf
 * @version $Id: C.java, v 0.1 Jan 3, 2013 10:21:43 AM cyf Exp $
 */
public class C {
    /** debug级别是否开启 */
    public static boolean isDebug   = true;
    /** http的编码方式 **/
    public static String  CHARSET   = "utf-8";
    /** 分页的大小 */
    public static int     PAGE_SIZE = 5;
    /** 分页的大小（查询用户的时候每页的大小） */
    public static int     PAGE_SIZE_USER = 10;
    /** ListViewRun设置的最大高度，一般不用修改 **/
    public static int     LISTVIEWH = 2000;
    
    public static final String APP_ID = "wxd930ea5d5a258f4f";//测试例子的appID
    
//    public static final String YTX_APPKEY="8a48b55150e162370150e9f58f1e35d7";//容联云的appkey
//    public static final String YTX_TOKEN="49c09bbdac011000b2f343c587337850";//容联云的token
  public static final String RY_APPKEY="8a48b55150e162370150e9f58f1e35d7";//容云的appkey
  public static final String RY_TOKEN="49c09bbdac011000b2f343c587337850";//容云的token
    
//    public static final String WX_APP_SECRET = "wxd930ea5d5a258f4f";

    public static String  IP        = "http://192.168.199.192:8080/"; // 毛哥本机
//    public static String  IP        = "http://120.24.175.13:8080/"; // 外网服务器

}
