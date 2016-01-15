/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.util.Map;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.ybd.yl.R;

/**
 * 消息-通讯录-添加好友好友验证
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxTxlTjdvHyyzActivity extends BaseActivity implements OnClickListener {
    private ImageView txImageView;//头像
    private TextView ncTextView;//昵称
    private TextView gmTextView;//购买
    private TextView mcTextView;//卖出
    private TextView xyTextView;//信用
    private TextView fsTextView;//粉丝
    private EditText tjhysmEditText;//添加好友说明
    Map<String, Object> map;//传过来的信息
    private ImageLoader               imageLoader = ImageLoader.getInstance();
    @SuppressWarnings("unchecked")
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl_tjdv_hyyz);
        initPublicView("身份验证", R.drawable.login_fh, "发送", null, this);
        if(this.getIntent().hasExtra("hyObject")){
            map=(Map<String, Object>) this.getIntent().getExtras().getSerializable("hyObject");
        }
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
       txImageView=(ImageView) findViewById(R.id.tx_iv);
       ncTextView=(TextView) findViewById(R.id.nc_tv);
       gmTextView=(TextView) findViewById(R.id.gm_tv);
       mcTextView=(TextView) findViewById(R.id.mc_tv);
       xyTextView=(TextView) findViewById(R.id.xy_tv);
       fsTextView=(TextView) findViewById(R.id.fs_tv);
       tjhysmEditText=(EditText) findViewById(R.id.tjhysm_et);
       
       imageLoader.displayImage(C.IP+PaseJson.getMapMsg(map, "icon_url"), txImageView,MainApplication.getRoundOffOptions());
       ncTextView.setText(PaseJson.getMapMsg(map, "nick_name"));
       gmTextView.setText(PaseJson.getMapMsg(map, "buy_vol")+"分");
       mcTextView.setText(PaseJson.getMapMsg(map, "sale_vol")+"件");
       xyTextView.setText(PaseJson.getMapMsg(map, "degree_credit")+"分");
       fsTextView.setText(PaseJson.getMapMsg(map, "followers_count"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_rl:
                NetWork.submit(activity, new jwhyNetWork());
                break;

            default:
                break;
        }
    }
    /**
     * 申请加为好友
     */
    class jwhyNetWork implements INetWork {
        @Override
        public boolean validate() {
            if(tjhysmEditText.getText().toString().equals("")){
                toastShow("好友验证信息不能为空！");
                return false;
            }
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("myfriend/addFriend.json");
            data.addData("relate_id",
                PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("brelate_id", PaseJson.getMapMsg(map, "user_id"));
            data.addData("apply_note",tjhysmEditText.getText().toString());
            return data;
        }

        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject.getString("code").equals("0")){
                toastShow("操作成功！");
            }else if(jsonObject.getString("code").equals("1")){
                toastShow("不能重复添加好友！");
            }else{
                toastShow("操作失败！");
            }
        }
    };
}
