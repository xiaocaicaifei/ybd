package com.ybd.yl.yl;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 艺论-上传-支付
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class YlScZfActivity extends BaseActivity implements OnClickListener {

    private TextView yzfTextView;    //应支付
    private TextView yeTextView;     //余额
    private TextView yetsTextView;   //余额提示
    private Button   qczButton;      //去充值
    private TextView wzklyxTextView; //我在考虑一下

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.yl_sc_zf);
        initPublicView("支付");
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        yzfTextView = (TextView) findViewById(R.id.yzf_tv);
        yeTextView = (TextView) findViewById(R.id.ye_tv);
        yetsTextView = (TextView) findViewById(R.id.yets_tv);
        qczButton = (Button) findViewById(R.id.qcz_b);
        qczButton.setOnClickListener(this);
        wzklyxTextView = (TextView) findViewById(R.id.wzklyx_tv);
        wzklyxTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.qcz_b:
                intent.setClass(activity, YlScCzActivity.class);
                startActivity(intent);
                break;
            case R.id.wzklyx_tv:

                break;
            default:
                break;
        }
    }

    /**
     * 查询支付
     */
    class zfNetWork implements INetWork {

        public zfNetWork() {
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("arttalk/gotoPayMoney.json");
                        data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
//                        data.addData("arttalk_id", zzEditText.getText().toString());
                        data.addData("num", "4");

            return data;
        }

        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("code").equals("0")) {
                toastShow("发送成功！");
            } else {
                toastShow("发送失败！");
            }
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == 0) {//选择图片（里面包含拍照和相册的照片）

            }
        }
    }
}
