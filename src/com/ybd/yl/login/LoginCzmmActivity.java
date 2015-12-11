package com.ybd.yl.login;

import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 用户登录-重置密码
 * @author cyf
 * @version $Id: LoginActivity.java, v 0.1 2015-10-27 下午5:30:45 cyf Exp $
 */
public class LoginCzmmActivity extends BaseActivity implements OnClickListener {
    private Button xybButton; //下一步按钮
    private EditText mmEditText;//密码
    private EditText qrmmEditText;//确认密码
    
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.login_czmm);
        initPublicView("重置密码");
        initView();
    }
    /**
     * 初始化页面控件
     */
    private void initView() {
        mmEditText=(EditText) findViewById(R.id.regist_mm_et);
        qrmmEditText=(EditText) findViewById(R.id.regist_qrmm_et);
        
        xybButton = (Button) findViewById(R.id.login_xyb_b);
        xybButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View arg0) {

        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.login_xyb_b:
//                intent.setClass(activity, LoginCzmmActivity.class);
//                startActivity(intent);
                NetWork.submit(activity, restPassword);
                break;
        }
    }
    
    INetWork restPassword=new INetWork() {
        
        @Override
        public boolean validate() {
            if(!mmEditText.getText().toString().equals(qrmmEditText.getText().toString())){
                toastShow("两次输入的密码不一致！");
                return false;
            }
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("register/resetPassword.json");
            data.addData("mobile", activity.getIntent().getExtras().getString("sjh"));
            data.addData("password", mmEditText.getText().toString());
            data.addData("randNum", activity.getIntent().getExtras().getString("yzm"));
            return data;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
            if(map.get("code").toString().equals("0")){
                toastShow("重置密码成功！");
            }else{
                toastShow("重置密码失败！");
            }
        }
    };

}
