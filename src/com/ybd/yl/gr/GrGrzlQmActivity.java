/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.gr;

import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.L;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.KeyboardOperate;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.common.SelectPhotoActivity;

/**
 * 个人-个人资料-签名
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class GrGrzlQmActivity extends BaseActivity implements OnClickListener {

    private EditText qmEditText;//签名
    private TextView numTextView;//剩余签名输入的数量
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.gr_grzl_qm);
        initPublicView("签名", R.drawable.login_fh, "保存", null, this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        numTextView=(TextView) findViewById(R.id.num_tv);
        numTextView.setText("30");
        qmEditText=(EditText) findViewById(R.id.qm_et);
        qmEditText.setText(this.getIntent().getExtras().getString("nr"));
        qmEditText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numTextView.setText(30-s.toString().length()+"");
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.right_rl:
                KeyboardOperate.setInputManager(false, activity);
                intent.putExtra("nr", qmEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

}
