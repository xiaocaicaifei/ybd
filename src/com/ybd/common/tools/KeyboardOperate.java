package com.ybd.common.tools;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘的操作类
 * 
 * @author cyf
 * @version $Id: KeyboardOperate.java, v 0.1 2015-12-22 下午5:39:24 cyf Exp $
 */
public class KeyboardOperate {

    /**
     * 键盘打开或隐藏的类
     */
    public static void hideOrOpenKeyboard(Activity activity){
        InputMethodManager m = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
            m.toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
