package com.ybd.yl.yl;

import org.json.JSONObject;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 艺论-投诉
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class YlTsActivity extends BaseActivity implements OnClickListener {

    private String tslx="1";//1,2,3,4,5,6,7顺序对应上下顺序，最后一个是其他，
    private String tsContent="";//用户不选择，填写投诉的内容
    private RelativeLayout ggsrLayout;
    private RadioButton ggsrRadioButton;
    
    private RelativeLayout zzmgLayout;
    private RadioButton zzmgRadioButton;
    
    private RelativeLayout sqdsLayout;
    private RadioButton sqdsRadioButton;
    
    private RelativeLayout lhpbLayout;
    private RadioButton lhpbRadioButton;
    
    private RelativeLayout qzpqLayout;
    private RadioButton qzpqRadioButton;
    
    private RelativeLayout wfLayout;
    private RadioButton wfRadioButton;
    
    private EditText qtwtmsEditText;

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.yl_ts);
        initPublicView("用户投诉", R.drawable.login_fh, "发送",null, this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        ggsrLayout=(RelativeLayout) findViewById(R.id.ggsr_ll);
        ggsrLayout.setOnClickListener(this);
        ggsrRadioButton=(RadioButton) findViewById(R.id.ggsr_rb);
        
        zzmgLayout=(RelativeLayout) findViewById(R.id.zzmg_ll);
        zzmgLayout.setOnClickListener(this);
        zzmgRadioButton=(RadioButton) findViewById(R.id.zzmg_rb);
        
        sqdsLayout=(RelativeLayout) findViewById(R.id.sqds_ll);
        sqdsLayout.setOnClickListener(this);
        sqdsRadioButton=(RadioButton) findViewById(R.id.sqds_rb);
        
        lhpbLayout=(RelativeLayout) findViewById(R.id.lhpb_ll);
        lhpbLayout.setOnClickListener(this);
        lhpbRadioButton=(RadioButton) findViewById(R.id.lhpb_rb);
        
        qzpqLayout=(RelativeLayout) findViewById(R.id.qzpq_ll);
        qzpqLayout.setOnClickListener(this);
        qzpqRadioButton=(RadioButton) findViewById(R.id.qzpq_rb);
        
        wfLayout=(RelativeLayout) findViewById(R.id.wf_ll);
        wfLayout.setOnClickListener(this);
        wfRadioButton=(RadioButton) findViewById(R.id.wf_rb);
        qtwtmsEditText=(EditText) findViewById(R.id.qtwtms_et);
        qtwtmsEditText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                    judgeSelect(0);
                    tslx="7";
                }
                tsContent=s.toString();
            }
        });
    }

    /**
     * 判断该选中哪一个
     * @param num
     */
    private void judgeSelect(int num){
        ggsrRadioButton.setChecked(false);
        zzmgRadioButton.setChecked(false);
        sqdsRadioButton.setChecked(false);
        lhpbRadioButton.setChecked(false);
        qzpqRadioButton.setChecked(false);
        wfRadioButton.setChecked(false);
        switch (num) {
            case 1:
                ggsrRadioButton.setChecked(true);
                break;
            case 2:
                zzmgRadioButton.setChecked(true);
                break;
            case 3:
                sqdsRadioButton.setChecked(true);
                break;
            case 4:
                lhpbRadioButton.setChecked(true);
                break;
            case 5:
                qzpqRadioButton.setChecked(true);
                break;
            case 6:
                wfRadioButton.setChecked(true);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.right_rl://头部右侧按钮的发送
                NetWork.submit(activity, tsNetWork);
                break;
            case R.id.ggsr_ll:
                judgeSelect(1);
                tslx="1";
                tsContent="";
                qtwtmsEditText.setText("");
                break;
            case R.id.zzmg_ll:
                judgeSelect(2);
                tslx="2";
                tsContent="";
                qtwtmsEditText.setText("");
                break;
            case R.id.sqds_ll:
                judgeSelect(3);
                tslx="3";
                tsContent="";
                qtwtmsEditText.setText("");
                break;
            case R.id.lhpb_ll:
                judgeSelect(4);
                tslx="4";
                tsContent="";
                qtwtmsEditText.setText("");
                break;
            case R.id.qzpq_ll:
                judgeSelect(5);
                tslx="5";
                tsContent="";
                qtwtmsEditText.setText("");
                break;
            case R.id.wf_ll:
                judgeSelect(6);
                tslx="6";
                tsContent="";
                qtwtmsEditText.setText("");
                break;
            default:
                break;
        }
    }
    
    private INetWork tsNetWork=new INetWork() {
        
        @Override
        public boolean validate() {
            if(tslx.equals("7")&&tsContent.equals("")){
                toastShow("请选择或输入投诉类型！");
                return false;
            }
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("complain/complain.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("circle_id", "");
            data.addData("arttalk_id", activity.getIntent().getExtras().getString("ylid"));
            data.addData("tstype", tslx);
            data.addData("content", tsContent);
            return data;
        }
        
        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject.getString("code").equals("0")){
                toastShow("投诉成功！");
                finish();
            }else{
                toastShow("投诉失败！");
            }
        }
        
    };

}
