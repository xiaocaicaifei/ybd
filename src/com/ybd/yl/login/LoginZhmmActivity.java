package com.ybd.yl.login;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 用户登录-找回密码
 * @author cyf
 * @version $Id: LoginActivity.java, v 0.1 2015-10-27 下午5:30:45 cyf Exp $
 */
public class LoginZhmmActivity extends BaseActivity implements OnClickListener {

    private Button xybButton; //下一步按钮
    private EditText sjhEditText;//手机号

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.login_zhmm);
        initPublicView("找回密码");
        initView();
    }
    /**
     * 初始化页面控件
     */
    private void initView() {
        sjhEditText=(EditText) findViewById(R.id.regist_sjh_e);
        xybButton = (Button) findViewById(R.id.login_xyb_b);
        xybButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {

        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.login_xyb_b:
                if(sjhEditText.getText().toString().equals("")){
                    toastShow("手机号不能为空");
                    return ;
                }
                intent.putExtra("sjh", sjhEditText.getText().toString());
                intent.setClass(activity, LoginTxyzmActivity.class);
                startActivity(intent);
                break;
        }
    }

}
