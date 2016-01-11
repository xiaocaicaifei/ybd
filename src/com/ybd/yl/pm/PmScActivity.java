package com.ybd.yl.pm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.GridViewRun;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.common.PreviewImgActivity;
import com.ybd.yl.yl.YlScDzxysActivity;

/**
 * 拍卖-上传
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class PmScActivity extends BaseActivity implements OnClickListener {
    private Button   ddButton;      //当代
    private Button   jxdButton;     //近现代
    private Button   gdButton;      //古代
    private GridViewRun                        tpGridView;
    private BaseAdapter                     tpAdapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private String   selectNd = "2"; //选择年代的值(1当代;2近现代;3古代;)
    private EditText zzEditText;//作者
    private EditText zdEditText;//质地
    private EditText cdEditText;//尺寸(长度)
    private EditText kdEditText;//尺寸(宽度)
    private EditText xqEditText;//详情
    private Button bxButton;//不详
    private Button wkButton;//无款
    private Button zbButton;//纸本
    private Button jbButton;//绢本
    private Button lbButton;//绫本
//    private Button zjspButton;//直接上拍
//    private Button qrscButton;//确认上传
    private EditText qpjEditText;//起拍价
    
    

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.pm_sc);
        initPublicView("上传",R.drawable.login_fh,"确定",null,this);
        init();
        initTpGridView();
        registBroadcast();
    }

    /**
     * 初始化控件
     */
    private void init() {
        ddButton = (Button) findViewById(R.id.dd_b);
        ddButton.setOnClickListener(this);
        jxdButton = (Button) findViewById(R.id.jxd_b);
        jxdButton.setOnClickListener(this);
        gdButton = (Button) findViewById(R.id.gd_b);
        gdButton.setOnClickListener(this);
        zzEditText=(EditText) findViewById(R.id.zz_et);
        zzEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                juageQrsc();
            }
        });
        bxButton=(Button) findViewById(R.id.bx_b);
        bxButton.setOnClickListener(this);
        wkButton=(Button) findViewById(R.id.wk_b);
        wkButton.setOnClickListener(this);
        zdEditText=(EditText) findViewById(R.id.zd_et);
        zdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                juageQrsc();
            }
        });
        zbButton=(Button) findViewById(R.id.zb_b);
        zbButton.setOnClickListener(this);
        jbButton=(Button) findViewById(R.id.jb_b);
        jbButton.setOnClickListener(this);
        lbButton=(Button) findViewById(R.id.lb_b);
        lbButton.setOnClickListener(this);
        cdEditText=(EditText) findViewById(R.id.cd_et);
        cdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                juageQrsc();
            }
        });
        kdEditText=(EditText) findViewById(R.id.kd_et);
        kdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                juageQrsc();
            }
        });
        xqEditText=(EditText) findViewById(R.id.xq_et);
//        zjspButton=(Button) findViewById(R.id.zjsp_b);
//        zjspButton.setOnClickListener(this);
//        qrscButton=(Button) findViewById(R.id.qrsc_b);
//        qrscButton.setOnClickListener(this);
//        qrscButton.setEnabled(false);
        
        qpjEditText=(EditText) findViewById(R.id.qpj_et);
    }
    /**
     * 初始化图片的GridView
     */
    private void initTpGridView(){
      //更多按钮
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("path", "drawable://" + R.drawable.qz_sc_sctp_gd);
        list.add(list.size(), map2);
        tpGridView = (GridViewRun) findViewById(R.id.tp_gv);
        tpAdapter = new PmScAdapter(list, activity);
        tpGridView.setAdapter(tpAdapter);
        tpAdapter.notifyDataSetChanged();
        tpGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(activity, PreviewImgActivity.class);
                intent.putExtra("object", (Serializable)list.subList(0, list.size()-1));
                startActivity(intent);
            }
        });
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
            case R.id.dd_b:
                ddButton.setBackgroundResource(R.drawable.yl_share_button);
                ddButton.setTextColor(getResources().getColor(R.color.white));
                jxdButton.setBackgroundResource(R.drawable.yl_share_button2);
                jxdButton.setTextColor(getResources().getColor(R.color.unitform_button_red));
                gdButton.setBackgroundResource(R.drawable.yl_share_button2);
                gdButton.setTextColor(getResources().getColor(R.color.unitform_button_red));
                selectNd = "1";
                break;
            case R.id.jxd_b:
                jxdButton.setBackgroundResource(R.drawable.yl_share_button);
                jxdButton.setTextColor(getResources().getColor(R.color.white));
                ddButton.setBackgroundResource(R.drawable.yl_share_button2);
                ddButton.setTextColor(getResources().getColor(R.color.unitform_button_red));
                gdButton.setBackgroundResource(R.drawable.yl_share_button2);
                gdButton.setTextColor(getResources().getColor(R.color.unitform_button_red));
                selectNd = "2";
                break;
            case R.id.gd_b:
                gdButton.setBackgroundResource(R.drawable.yl_share_button);
                gdButton.setTextColor(getResources().getColor(R.color.white));
                jxdButton.setBackgroundResource(R.drawable.yl_share_button2);
                jxdButton.setTextColor(getResources().getColor(R.color.unitform_button_red));
                ddButton.setBackgroundResource(R.drawable.yl_share_button2);
                ddButton.setTextColor(getResources().getColor(R.color.unitform_button_red));
                selectNd = "3";
                break;
            case R.id.bx_b:
                zzEditText.setText("不详");
                bxButton.setTextAppearance(activity, R.style.yl_sc_button_select);
//                bxButton.setBackgroundResource(R.drawable.yl_share_button4);
                wkButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
//                wkButton.setBackgroundResource(R.drawable.yl_share_button3);
                break;
            case R.id.wk_b:
                zzEditText.setText("无款");
                wkButton.setTextAppearance(activity, R.style.yl_sc_button_select);
//                wkButton.setBackgroundResource(R.drawable.yl_share_button4);
                bxButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
//                bxButton.setBackgroundResource(R.drawable.yl_share_button3);
                break;
            case R.id.zb_b:
                zdEditText.setText("纸本");
                zbButton.setTextAppearance(activity, R.style.yl_sc_button_select);
                jbButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
                lbButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
                break;
            case R.id.jb_b:
                zdEditText.setText("绢本");
                jbButton.setTextAppearance(activity, R.style.yl_sc_button_select);
                zbButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
                lbButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
                break;
            case R.id.lb_b:
                zdEditText.setText("绫本");
                lbButton.setTextAppearance(activity, R.style.yl_sc_button_select);
                jbButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
                zbButton.setTextAppearance(activity, R.style.yl_sc_button_unselect);
                break;
//            case R.id.zjsp_b:
////                intent.setClass(activity, YlScDzxysActivity.class);
////                startActivity(intent);
//                qrscLayout.setVisibility(View.GONE);
//                zjspLayout.setVisibility(View.VISIBLE);
//                break;
            case R.id.right_rl://直接上拍中的确实上传
                if(list.size()==1){
                    toastShow("至少要上传一张图片！");
                    return;
                }
                String zz=zzEditText.getText().toString();
                String zd=zdEditText.getText().toString();
                String cd=cdEditText.getText().toString();
                String kd=kdEditText.getText().toString();
                if(zz.equals("")||zd.equals("")||cd.equals("")||kd.equals("")){
                    toastShow("作者、质地、尺寸必须填写完整");
                    return;
                }
                if(qpjEditText.getText().toString().equals("")){
                    toastShow("请填写起拍价！");
                    return;
                }
                
                try {
                    JSONObject jsonObject=new JSONObject();
                    JSONArray jsonArray=new JSONArray();
                    for(Map<String, Object> m:list.subList(0, list.size()-1)){
                        JSONObject object=new JSONObject();
                        object.put("path",PaseJson.getMapMsg(m, "path"));
                        jsonArray.put(object);
                    }
                    jsonObject.put("paths", jsonArray);
                    jsonObject.put("zz",zzEditText.getText().toString() );
                    jsonObject.put("zd",zdEditText.getText().toString() );
                    jsonObject.put("cd",cdEditText.getText().toString() );
                    jsonObject.put("kd",kdEditText.getText().toString() );
                    jsonObject.put("xq", xqEditText.getText().toString());
                    jsonObject.put("qpj",qpjEditText.getText().toString() );
                    jsonObject.put("fblx", selectNd);
                    PropertiesUtil.write(activity, PropertiesUtil.SCSP, jsonObject.toString());
                    PropertiesUtil.write(activity, PropertiesUtil.QPJ, qpjEditText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.setClass(activity, PmScDzxysActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 判断确认上传是否可用
     */
    private void juageQrsc(){
//        String zz=zzEditText.getText().toString();
//        String zd=zdEditText.getText().toString();
//        String cd=cdEditText.getText().toString();
//        String kd=kdEditText.getText().toString();
//        if(!zz.equals("")&&!zd.equals("")&&!cd.equals("")&&!kd.equals("")){
//            qrscButton.setBackgroundResource(R.drawable.login_share_button);
//            qrscButton.setEnabled(true);
//        }else{
//            qrscButton.setBackgroundResource(R.drawable.login_share_button2);
//            qrscButton.setEnabled(false);
//        }
    }
    /**
     * 确认上传
     */
    class qrscNetWork implements INetWork{
        private String path;
        private String isZjsp;//是否直接上拍
        public qrscNetWork(String str,String isZjsp) {
            this.path=str;
            this.isZjsp=isZjsp;
        }
        @Override
        public boolean validate() {
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("arttalk/pushToArttalk.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("author",zzEditText.getText().toString() );
            data.addData("num","4");
            data.addData("mea_len",cdEditText.getText().toString() );
            data.addData("mea_wide", kdEditText.getText().toString());
            data.addData("texture", zdEditText.getText().toString());
            data.addData("xs", "1");
            data.addData("fbtype", selectNd);
            data.addData("is_ars", isZjsp);
            data.addData("startprice", "");
            data.addData("source", "1");
            data.addData("description", xqEditText.getText().toString());
            data.addData("path", path);
            return data;
        }
        
        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject.getString("code").equals("0")){
                toastShow("发送成功！");
                Intent intent=new Intent("QZSX");
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                list.clear();
                finish();
            }else{
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
                list.addAll(0,(List<Map<String, Object>>)arg2.getExtras().getSerializable("list"));
                tpAdapter.notifyDataSetChanged();
            }
        }
    }
}
