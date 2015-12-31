package com.ybd.yl.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.qz.QzScSctpActivity;

/**
 * 选择图片-多选(用于艺论)
 * @author cyf
 * @version $Id: SelectPhoto.java, v 0.1 2015-11-19 上午10:09:03 cyf Exp $
 */
public class SelectPhotoMultiple2Activity extends BaseActivity implements OnClickListener {
    private GridView          xcGridView;                                 //相册
    private SelectPhotoMultipleAdapter       adapter;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//    private String selectImagePath="";//用户选择的照片
    private TextView ylTextView;//预览
    private Button qdButton;//确定
    private int selectNum;//选定照片的数量
    private int sumSelectNum;//一共可选的数量（可选12张-牌照的数量）


    @Override
    protected void initComponentBase() {
        setContentView(R.layout.select_photo_multiply);
        initPublicView("相册",R.drawable.login_fh,0,null,null);
        init();
        getImages();
    }
    /**
     * 选中图片的时候的事件
     */
    OnClickListener onSelectListener=new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            if(v.getTag().toString().equals("0")){
                selectNum--;
            }else{
                selectNum++;
            }
            if(selectNum==0){
                qdButton.setBackgroundResource(R.drawable.login_share_button2);
                qdButton.setTextColor(R.color.yl_username_msg_gray_color);
            }else{
                qdButton.setBackgroundResource(R.drawable.login_share_button);
                qdButton.setTextColor(R.color.white);
            }
            qdButton.setText("确定("+selectNum+"/"+sumSelectNum+")");
        }
    };

    /**
     * 初始化页面控件
     */
    private void init() {
        xcGridView = (GridView) findViewById(R.id.xc_gv);
        adapter = new SelectPhotoMultipleAdapter(list, activity,onSelectListener);
        xcGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        xcGridView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    // 拍照
//                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(i, 1);
//                } else {
//                    selectImagePath=list.get(position).get("path").toString();
//                    Uri imageUri = Uri.parse(selectImagePath);
//                    cropImageUri(imageUri, 300, 300, 2);
//                }
//            }
//        });
        qdButton=(Button) findViewById(R.id.qd_b);
        qdButton.setOnClickListener(this);
        ylTextView=(TextView) findViewById(R.id.yl_tv);
        ylTextView.setOnClickListener(this);
        
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
                ContentResolver mContentResolver = SelectPhotoMultiple2Activity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?", new String[] {
                            "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

                final List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("path", "file:///" + path);
                    map.put("ispz", "0");
                    for(Map<String, Object> m:QzScSctpActivity.list){
                        if(m.get("path").toString().equals("file:///" + path)){
                            map.put("select", "1");
                            selectNum++;
                        }
                    }
                    l.add(map);
                }

                list.clear();
                list.addAll(l);
                try {
                    if(selectNum==0){
                        qdButton.setBackgroundResource(R.drawable.login_share_button2);
                        qdButton.setTextColor(R.color.yl_username_msg_gray_color);
                    }else{
                        qdButton.setBackgroundResource(R.drawable.login_share_button);
                        qdButton.setTextColor(R.color.white);
                    }
                    sumSelectNum=12-((QzScSctpActivity.list.size()-1)-selectNum);
                    if(sumSelectNum>12){
                        sumSelectNum=12;
                    }
                    qdButton.setText("确定("+selectNum+"/"+sumSelectNum+")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        }).start();
    }

//    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("scale", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection
//        startActivityForResult(intent, requestCode);
//    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
//        if (arg1 == RESULT_OK) {
//            if (arg0 == 1) {
//                selectImagePath = PicUtil.savePz(arg2);
//                Uri imageUri = Uri.parse("file:///" + selectImagePath);
//                cropImageUri(imageUri, 300, 300, 2);
//            } else if (arg0 == 2) {
//                Intent intent = new Intent();
//                intent.putExtra("path", selectImagePath.replace("file:///",""));
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.yl_tv:
                List<Map<String, Object>> l2=new ArrayList<Map<String,Object>>();
                for(Map<String, Object> map:adapter.getList()){
                    if(map.get("select")!=null&&map.get("select").toString().equals("1")){
                        l2.add(map);
                    }
                }
                intent.putExtra("object", (Serializable)l2);
                intent.setClass(activity, PreviewImgActivity.class);
                startActivity(intent);
                break;
            case R.id.qd_b:
                List<Map<String, Object>> l=new ArrayList<Map<String,Object>>();
                for(Map<String, Object> map:adapter.getList()){
                    if(map.get("select")!=null&&map.get("select").toString().equals("1")){
                        l.add(map);
                    }
                }
                intent.putExtra("list", (Serializable)l);
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
