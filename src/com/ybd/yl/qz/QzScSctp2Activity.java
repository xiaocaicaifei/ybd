package com.ybd.yl.qz;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 圈子-上传-上传图片(单纯的文字上传)
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class QzScSctp2Activity extends BaseActivity implements OnClickListener {
    private EditText nrEditText;//内容

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.qz_sc_sctp2);
        initPublicView("上传图片", R.drawable.login_fh, "发送", null, this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        nrEditText=(EditText) findViewById(R.id.nr_et);
    }
    
    
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.right_rl:
               NetWork.submit(activity, new qrscNetWork(""));//不上传图片
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
                toastShow("保存成功！");
                finish();
            }else{
                toastShow("保存失败！");
            }
        }
    };
}
