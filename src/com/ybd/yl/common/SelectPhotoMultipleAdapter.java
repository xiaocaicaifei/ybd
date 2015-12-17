package com.ybd.yl.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.MainApplication;
import com.ybd.yl.R;

/**
 * 选择图片-多选
 * @author cyf
 * @version $Id: SelectPhotoMultipleAdapter.java, v 0.1 2015-12-16 下午4:59:56 cyf Exp $
 */
public class SelectPhotoMultipleAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();
    WindowManager                     wm;
    public SelectPhotoMultipleAdapter(List<Map<String, Object>> list, Activity activity) {
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


    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String, Object> map = list.get(position);
        ViewHoler viewHoler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.select_photo_multiply_item, null);// 这个过程相当耗时间
            viewHoler = new ViewHoler();
            viewHoler.photoImageView = (ImageView) convertView.findViewById(R.id.photo_iv);
            viewHoler.selectIconImageView=(ImageView) convertView.findViewById(R.id.select_icon_iv);

            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams=viewHoler.photoImageView.getLayoutParams();
            layoutParams.height=((width-26)/3);
            viewHoler.photoImageView.setLayoutParams(layoutParams);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
//        if(map.get("path").toString().equals("")){
//                imageLoader.displayImage("drawable://" +R.drawable.regist_txgrzl_camer,viewHoler.photoImageView,
//                    MainApplication.getOptions());
//        }else{
            imageLoader.displayImage(map.get("path").toString(), viewHoler.photoImageView,
                MainApplication.getOptions());
//        }
        viewHoler.selectIconImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(map.get("select")!=null&&map.get("select").toString().equals("1")){
                    map.put("select","0");
                    v.setBackgroundResource(R.drawable.select_photo_multiply);
                }else{
                    map.put("select","1");
                    v.setBackgroundResource(R.drawable.select_photo_multiply_select);
                    Map<String, Object> m=new HashMap<String, Object>();
                    m.put("path", map.get("path").toString());
                }
                
            }
        });
        if(map.get("select")!=null&& map.get("select").toString().equals("1")){
            viewHoler.selectIconImageView.setBackgroundResource(R.drawable.select_photo_multiply_select);
        }else{
            viewHoler.selectIconImageView.setBackgroundResource(R.drawable.select_photo_multiply);
        }
        
        return convertView;
    }

    class ViewHoler {
        ImageView photoImageView;
        ImageView selectIconImageView;
    }
}
