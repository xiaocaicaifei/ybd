package com.ybd.yl.qz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.INetWorkResult;
import com.ybd.common.net.NetWork;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 圈子-上传-上传图片
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class QzScSctpActivity extends BaseActivity implements OnClickListener {
    private GridView                        tpGridView;
    private BaseAdapter                     tpAdapter;
    public static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private static int gdState=0;//更多按钮是否加载
    private Button qrscButton;//确认上传按钮
    private EditText nrEditText;//内容

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.qz_sc_sctp);
        initPublicView("上传图片", R.drawable.login_fh, 0, null, null);
        init();
    }

    /**
     * 初始化控件
     */
    //    @SuppressWarnings("unchecked")
    private void init() {
        tpGridView = (GridView) findViewById(R.id.tp_gv);
        if(gdState==0){
            //更多按钮
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("path", "drawable://" + R.drawable.qz_sc_sctp_gd);
            list.add(list.size(), map2);
            gdState=1;//只加载一次，以后就不在加载
        }
        tpAdapter = new QzScSctpAdapter(list, activity);
        tpGridView.setAdapter(tpAdapter);
        tpAdapter.notifyDataSetChanged();
        qrscButton=(Button) findViewById(R.id.qrsc_b);
        qrscButton.setOnClickListener(this);
        nrEditText=(EditText) findViewById(R.id.nr_et);
    }
    
    
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.qrsc_b:
                NetWork.submit(activity, new UploadFile(list.subList(0, list.size()-1), new INetWorkResult() {
                    @Override
                    public void result(Object result) throws Exception {
                        NetWork.submit(activity, new qrscNetWork(result.toString()));
                    }
                }));
                break;
            default:
                break;
        }
    }
    
    /**
     */
    class qrscNetWork implements INetWork{
        private String path;
        public qrscNetWork(String str) {
            this.path=str;
        }
        @Override
        public boolean validate() {
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("circle/pushToCircle.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("fbtype", PropertiesUtil.read(activity, "ND"));
            data.addData("description", nrEditText.getText().toString());
            data.addData("paths", path);
            return data;
        }
        
        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject.getString("code").equals("0")){
                toastShow("发送成功！");
                Intent intent=new Intent("QZSX");
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                finish();
            }else{
                toastShow("发送失败！");
            }
        }
    };
}
