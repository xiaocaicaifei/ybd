package com.ybd.common;

import android.content.Context;
import android.content.Intent;

/**
 * 代参数的回调
 * @author zn
 * @version $Id: ICallbackPara.java, v 0.1 2013-5-20 上午12:52:13 zn Exp $
 */
public interface ICallbackPara {
	void callback(Context ctx, Intent intent);
}
