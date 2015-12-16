package com.ybd.yl.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.ybd.common.PicUtil;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 选择相册-单选
 * @author cyf
 * @version $Id: SelectPhoto.java, v 0.1 2015-11-19 上午10:09:03 cyf Exp $
 */
public class SelectPhotoActivity extends BaseActivity {
    private GridView          xcGridView;                                 //相册
    private BaseAdapter       adapter;
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private String selectImagePath="";//用户选择的照片

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.select_photo);
        initPublicView("相册");
        init();
        getImages();
    }

    /**
     * 初始化页面控件
     */
    private void init() {
        xcGridView = (GridView) findViewById(R.id.xc_gv);
        adapter = new SelectPhotoAdapter(list, activity);
        xcGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        xcGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // 拍照
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 1);
                } else {
                    selectImagePath=list.get(position).get("path").toString();
                    Uri imageUri = Uri.parse(selectImagePath);
                    cropImageUri(imageUri, 300, 300, 2);
                }
            }
        });
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SelectPhotoActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?", new String[] {
                            "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

                final List<Map<String, String>> l = new ArrayList<Map<String, String>>();
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("path", "file:///" + path);
                    l.add(map);
                }

                list.clear();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("path", "");
                list.add(map);
                list.addAll(l);
                adapter.notifyDataSetChanged();
            }
        }).start();
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == 1) {
                selectImagePath = PicUtil.savePz(arg2);
                Uri imageUri = Uri.parse("file:///" + selectImagePath);
                cropImageUri(imageUri, 300, 300, 2);
            } else if (arg0 == 2) {
                Intent intent = new Intent();
                intent.putExtra("path", selectImagePath.replace("file:///",""));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
