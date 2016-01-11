package com.ybd.yl.pm;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 拍卖-上传-充值
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */

public class PmScCzActivity extends BaseActivity implements OnClickListener {
    private Button ssButton;//30
    private Button wsButton;//50
    private Button ybButton;//100
    private Button lbButton;//200
    private Button sbButton;//300
    private Button wbButton;//500
    private EditText jeEditText;//金额
    private LinearLayout wxzfLayout;//微信支付
    private LinearLayout zfbzfLayout;//支付宝支付
    private LinearLayout ylzfLayout;//银联支付
    private ImageView wxzfImageView;//微信支付
    private ImageView zfbzfImageView;//支付宝支付
    private ImageView ylzfImageView;//银联支付

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.pm_sc_cz);
        initPublicView("充值");
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
       ssButton=(Button) findViewById(R.id.ssy);
       ssButton.setOnClickListener(this);
       wsButton=(Button) findViewById(R.id.wsy);
       wsButton.setOnClickListener(this);
       ybButton=(Button) findViewById(R.id.yby);
       ybButton.setOnClickListener(this);
       lbButton=(Button) findViewById(R.id.lby);
       lbButton.setOnClickListener(this);
       sbButton=(Button) findViewById(R.id.sby);
       sbButton.setOnClickListener(this);
       wbButton=(Button) findViewById(R.id.wby);
       wbButton.setOnClickListener(this);
       jeEditText=(EditText) findViewById(R.id.je_et);
       wxzfLayout=(LinearLayout) findViewById(R.id.wxzf_ll);
       wxzfLayout.setOnClickListener(this);
       wxzfImageView=(ImageView) findViewById(R.id.wxzf_iv);
       zfbzfLayout=(LinearLayout) findViewById(R.id.zfbzf_ll);
       zfbzfLayout.setOnClickListener(this);
       zfbzfImageView=(ImageView) findViewById(R.id.zfbzf_iv);
       ylzfLayout=(LinearLayout) findViewById(R.id.ylzf_ll);
       ylzfLayout.setOnClickListener(this);
       ylzfImageView=(ImageView) findViewById(R.id.ylzf_iv);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.ssy:
                jeEditText.setText("30");
                ssButton.setTextAppearance(activity, R.style.yl_sc_cz_button_select);
                wsButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                ybButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                lbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                sbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                break;
            case R.id.wsy:
                jeEditText.setText("50");
                ssButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wsButton.setTextAppearance(activity, R.style.yl_sc_cz_button_select);
                ybButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                lbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                sbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                break;
            case R.id.yby:
                jeEditText.setText("100");
                ssButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wsButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                ybButton.setTextAppearance(activity, R.style.yl_sc_cz_button_select);
                lbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                sbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                break;
            case R.id.lby:
                jeEditText.setText("200");
                ssButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wsButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                ybButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                lbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_select);
                sbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                break;
            case R.id.sby:
                jeEditText.setText("300");
                ssButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wsButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                ybButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                lbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                sbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_select);
                wbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                break;
            case R.id.wby:
                jeEditText.setText("500");
                ssButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wsButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                ybButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                lbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                sbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_unselect);
                wbButton.setTextAppearance(activity, R.style.yl_sc_cz_button_select);
                break;
            case R.id.wxzf_ll:
                wxzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_select);
                zfbzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_unselect);
                ylzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_unselect);
                intent.setClass(activity, PmScCzcgActivity.class);
                startActivity(intent);
                break;
            case R.id.zfbzf_ll:
                wxzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_unselect);
                zfbzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_select);
                ylzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_unselect);
                break;
            case R.id.ylzf_ll:
                wxzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_unselect);
                zfbzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_unselect);
                ylzfImageView.setBackgroundResource(R.drawable.yl_sc_cz_select);
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
