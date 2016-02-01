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
 * 个人-个人资料-昵称
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class GrGrzlNcActivity extends BaseActivity implements OnClickListener {

    private EditText ncEditText;//昵称
    
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.gr_grzl_nc);
        initPublicView("更改昵称", R.drawable.login_fh, "保存", null, this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        ncEditText=(EditText) findViewById(R.id.qm_et);
        ncEditText.setText(this.getIntent().getExtras().getString("nr"));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.right_rl:
                KeyboardOperate.setInputManager(false, activity);
                intent.putExtra("nr", ncEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

}
