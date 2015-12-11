/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common.net;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.ybd.common.App;

/**
 * 当一次向服务器发起多次请求时，在第一个请求时弹出对话框，最后一个请求关闭对话框
 * 
 * @author cyf
 * @version $Id: DialogControl.java, v 0.1 2011-8-25 上午10:04:39 cyf Exp $
 */
public class DialogControl {

    /**总数目*/
    private int                      totleNum;
    /**当前第X个请求*/
    private int                      curReqNum;
    /**已经完成的数目*/
    private int                      doneNum;
    /** 提示信息框，用户提交时弹出，服务器响应后消失 */
    private ProgressDialog           dialog;
    /** 任务列表*/
    private List<AsyncTask<?, ?, ?>> tasks = new ArrayList<AsyncTask<?, ?, ?>>();

    /**
     * 构造函数
     * 
     * @param totleNum {@link DialogControl#totleNum}
     */
    public DialogControl(int totleNum, ProgressDialog dialog) {
        this.totleNum = totleNum;
        this.doneNum = 0;
        this.curReqNum = 1;
        this.dialog = dialog;
    }

    /**
     * 弹出对话框
     * 
     */
    public void show(final AsyncTask<?, ?, ?> task) {
        tasks.add(task);
        if (curReqNum == 1 && null != dialog) {
            dialog.show();
            dialog.setButton(App.CANCEL, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {
                    for (AsyncTask<?, ?, ?> ele : tasks) {
                        ele.cancel(true);
                    }
                    dialog.cancel();
                }
            });
        }
    }
    
    /**
     * 弹出对话框
     * 
     */
    public void showProgress(final AsyncTask<?, ?, ?> task) {
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        show(task);
    }


    public void setProgress(long progress) {
        dialog.setProgress((int) progress);
    }

    public void setMax(long max) {
        dialog.setMax((int) max);
    }

    /**
     * 请求全部完成，关闭对话框
     */
    public synchronized void done() {
        doneNum = doneNum + 1;
        if (totleNum == doneNum && null != dialog) {
            dialog.cancel();
        }
    }

    /**
     * Getter method for property <tt>totleNum</tt>.
     * 
     * @return property value of totleNum
     */
    public int getTotleNum() {
        return totleNum;
    }

    /**
     * Setter method for property <tt>totleNum</tt>.
     * 
     * @param totleNum value to be assigned to property totleNum
     */
    public void setTotleNum(int totleNum) {
        this.totleNum = totleNum;
    }

    /**
     * Getter method for property <tt>curReqNum</tt>.
     * 
     * @return property value of curReqNum
     */
    public int getCurReqNum() {
        return curReqNum;
    }

    /**
     * Setter method for property <tt>curReqNum</tt>.
     * 
     * @param curReqNum value to be assigned to property curReqNum
     */
    public void setCurReqNum(int curReqNum) {
        this.curReqNum = curReqNum;
    }

    /**
     * Getter method for property <tt>doneNum</tt>.
     * 
     * @return property value of doneNum
     */
    public int getDoneNum() {
        return doneNum;
    }

    /**
     * Setter method for property <tt>doneNum</tt>.
     * 
     * @param doneNum value to be assigned to property doneNum
     */
    public void setDoneNum(int doneNum) {
        this.doneNum = doneNum;
    }

}
