/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.ybd.common.tools;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ybd.common.App;
import com.ybd.common.ICallback;
import com.ybd.yl.R;

/**
 * 
 * @author cyf
 * @version $Id: DialogUtil.java, v 0.1 Jan 3, 2013 3:49:04 PM cyf Exp $
 */
public class DialogUtil {

    /**
     * 在屏幕正中弹出一个提示框
     * @param cxt
     * 一般在activity中传递this
     * @param msg
     * 提示框中显示的信息
     */
    public static void toast(Context ctx, String msg) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View toastView = inflater.inflate(R.layout.toast, null);
        TextView toastTV = (TextView) toastView.findViewById(R.id.tv_toast);
        toastTV.setText(msg);
        Toast tost = new Toast(ctx);
        tost.setView(toastView);
        tost.setDuration(Toast.LENGTH_SHORT);
        tost.setGravity(Gravity.CENTER, 0, 0);
        tost.show();
    }

    /**
     * 弹出一个确认消息框
     * 
     * @param ctx
     *            一般在activity中传递this
     * @param msg
     *            消息框上显示的消息
     * @param callback
     *            用户点击确认消息框上的取消时，触发的回调动作
     */
    public static void cancel(final Context ctx, String msg) {
        Builder builder = new Builder(ctx).setTitle(App.INFO);
        builder.setMessage(msg);
        Dialog dialog = builder.setPositiveButton(App.OK,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            }).create();
        dialog.show();
    }

    /**
     * 弹出一个确认消息框
     * 
     * @param ctx
     *            一般在activity中传递this
     * @param msg
     *            消息框上显示的消息
     */
    public static void confirm(final Context ctx, String msg) {
        confirm(ctx, msg, null, null);
    }

    /**
     * 弹出一个确认消息框
     * 
     * @param ctx
     *            一般在activity中传递this
     * @param msg
     *            消息框上显示的消息
     */
    public static void confirm(final Context ctx, String msg, final ICallback callback) {
        confirm(ctx, msg, callback, null);
    }

    /**
     * 弹出一个确认消息框
     * 
     * @param ctx
     *            一般在activity中传递this
     * @param msg
     *            消息框上显示的消息
     * @param callback
     *            用户点击确认消息框上的取消时，触发的回调动作
     */
    public static void confirm(final Context ctx, String msg, final ICallback callback,
                               final ICallback cancelCallback) {
        Builder builder = new Builder(ctx).setTitle(App.INFO);
        builder.setMessage(msg);
        Dialog dialog = builder.setPositiveButton(App.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (callback != null) {
                    callback.callback(ctx);
                }
            }
        }).setNegativeButton(App.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (cancelCallback != null) {
                    cancelCallback.callback(ctx);
                }
            }
        }).create();
        dialog.show();
    }

    /**
     * 只有一个确定按扭的对话框
     * @param ctx
     * @param msg
     * @param callback
     */
    public static void confirm_ok(final Context ctx, String msg, final ICallback callback) {
        Builder builder = new Builder(ctx).setTitle(App.INFO);
        builder.setMessage(msg);
        Dialog dialog = builder.setPositiveButton(App.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (callback != null) {
                    callback.callback(ctx);
                }
            }
        }).create();
        dialog.show();
    }

    /**
     * 弹出一个带取消按钮的提示框
     * 
     * @param cxt
     *            一般在activity中传递this
     * @param msg
     *            提示框中显示的信息
     * @return 提示框对象
     */
    public static ProgressDialog show(final Context obj, String msg) {
        return show(obj, msg, null);
    }

    /**
     * 弹出一个带取消按钮的提示框
     * 
     * @param cxt
     *            一般在activity中传递this
     * @param msg
     *            提示框中显示的信息
     * @param async
     *            请求服务器的后台线程
     * @return 提示框对象
     */
    public static ProgressDialog show(final Context obj, String msg,
                                      final AsyncTask<byte[], Integer, String> async) {
        ProgressDialog dialog = new ProgressDialog(obj);
        dialog.setTitle(App.INFO);
        // dialog.setIcon(R.drawable.putong);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(false);
        dialog.setButton(App.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                if (null != async) {
                    async.cancel(true);
                }
                dialog.cancel();
            }
        });

        dialog.show();
        return dialog;
    }

    /**
    * 创建一个带取消按钮的提示框，不弹出
    * 
    * @param cxt
    *            一般在activity中传递this
    * @param msg
    *            提示框中显示的信息
    * @return 提示框对象
    */
    public static ProgressDialog create(final Context obj, String msg) {
        //        ProgressDialog dialog = new MyDialog(obj);
        ProgressDialog dialog = new ProgressDialog(obj);
        dialog.setTitle(App.INFO);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.setButton(App.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    /**
       * 创建一个带取消按钮的提示框，不弹出
       * 
       * @param cxt
       *            一般在activity中传递this
       * @param msg
       *            提示框中显示的信息
       * @return 提示框对象
       */
    public static ProgressDialog progress(final Context obj, String msg) {
        ProgressDialog dialog = new ProgressDialog(obj);
        dialog.setTitle(App.INFO);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(false);
        dialog.setButton(App.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        return dialog;
    }
}
