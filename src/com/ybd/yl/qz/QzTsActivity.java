package com.ybd.yl.qz;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 圈子-投诉
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class QzTsActivity extends BaseActivity implements OnClickListener {

    private Button qrtjButton;//确认提交Button
    private RadioGroup  tsRadioGroup;
    private String tsyy="1";//1,2,3,4,顺序对应上下顺序的四个

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.qz_ts);
        initPublicView("用户投诉", R.drawable.login_fh, 0, null, null);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        tsRadioGroup = (RadioGroup) findViewById(R.id.ts_rg);
        tsRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.one_rb:
                        tsyy="1";
                        break;
                    case R.id.two_rb:
                        tsyy="2";
                        break;
                    case R.id.three_rb:
                        tsyy="3";
                        break;
                    case R.id.four_rb:
                        tsyy="4";
                        break;
                    default:
                        break;
                }
            }
        });
        qrtjButton=(Button) findViewById(R.id.qrtj_b);
        qrtjButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.qrtj_b:
                NetWork.submit(activity, tsNetWork);
                break;
            default:
                break;
        }
    }
    
    private INetWork tsNetWork=new INetWork() {
        
        @Override
        public boolean validate() {
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("complain/circleComplain.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("circle_id", activity.getIntent().getExtras().getString("qzid"));
            data.addData("tstype", tsyy);
            return data;
        }
        
        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject.getString("code").equals("0")){
                toastShow("投诉成功！");
                finish();
            }else{
                toastShow("投诉失败！");
            }
        }
        
    };

}
