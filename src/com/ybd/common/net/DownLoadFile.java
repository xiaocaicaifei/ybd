/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.ybd.common.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ybd.common.App;
import com.ybd.common.C;
import com.ybd.common.L;
import com.ybd.common.tools.DialogUtil;
import com.ybd.common.tools.FileUtil;

/**
 * 
 * @author cyf
 * @version $Id: DownLoadFile.java, v 0.1 2013-5-9 上午8:43:54 cyf Exp $
 */
public class DownLoadFile extends AsyncTask<byte[], Integer, String> {

    /** 上下文信息 */
    private Activity       ctx;
    /** 网络连接的工具类 */
    private HttpClient     httpClient;
    /**提交数据*/
    private Data           toServerData;
    /** 本地路径 */
    private String         localPath;
    /** 另存为的文件名 */
    private String         fileName;
    /** 是否打开 */
    private boolean        open;

    /**{@link DialogControl}*/
    private ProgressDialog con;

    public DownLoadFile(Activity ctx, Data toServerData, String localPath, String fileName,
                        boolean open, int sotimeout) {
        super();
        this.ctx = ctx;
        this.toServerData = toServerData;
        this.localPath = localPath;
        this.fileName = fileName;
        this.open = open;
        this.httpClient = HttpUtil.getHttpClient(sotimeout, true);
        this.con = DialogUtil.progress(ctx, "建立连接中...");
        toServerData.addData("name", fileName);
        File dir = new File(localPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /** 触发当前 AsyncTask*/
    public void execute() {
        if (open) {
            File xml = new File(localPath.concat(fileName));
            if (xml.exists()) {
                confirm("文件在本地已经存在，重新下载会覆盖原文件，您可以：");
            } else {
                if (null != toServerData) {
                    execute("".getBytes());
                }
            }

        } else {
            if (null != toServerData) {
                execute("".getBytes());
            }
        }

    }

    /**
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    protected void onPreExecute() {
        super.onPreExecute();
        this.con.setMessage("下载文件中...");
        this.con.show();
    }

    /** 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    protected String doInBackground(byte[]... params) {
        //      String path = PropertiesUtil.read(ctx, PropertiesUtil.IP);
        String path = C.IP;
        path = path.concat(toServerData.getUrl());
        L.d("path", path);
        HttpPost post = new HttpPost(path);

        try {
//            String userId = PropertiesUtil.read(ctx, PropertiesUtil.JSESSION);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            if (null != toServerData.getDatas()) {
                for (Map.Entry<String, String> ele : toServerData.getDatas().entrySet()) {
                    parameters.add(new BasicNameValuePair(ele.getKey(), ele.getValue()));
                }
            }
//            parameters.add(new BasicNameValuePair("jsessionid", userId));
            // 创建UrlEncodedFormEntity对象   
            HttpEntity en = new UrlEncodedFormEntity(parameters, "utf-8");
            post.setEntity(en);
            HttpResponse res = httpClient.execute(post);
            //请求失败
            if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            File xml = new File(localPath.concat(fileName));
            FileOutputStream outputStream = new FileOutputStream(xml);
            InputStream inputStream = res.getEntity().getContent();
            Long total = res.getEntity().getContentLength();
            L.d("download", total);
            this.con.setMax(total.intValue());
            int down = 0;
            byte b[] = new byte[1024];
            int j = 0;
            while ((j = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, j);
                down += j;
                L.d("downloaded", down);
                this.con.setProgress(down);
            }
            outputStream.flush();
            outputStream.close();
            if (open) {
                FileUtil.openFile(ctx, localPath.concat(fileName));
            }
        } catch (Exception e) {
            L.e("UploadData", e);
            this.con.cancel();
        } finally {
            post.abort();
        }
        return null;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    protected void onPostExecute(String result) {
        if (null == result) {
            //HttpUtil.serverTimeout(ctx, App.TIMEOUT);
        }
        if(!open){
            Toast.makeText(ctx, "下载成功,存放路径为"+localPath+fileName, Toast.LENGTH_LONG).show();
        }
        this.con.cancel();
    }

    /**
     * 弹出一个确认消息框
     * 
     * @param msg   消息框上显示的消息
     * @param callback
     *            用户点击确认消息框上的取消时，触发的回调动作
     */
    public void confirm(String msg) {
        Builder builder = new Builder(ctx).setTitle(App.INFO);
        builder.setMessage(msg);
        Dialog dialog = builder.setPositiveButton("打开本地文件", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                cancel(true);
                FileUtil.openFile(ctx, localPath.concat(fileName));
                con.cancel();
            }
        }).setNegativeButton("下载最新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (null != toServerData) {
                    execute("".getBytes());
                }
            }
        }).create();
        dialog.show();
    }

   
}
