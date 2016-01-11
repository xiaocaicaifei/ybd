package com.ybd.yl.pm;

import java.util.HashMap;
import java.util.Map;

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
 * 拍卖-加一口-
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class PmJykActivity extends BaseActivity implements OnClickListener {

    private TextView yzfTextView;    //应支付
    private TextView yeTextView;     //余额
    private TextView yetsTextView;   //余额提示
    private Button   qdButton;      //确认
    private TextView wzklyxTextView; //我在考虑一下
    private TextView cjTextView;//出价
    private boolean sfcz;//金币是否充足
    private Map<String,Object> map=new HashMap<String, Object>();

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.pm_jyk);
        initPublicView("支付");
        init();
        NetWork.submit(activity, new yeNetWork());
//        registBroadcast();
    }

    /**
     * 初始化控件
     */
    private void init() {
        yzfTextView = (TextView) findViewById(R.id.yzf_tv);
        yeTextView = (TextView) findViewById(R.id.ye_tv);
        yetsTextView = (TextView) findViewById(R.id.yets_tv);
        qdButton = (Button) findViewById(R.id.qd_b);
        qdButton.setOnClickListener(this);
        wzklyxTextView = (TextView) findViewById(R.id.wzklyx_tv);
        wzklyxTextView.setOnClickListener(this);
        cjTextView=(TextView) findViewById(R.id.cj_tv);
    }
//    /**
//     * 注册广播
//     */
//    private void registBroadcast(){
//        //支付成功的广播
//        BroadcaseUtil.registBroadcase(activity, new BroadcastReceiver() {
//            
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                finish();
//            }
//        }, BroadcaseUtil.YL_SCCG);
//    }
    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.qd_b:
                if(sfcz){
                    intent.setClass(activity, PmScCzActivity.class);
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
     * 查询加一口的信息
     */
    class yeNetWork implements INetWork {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("addpricerecord/raisePrice.json");
                        data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
                        data.addData("arttalk_id", activity.getIntent().getExtras().getString("ylid"));
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
           map=(Map<String, Object>) ((Map<String, Object>) PaseJson.paseJsonToObject(result)).get("data");
           String yzf=PaseJson.getMapMsg(map, "fee");
           String ye=PaseJson.getMapMsg(map, "kyMoney");
           yzfTextView.setText(yzf);
           yeTextView.setText("余额("+ye+")");
           if(Double.parseDouble(ye)>Double.parseDouble(yzf)){
               sfcz=false;
               yetsTextView.setText("去支付");
               qdButton.setText("立即支付");
           }else{
               yetsTextView.setText("余额不足");
               qdButton.setText("去充值");
               sfcz=true;
           }
           String str="当前价格为"+PaseJson.getMapMsg(map, "curprice")+"，您愿意出价"+PaseJson.getMapMsg(map, "addprice")+"需要冻结您"+PaseJson.getMapMsg(map, "fee")+"金币，作为佣金，如果您顺利成交将扣除佣金，别人买走将解除冻结";
           cjTextView.setText(str);
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
            Data data = new Data("addpricerecord/improvePrice.json");
                        data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
                        data.addData("arttalk_id", activity.getIntent().getExtras().getString("ylid"));
                        data.addData("offerPrice",PaseJson.getMapMsg(map, "addprice"));
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
//          JSONObject jsonObject=new JSONObject(result);
            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
          if(PaseJson.getMapMsg(map, "code").equals("1")){
              toastShow("操作成功！");
//              Intent intent=new Intent();
//              intent.putExtra("ylid", ((Map<String, Object>)map.get("data")).get("arttalk_id").toString());
//              intent.putExtra("czje", PaseJson.getMapMsg(map, "payMoney"));
//              intent.setClass(activity, PmScZfcgActivity.class);
//              startActivity(intent);
              BroadcaseUtil.sendBroadcase(BroadcaseUtil.PM_JYK_SUCCESS, activity);
              finish();
          }else if(PaseJson.getMapMsg(map, "code").equals("-1")){
              toastShow("操作失败！");
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
