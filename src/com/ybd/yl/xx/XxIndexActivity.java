/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.BaseFragment;
import com.ybd.yl.R;
import com.ybd.yl.home.HomeClickListener;

/**
 * 信息-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxIndexActivity extends BaseActivity implements OnClickListener{
   
    private ImageLoader imageLoader=ImageLoader.getInstance();
    BaseAdapter xcAdapter;
    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    
    
    
    @Override
    protected void initComponentBase() {
//        view = inflater.inflate(R.layout.xx_index, null, false);
        setContentView(R.layout.xx_index);
        initPublicView("消息列表", R.drawable.xx_txl, 0, XxIndexActivity.this, null);
        init();
//        NetWork.submit(activity, init);
    }
    
    /**
     * 初始化控件
     */
    private void init(){
      
    }
    
    INetWork init=new INetWork() {
        
        @Override
        public boolean validate() {
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("auser/selectUserById.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
            
        }
    };



    @Override
    public void onClick(View v) {
    }

}
