/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common.net;

/**
 * 提交之后返回的值
 * 
 * @author zn
 * @version $Id: INetWork.java, v 0.1 2011-6-8 上午07:31:05 zn Exp $
 */
public interface INetWorkResult {
    /**
     * 处理返回数据
     * 
     * @param result 服务器返回数据
     */
    public void result(Object result)throws Exception;
}
