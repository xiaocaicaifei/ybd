package com.ybd.yl.pm;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.C;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 拍卖-上传-电子协议书
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class PmScDzxysActivity extends BaseActivity implements OnClickListener {

    private Button  btyButton;    //不同意
    private Button  tyButton;     //确认上传
    private WebView dzxysWebView; //电子协议书

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.pm_sc_dzxys);
        initPublicView("电子协议书");
        init();
        registBroadcast();
    }

    /**
     * 初始化控件
     */
    private void init() {
        btyButton = (Button) findViewById(R.id.bty_b);
        btyButton.setOnClickListener(this);
        tyButton = (Button) findViewById(R.id.ty_b);
        tyButton.setOnClickListener(this);
        dzxysWebView = (WebView) findViewById(R.id.dzxy_wb);
        dzxysWebView.loadUrl(C.IP+"static/protocol.html");
    }
    
    /**
     * 注册广播
     */
    private void registBroadcast(){
        //支付成功的广播
        BroadcaseUtil.registBroadcase(activity, new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, BroadcaseUtil.YL_SCCG);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent();
        switch (arg0.getId()) {

            case R.id.bty_b:
                finish();
                break;
            case R.id.ty_b:
                intent.setClass(activity, PmJykActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 查询电子协议书
     */
    class dzxysNetWork implements INetWork {

        public dzxysNetWork() {
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("electronicagree/agreeElectronic.json");
//            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
//            data.addData("author", zzEditText.getText().toString());
//            data.addData("num", "4");

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
