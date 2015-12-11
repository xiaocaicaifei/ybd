package com.ybd.yl.login;

import java.util.Map;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.ImageUtil;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.common.SelectPhotoActivity;
import com.ybd.yl.dialog.DialogSelectPosition;
import com.ybd.yl.home.NavigationActivity;

/**
 * 用户登录-创建个人资料
 * @author cyf
 * @version $Id: LoginActivity.java, v 0.1 2015-10-27 下午5:30:45 cyf Exp $
 */
public class RegistCjgrzlActivity extends BaseActivity implements OnClickListener {

    private ImageView txImageView; //用户头像
    private EditText  ncEditText;  //昵称
    private EditText  xbEditText;  //性别
    private EditText  wzEditText;  //位置
    private ImageView xbImageView; //性别
    private Button loginButton;//登录
    private String tempPhotoUrl;//上次图片给出的临时图片路径
    String selectPositionCode="";

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.regist_cjgrzl);
        initPublicView("创建个人资料");
        init();
    }

    /**
     * 初始化页面控件
     * 
     */
    private void init() {
        txImageView = (ImageView) findViewById(R.id.tx_iv);
        txImageView.setBackgroundResource(R.drawable.regist_cjgrzl_tx);
        txImageView.setOnClickListener(this);
        ncEditText = (EditText) findViewById(R.id.nc_et);
        xbEditText = (EditText) findViewById(R.id.xb_et);
        xbEditText.setText("男");
        wzEditText = (EditText) findViewById(R.id.wz_et);
        wzEditText.setOnClickListener(this);
        xbImageView = (ImageView) findViewById(R.id.xb_iv);
        xbImageView.setOnClickListener(this);
        loginButton=(Button) findViewById(R.id.login_login_b);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {

        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.wz_et:
                intent.setClass(activity, DialogSelectPosition.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.xb_iv:
                if (xbEditText.getText().toString().equals("男")) {
                    xbImageView.setBackgroundResource(R.drawable.regist_cjgrzl_close);
                    xbEditText.setText("女");
                } else {
                    xbImageView.setBackgroundResource(R.drawable.regist_cjgrzl_open);
                    xbEditText.setText("男");
                }
                break;
            case R.id.tx_iv:
                intent.setClass(activity, SelectPhotoActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.login_login_b:
                NetWork.submit(activity, saveUserMsg);
                break;
        }
    }

    /**
     * 上传附件
     * @author Administrator
     *
     */
    private class UploadFile implements INetWork {
        private String picPath;

        public UploadFile(String picPath) {
            this.picPath = picPath;
        }
        @Override
        public boolean validate() {
            return true;
        }
        @Override
        public Data getSubmitData() throws Exception {
            Data d = new Data("fs/uploadFile.json");
            d.addData("temp", "");
//            d.addData("path", picPath);
            d.addPath(picPath);
            return d;
        }

        @Override
        public void result(String result) throws Exception {
            @SuppressWarnings("unchecked")
            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
            if(map.get("code").equals("0")){
                String []str=(String[]) map.get("fileList");
                tempPhotoUrl=str[0];
            }else{
                toastShow("上传图片失败！");
            }
        }
    }
    /**
     * 保存用户信息
     * @see 
     */
    INetWork saveUserMsg=new INetWork() {
        
        @Override
        public boolean validate() {
            if(ncEditText.getText().toString().equals("")){
                toastShow("用户昵称不能为空！");
                return false;
            }else if(wzEditText.getText().toString().equals("")){
                toastShow("请选择位置信息！");
                return false;
            }
            
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("register/updateUserDetail.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("icon_url", tempPhotoUrl);
            data.addData("nick_name", ncEditText.getText().toString());
            data.addData("sex", xbEditText.getText().toString().equals("女")?"2":"1");
            data.addData("prefix_code", selectPositionCode);
            data.addData("address", wzEditText.getText().toString());
            return data;
        }
        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String , Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
            if(map.get("code").equals("0")){
                Intent intent=new Intent();
                intent.setClass(activity, NavigationActivity.class);
                startActivity(intent);
            }else{
                toastShow("修改个人信息失败！");
            }
        }

    };
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == 0) {
                wzEditText.setText(arg2.getExtras().getString("name"));
                selectPositionCode=arg2.getExtras().getString("code");
            }else if(arg0==1){
                String path=arg2.getExtras().getString("path");
                NetWork.submit(activity, new UploadFile(path));
                txImageView.setImageBitmap(ImageUtil.toRoundBitmap2(ImageUtil.getLoacalBitmap(path)));
            }
        }
    }
}
