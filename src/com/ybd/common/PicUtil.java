/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.ybd.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.ybd.common.tools.FileUtil;
import com.ybd.common.tools.FileUtil.DirType;

/**
 * 生成拍照图片的工具类
 * 
 * @author wum
 * @version $Id: PicUtil.java, v 0.1 2014年8月1日 上午10:08:19 wum Exp $
 */
public class PicUtil {

    private static final String TAG = "PicUtil";

    /**
     * 将用户拍照的拍照信息保存成图片（jpg）
     * 
     * @param data {@link Intent}
     * @return 图片路径
     */
    public static String savePz(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        FileOutputStream b = null;
        String name = String.valueOf(System.currentTimeMillis()).concat(".jpg");
        File f = new File(FileUtil.getRFileSaveDir(DirType.ZFWS, name));
        try {
            b = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "", e);
        } finally {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }

}
