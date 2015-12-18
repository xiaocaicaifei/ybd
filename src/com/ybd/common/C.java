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
    /** ListViewRun设置的最大高度，一般不用修改 **/
    public static int     LISTVIEWH = 2000;
    
    public static final String APP_ID = "wxd930ea5d5a258f4f";//测试例子的appID
    
//    public static final String WX_APP_SECRET = "wxd930ea5d5a258f4f";

    public static String  IP        = "http://192.168.199.202:8080/"; // 毛哥本机
//    public static String  IP        = "http://192.168.1.100:8080/"; // 毛哥本机
//    public static String  IP        = "http://120.24.175.13:8080/"; // 外网服务器

}
