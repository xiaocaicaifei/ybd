package com.ybd.yl.login;

import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 用户注册-设置密码
 * @author cyf
 * @version $Id: LoginActivity.java, v 0.1 2015-10-27 下午5:30:45 cyf Exp $
 */
public class RegistSzmmActivity extends BaseActivity implements OnClickListener {
    private Button   xybButton;   //下一步按钮
    private EditText mmEditText;   //密码
    private EditText qrmmEditText; //确认密码

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.regist_szmm);
        initPublicView("设置密码");
        initView();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        mmEditText = (EditText) findViewById(R.id.regist_mm_et);
        qrmmEditText = (EditText) findViewById(R.id.regist_qrmm_et);
        xybButton = (Button) findViewById(R.id.login_xyb_b);
        xybButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {

        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.login_xyb_b:
                //                intent.setClass(activity, RegistCjgrzlActivity.class);
                //                startActivity(intent);
                NetWork.submit(activity, saveUserPassword);
                break;
        }
    }

    /**
     * 保存用户的密码
     */
    INetWork saveUserPassword = new INetWork() {

                                  @Override
                                  public boolean validate() {
                                      String mm = mmEditText.getText().toString();
                                      String qrmm = qrmmEditText.getText().toString();
                                      if (mm.length() < 6 || qrmm.length() < 6) {
                                          toastShow("密码长度必须是6~18位");
                                          return false;
                                      }
                                      if (!mm.equals(qrmm)) {
                                          toastShow("密码和确认密码不一致");
                                          return false;
                                      }
                                      return true;
                                  }

                                  @Override
                                  public Data getSubmitData() throws Exception {
                                      Data data = new Data("register/initUser.json");
                                      data.addData("wbopen_id", "");
                                      data.addData("wxopen_id", "");
                                      String mobile = "";
                                      if (activity.getIntent().hasExtra("phone")) {
                                          mobile = activity.getIntent().getExtras()
                                              .getString("phone");
                                      }
                                      data.addData("mobile", mobile);
                                      data.addData("randNum", activity.getIntent().getExtras().getString("yzm"));
                                      data.addData("password", mmEditText.getText().toString());
                                      return data;
                                  }

                                  @SuppressWarnings("unchecked")
                                  @Override
                                  public void result(String result) throws Exception {
                                      Map<String, Object> map = (Map<String, Object>) PaseJson
                                          .paseJsonToObject(result);
                                      if (map.get("code").toString().equals("0")) {
                                          Intent intent = new Intent();
                                          intent.setClass(activity, RegistCjgrzlActivity.class);
                                          startActivity(intent);
                                      } else if (map.get("code").toString().equals("-1")) {
                                          toastShow(map.get("msg").toString());
                                      }
                                      Map<String, Object> map2 = (Map<String, Object>) map
                                          .get("data");
                                      if (map2 != null) {
                                          PropertiesUtil.write(activity, PropertiesUtil.USERID,
                                              PaseJson.getMapMsg(map2, "user_id"));
                                      }
                                      //            else if(map.get("code").toString().equals("2")){
                                      //                Intent intent=new Intent();
                                      //                intent.setClass(activity, RegistSzmmActivity.class);
                                      //                startActivity(intent);
                                      //            }
                                      //            Map<String, Object> map2=(Map<String, Object>) map.get("data");
                                      //            if(map2!=null){
                                      //                PropertiesUtil.write(activity, PropertiesUtil.USERID,PaseJson.getMapMsg(map2, "userId"));
                                      //                PropertiesUtil.write(activity, PropertiesUtil.NICKNAME, PaseJson.getMapMsg(map2, "nickname"));
                                      //                PropertiesUtil.write(activity, PropertiesUtil.HEADIMGURL, PaseJson.getMapMsg(map2, "headimgurl"));
                                      //            }
                                  }
                              };
}
