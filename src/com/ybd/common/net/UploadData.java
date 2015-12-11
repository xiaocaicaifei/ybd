/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.ybd.common.net;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.ybd.common.App;
import com.ybd.common.C;
import com.ybd.common.L;
import com.ybd.common.net.CustomMultiPartEntity.ProgressListener;
import com.ybd.common.tools.DialogUtil;
import com.ybd.common.tools.StringUtil;
import com.ybd.yl.MainActivity;

/**
 * 向服务器提交数据<br>
 * AsyncTask能够适当地、简单地用于 UI线程。这个类准许执行后台操作，让那些没有熟练操作线程的操作者在 UI线程上发布结果。
 * 异步任务的定义是一个在后台线程上运行，其结果是在 UI线程上发表的计算<br>
 * 把UI展示作为一个线程，请求数据做为一个线程，2个线程互不影响
 * 
 * @author cyf
 * @version $Id: UploadData.java, v 0.1 2011-6-15 上午09:35:35 cyf Exp $
 */
public class UploadData extends AsyncTask<byte[], Integer, String> {

    /** 上下文信息 */
    private Context             ctx;
    /** 网络连接的工具类 */
    private HttpClient          httpClient;
    /** 服务器提交的处理 */
    private INetWork            netWork;
    /** 提交数据 */
    private Data                toServerData;
    /** {@code Handler} */
    private Handler             handler;
    /** {@link DialogControl} */
    private DialogControl       con;
    /** 是否Service提交的 */
    private boolean             isServices = false;

    /** 服务端返回的用户没有登录的状态码 **/
    private static final String RE_LOGIN   = "911";

    /**
     * 构造函数，向服务器提交多次数据
     * 
     * @param ctx
     *            {@link UploadData#ctx}
     * @param loadMsg
     *            {@link UploadData#loadMsg}
     * @param netWork
     *            {@link UploadData#netWork}
     * @param con
     *            {@link DialogControl}
     */
    public UploadData(Context ctx, String loadMsg, INetWork netWork, DialogControl con,
                      int sotimeout, boolean create) {
        this(ctx, loadMsg, netWork, con, sotimeout, create, false);
    }

    /**
     * 构造函数，向服务器提交多次数据
     * 
     * @param ctx
     *            {@link UploadData#ctx}
     * @param loadMsg
     *            {@link UploadData#loadMsg}
     * @param netWork
     *            {@link UploadData#netWork}
     * @param con
     *            {@link DialogControl}
     * @param sotimeout
     *            超时时间
     * @param create
     *            是否创建 DialogControl
     * @param isServices
     *            是否为{@link Service}提交的，如果是，不弹出Dialog
     */
    public UploadData(Context ctx, String loadMsg, INetWork netWork, DialogControl con,
                      int sotimeout, boolean create, boolean isServices) {

        // 如果校验没有通过，不继续执行
        if (!netWork.validate()) {
            L.d("netWork:", netWork.getClass());
            return;
        }
        try {
            toServerData = netWork.getSubmitData();
            L.d("toServerData", toServerData.getUrl());
        } catch (Exception e) {
            L.e("NetWork", e);
            DialogUtil.toast(ctx, "netWork.getSubmitData()执行错误，请检查程序");
            return;
        }
        // 获取参数为空
        if (null == toServerData) {
            DialogUtil.toast(ctx, "netWork.getSubmitData()执行为空，请检查程序");
            return;
        }
        this.httpClient = HttpUtil.getHttpClient(sotimeout, create);
        this.handler = new Handler();
        this.ctx = ctx;
        this.netWork = netWork;
        this.isServices = isServices;
        this.con = con;
        if (!isServices) {
            if (null == toServerData.getPath()) {
                this.con.show(this);
            } else {
                this.con.showProgress(this);
            }
        }

    }

    /** 触发当前 AsyncTask 
     * 代码中手动触发
     * 第一个被执行
     * */
    public void execute() {
        if (null != toServerData) {
            this.execute("".getBytes());
        }
    }

    /**
     * @see android.os.AsyncTask#doInBackground(Params[])
     * 执行完onpostexecute方法后执行
     */
    protected String doInBackground(byte[]... params) {
        String path = C.IP;
        path = path.concat(toServerData.getUrl());
        Log.v("dddd", "请求路径：" + path);
        HttpPost post = new HttpPost(path);
        try {
//            String sessionId = PropertiesUtil.read(ctx, PropertiesUtil.JSESSION);
            // 表单
            if (null == toServerData.getPath()) {
                // 创建名/值组列表
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                if (null != toServerData.getDatas()) {
                    for (Map.Entry<String, String> ele : toServerData.getDatas().entrySet()) {
                        parameters.add(new BasicNameValuePair(ele.getKey(), ele.getValue()));
                        Log.v("dddd", ele.getKey() + ":" + ele.getValue());
                    }
                }
                if (null != toServerData.getDatasArray()) {
                    for (Map.Entry<String, List<String>> ele : toServerData.getDatasArray()
                        .entrySet()) {
                        for (String mx : ele.getValue()) {
                            parameters.add(new BasicNameValuePair(ele.getKey(), mx));
                        }
                        Log.v("dddd", ele.getKey() + ":" + ele.getValue());
                    }
                }
//                parameters.add(new BasicNameValuePair("jsessionid", sessionId));
                // 创建UrlEncodedFormEntity对象
                HttpEntity en = new UrlEncodedFormEntity(parameters, C.CHARSET);
                post.setEntity(en);
                HttpResponse res = httpClient.execute(post);
                // 请求失败
                if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    handler.post(new ServerDataThread(String.valueOf(res.getStatusLine()
                        .getStatusCode())));
                    return null;
                }
                // 请求成功//取得返回的字符串
                String strResult = EntityUtils.toString(res.getEntity(), "UTF-8");
                // / strResult = StringUtil.subJSON(strResult);
                handler.post(new ServerDataThread(strResult));
                return strResult;
            } else {
                // 表单加文件
                FileBody file = null;
                // 对请求的表单域进行填充
                CustomMultiPartEntity m = new CustomMultiPartEntity(new ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        con.setProgress(num);
                    }
                });
                for (String ele : toServerData.getPath()) {
                    file = new FileBody(new File(ele));
                    m.addPart("file", file);

                    Log.v("dddd", path + ":" + ele);
                }
                if (null != toServerData.getDatas()) {
                    for (Map.Entry<String, String> ele : toServerData.getDatas().entrySet()) {
                        m.addPart(ele.getKey(),
                            new StringBody(ele.getValue(), Charset.forName(C.CHARSET)));
                        Log.v("dddd", ele.getKey() + ":" + ele.getValue());
                    }
                }

                if (null != toServerData.getDatasArray()) {
                    for (Map.Entry<String, List<String>> ele : toServerData.getDatasArray()
                        .entrySet()) {
                        for (String mx : ele.getValue()) {
                            m.addPart(ele.getKey(), new StringBody(mx, Charset.forName(C.CHARSET)));
                        }
                        Log.v("dddd", ele.getKey() + ":" + ele.getValue());
                    }
                }

//                m.addPart("jsessionid", new StringBody(sessionId));
                post.setEntity(m);
                con.setMax(m.getContentLength());
                HttpResponse res = httpClient.execute(post);
                // 请求失败
                if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    handler.post(new ServerDataThread(String.valueOf(res.getStatusLine()
                        .getStatusCode())));
                    return null;
                }
                // 请求成功//取得返回的字符串
                String strResult = EntityUtils.toString(res.getEntity());
                // / strResult = StringUtil.subJSON(strResult);
                handler.post(new ServerDataThread(strResult));
                return strResult;
            }
        } catch (Exception e) {
            if (!isServices) {
                this.con.done();
            }

            L.e("UploadData", e);
        } finally {
            post.abort();
        }
        return null;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     * 一般用来在执行后台任务前对UI做一些标记。
     * 执行完execute方法后执行
     */
    protected void onPostExecute(String result) {

        L.d("onPostExecute", result);
        if (!isServices) {
            this.con.done();
            if (null == result) {
                HttpUtil.serverTimeout(ctx, App.TIMEOUT);
            }
        }
    }

    /**
     * 处理服务器返回数据的线程
     * 
     * @author cyf
     * @version $Id: UploadData.java, v 0.1 2011-6-16 上午02:49:51 cyf Exp $
     */
    private class ServerDataThread implements Runnable {

        /** 服务器返回的数据 */
        private String resData;

        /**
         * 构造函数
         * 
         * @param resData
         *            {@link ServerDataThread#resData}
         */
        public ServerDataThread(String resData) {
            this.resData = resData;
        }

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                // 返回数据为空,提示服务器错误
                if (StringUtil.isBlank(resData)) {
                    if (!isServices) {
                        DialogUtil.toast(ctx, "没有数据!");
                    }
                    return;
                }
                if (resData.charAt(1) == '[') {
                    resData = resData.substring(1);
                }

                if (StringUtil.equals(resData, RE_LOGIN)) {
                    // DialogUtil.show(ctx, "请重新登录");
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(ctx, MainActivity.class);
                    ctx.startActivity(intent);
                    //					BaseActivity.finishAll();
                    return;
                }
                L.v(resData);
                netWork.result(resData);
            } catch (Exception e) {
                L.e("UploadData", e);
            }
        }
    }

}
