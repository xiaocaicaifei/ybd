package com.ybd.yl.yl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.INetWorkResult;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 艺论-上传-支付成功
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class YlScZfcgActivity extends BaseActivity implements OnClickListener {

    private TextView czjbTextView; //充值金额
    private Button   qzfButton;    //去支付

    @SuppressWarnings("unchecked")
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.yl_sc_zfcg);
        initPublicView("支付成功", 0, 0, null, null);
        init();
        //发布广播 ，并且保存商品的信息
        BroadcaseUtil.sendBroadcase(BroadcaseUtil.YL_SCCG, activity);
        Map<String, Object> map = (Map<String, Object>) PaseJson
            .paseJsonToObject(PropertiesUtil.read(activity, PropertiesUtil.SCSP));
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        l.addAll((List<Map<String, Object>>) map.get("paths"));
        NetWork.submit(activity, new UploadFile(l, new INetWorkResult() {
            @Override
            public void result(Object result) throws Exception {
                NetWork.submit(activity, new qrscNetWork(result.toString(), "1"));
            }
        }));
    }

    /**
     * 初始化控件
     */
    private void init() {
        czjbTextView = (TextView) findViewById(R.id.czjb_tv);
        qzfButton = (Button) findViewById(R.id.wc_b);
        qzfButton.setOnClickListener(this);
        if (this.getIntent().hasExtra("czje")) {
            czjbTextView.setText("充值金币("+this.getIntent().getExtras().getString("czje")+")");
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.wc_b:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 确认上传
     */
    class qrscNetWork implements INetWork {
        private String path;
        private String isZjsp; //是否直接上拍

        public qrscNetWork(String str, String isZjsp) {
            this.path = str;
            this.isZjsp = isZjsp;
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) PaseJson
                .paseJsonToObject(PropertiesUtil.read(activity, PropertiesUtil.SCSP));
            Data data = new Data("arttalk/pushToArttalk.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("author", PaseJson.getMapMsg(map, "zz"));
            data.addData("num", "4");
            data.addData("mea_len", PaseJson.getMapMsg(map, "cd"));
            data.addData("mea_wide", PaseJson.getMapMsg(map, "kd"));
            data.addData("texture", PaseJson.getMapMsg(map, "zd"));
            data.addData("xs", "1");
            data.addData("fbtype", PaseJson.getMapMsg(map, "fblx"));
            data.addData("is_ars", isZjsp);
            data.addData("startprice", PaseJson.getMapMsg(map, "qpj"));
            data.addData("source", "1");
            data.addData("description", PaseJson.getMapMsg(map, "xq"));
            data.addData("arttalk_id", activity.getIntent().getExtras().getString("ylid"));
            data.addData("path", path);
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
                        JSONObject jsonObject=new JSONObject(result);
                        if(jsonObject.getString("code").equals("0")){
                            
                        }else{
                            
                        }
        }
    };
}
