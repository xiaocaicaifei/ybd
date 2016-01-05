package com.ybd.yl.yl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
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
    private boolean sfcz;//金币是否充足
    private Map<String,Object> map=new HashMap<String, Object>();

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.yl_sc_zf);
        initPublicView("支付");
        init();
        NetWork.submit(activity, new yeNetWork());
        registBroadcast();
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
            case R.id.qcz_b:
                if(sfcz){
                    intent.setClass(activity, YlScCzActivity.class);
                    startActivity(intent);
                }else{
                    NetWork.submit(activity, new zfNetWork());
                }
                break;
            case R.id.wzklyx_tv:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 查询余额
     */
    class yeNetWork implements INetWork {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("arttalk/gotoPayMoney.json");
                        data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
                        data.addData("startprice", PropertiesUtil.read(activity, PropertiesUtil.QPJ));
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
           map=(Map<String, Object>) ((Map<String, Object>) PaseJson.paseJsonToObject(result)).get("data");
           String yzf=PaseJson.getMapMsg(map, "payMoney");
           String ye=PaseJson.getMapMsg(map, "kyamount");
           yzfTextView.setText(yzf);
           yeTextView.setText("余额("+ye+")");
           if(Double.parseDouble(ye)>Double.parseDouble(yzf)){
               sfcz=false;
               yetsTextView.setText("去支付");
               qczButton.setText("立即支付");
           }else{
               yetsTextView.setText("余额不足");
               qczButton.setText("去充值");
               sfcz=true;
           }
        }
    };
    /**
     * 支付
     */
    class zfNetWork implements INetWork {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("arttalk/payMoney.json");
                        data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
                        data.addData("payMoney",PaseJson.getMapMsg(map, "payMoney"));
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
//          JSONObject jsonObject=new JSONObject(result);
            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
          if(PaseJson.getMapMsg(map, "code").equals("1")){
              toastShow("支付成功！");
              Intent intent=new Intent();
              intent.putExtra("ylid", ((Map<String, Object>)map.get("data")).get("arttalk_id").toString());
              intent.putExtra("czje", PaseJson.getMapMsg(map, "payMoney"));
              intent.setClass(activity, YlScZfcgActivity.class);
              startActivity(intent);
          }else if(PaseJson.getMapMsg(map, "code").equals("-1")){
              toastShow("支付失败！");
          }else if(PaseJson.getMapMsg(map, "code").equals("2")){
              toastShow("支付失败，余额不足！");
          }
        }
    };
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == 0) {//选择图片（里面包含拍照和相册的照片）

            }
        }
    }
}
