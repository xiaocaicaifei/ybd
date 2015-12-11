package com.ybd.yl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.MainApplication;
import com.ybd.yl.R;

public class SelectPhotoAdapter extends BaseAdapter {
    private List<Map<String, String>> list        = new ArrayList<Map<String, String>>();
    private Activity                  activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();
    WindowManager                     wm;

    public SelectPhotoAdapter(List<Map<String, String>> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, String> map = list.get(position);
        ViewHoler viewHoler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.select_photo_item, null);// 这个过程相当耗时间
            viewHoler = new ViewHoler();
            viewHoler.photoImageView = (ImageView) convertView.findViewById(R.id.photo_iv);

            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams=viewHoler.photoImageView.getLayoutParams();
            layoutParams.height=((width-26)/3);
            viewHoler.photoImageView.setLayoutParams(layoutParams);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        if(map.get("path").toString().equals("")){
//            viewHoler.photoImageView.setBackgroundResource(R.drawable.regist_txgrzl_camer);
                imageLoader.displayImage("drawable://" +R.drawable.regist_txgrzl_camer,viewHoler.photoImageView,
                    MainApplication.getOptions());
//                imageLoader.displayImage("assets://regist_txgrzl_camer.png",viewHoler.photoImageView,
//                    MainApplication.getOptions());
        }else{
            imageLoader.displayImage(map.get("path").toString(), viewHoler.photoImageView,
                MainApplication.getOptions());
        }
        //        Bitmap bitmap=ImageUtil.getLoacalBitmap(map.get("path").toString());
        //        Bitmap newBit = Bitmap
        //                .createScaledBitmap(bitmap, 100, 80, true);
        //        viewHoler.photoImageView.setImageBitmap(newBit);
        return convertView;
    }

    class ViewHoler {
        ImageView photoImageView;
    }
}
