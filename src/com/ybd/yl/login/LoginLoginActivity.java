package com.ybd.yl.login;

import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.FileUtils;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.tools.StringUtil;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.qz.QzIndexActivity;
import com.ybd.yl.service.ReceiverService;

/**
 * 用户登录
 * @author cyf
 * @version $Id: LoginActivity.java, v 0.1 2015-10-27 下午5:30:45 cyf Exp $
 */
public class LoginLoginActivity extends BaseActivity implements OnClickListener {
    private TextView wjmmTextView;
    private Button   loginButton;  //登录按钮

    private EditText zhEditText;   //账号
    private EditText mmEditText;   //密码

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.login_login);
        initPublicView("登录");
        init();
        //        setStateColor(2);
    }

    /**
     * 初始化页面控件
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    private void init() {
        zhEditText = (EditText) findViewById(R.id.login_zh);
        mmEditText = (EditText) findViewById(R.id.login_mm);
        loginButton = (Button) findViewById(R.id.login_login_b);
        loginButton.setOnClickListener(this);
        wjmmTextView = (TextView) findViewById(R.id.login_wjmm_tv);
        wjmmTextView.setOnClickListener(this);
        
        zhEditText.setText(PropertiesUtil.read(activity, PropertiesUtil.ACCOUNT));
        mmEditText.setText(PropertiesUtil.read(activity, PropertiesUtil.PASSWORD));
        
//        zhEditText.setText("18339966923");
//        mmEditText.setText("abc123456");
    }

    @Override
    public void onClick(View arg0) {

        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.login_wjmm_tv:
                intent.setClass(activity, LoginZhmmActivity.class);
                startActivity(intent);
                break;
            case R.id.login_login_b:
//                intent.setClass(activity, NavigationActivity.class);
//                startActivity(intent);
                NetWork.submit(activity, new LoginNet());
                break;
        }
    }

    /**
     * 登录
     * 
     * @author cyf
     * @version $Id: Login.java, v 0.1 Apr 19, 2013 6:00:28 PM cyf Exp $
     */
    private class LoginNet implements INetWork {

        @Override
        public boolean validate() {
            if (StringUtil.isBlank(zhEditText.getText().toString())) {
                toastShow("请输入用户名");
                return false;
            }
            if (StringUtil.isBlank(mmEditText.getText().toString())) {
                toastShow("请输入密码");
                return false;
            }
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data d = new Data("register/login.json");
            d.addData("mobile", zhEditText.getText().toString());
            d.addData("password", mmEditText.getText().toString());
            return d;
        }
        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) {
            Map<String, Object> map = ((Map<String, Object>) PaseJson.paseJsonToObject(result));
            Map<String, Object> data=(Map<String, Object>) map.get("data");
            Intent intent = new Intent();
            if (map.get("code").toString().equals("3")) {
                intent.setClass(activity, QzIndexActivity.class);
                startActivity(intent);
//                finish();
                //保存用户信息
                PropertiesUtil.write(activity, PropertiesUtil.USERID,
                    PaseJson.getMapMsg(data, "id"));
                PropertiesUtil.write(activity, PropertiesUtil.ACCOUNT, zhEditText.getText().toString());
                PropertiesUtil.write(activity, PropertiesUtil.PASSWORD, mmEditText.getText().toString());
                PropertiesUtil.write(activity, PropertiesUtil.NICKNAME,
                    PaseJson.getMapMsg(data, "nick_name"));
                PropertiesUtil.write(activity, PropertiesUtil.VOIPACCOUNT,
                    PaseJson.getMapMsg(data, "voipAccount"));
                PropertiesUtil.write(activity, PropertiesUtil.HEADIMGURL,
                    PaseJson.getMapMsg(data, "icon_url"));
                Intent service=new Intent();
                service.putExtra("loginZh", PaseJson.getMapMsg(data, "subAccountSid"));
                service.setClass(activity, ReceiverService.class);
                startService(service);
//                PropertiesUtil.write(activity, PropertiesUtil.HEADIMGURL,
//                    PaseJson.getMapMsg(data, "icon_url"));

            } else if (map.get("code").toString().equals("4")){
                toastShow("账号或密码错误！");
            } else if (map.get("code").toString().equals("2")){
                intent.setClass(activity, RegistCjgrzlActivity.class);
                startActivity(intent);
                //保存用户信息
                PropertiesUtil.write(activity, PropertiesUtil.USERID,
                    PaseJson.getMapMsg(data, "id"));
               
            } else if (map.get("code").toString().equals("1")){
                toastShow("该用户不存在，请先注册！");
            }else {
                toastShow("账号或密码错误！");
            }
        }

    }

    /**
     * 更新客户端
     * 
     * @author zn
     * @version $Id: Login.java, v 0.1 2013-5-14 上午7:26:49 zn Exp $
     */
    //    private class updateClient implements INetWork {
    //
    //        @Override
    //        public boolean validate() {
    //            // 检查是否有新客户端
    ////          String last = PropertiesUtil.read(MainActivity.this, LAST_UPTIME);
    ////          long interval = -1;// 间隔时间
    ////          if (null != last) {
    ////              interval = DateUtil.getMillis(1, DateUtil.TimeUnits.d);
    ////          }
    ////          Log.v("dddd", "last:"+last);
    ////          if (interval == -1 ||System.currentTimeMillis() - Long.valueOf(last) > interval) {
    //                return true;
    ////          }
    ////          return false;
    //        }
    //
    //        @Override
    //        public Data getSubmitData() throws Exception {
    //            Data data = new Data("getVersion.json");
    //            return data;
    //        }
    //
    //        @Override
    //        public void result(String result) throws Exception {
    //            final JSONObject jsonObj = new JSONObject(result).getJSONObject("data");
    //            if (jsonObj.has("code")) {
    //                if (jsonObj.getString("code").equals("null")) {
    //                    return;
    //                }
    //                float value = Float.parseFloat(jsonObj.getString("code"));
    //                // 当前版本号小于最新版本，提示下载
    //                if (getVersionCode() < value) {
    //                    // final String id = jsonObj.getString("id");
    //                    ICallback callback = new ICallback() {
    //
    //                        @Override
    //                        public void callback(Context ctx) {
    //                            Data data = new Data("downApk");
    //                            // data.addData("id", id);
    //                            NetWork.downloadAndOpen(MainActivity.this, data, DirType.CLIENT, "client.apk");
    //                            // 记录客户端更新时间
    //                            String last = String.valueOf(System.currentTimeMillis());
    //                            PropertiesUtil.write(ctx, LAST_UPTIME, last);
    //                        }
    //                    };
    //                    if (jsonObj.has("code") && jsonObj.getString("isforce").equals("Y")) {
    //                        DialogUtil.confirm_ok(MainActivity.this, "新版本需要更新，否则影响使用", callback);
    //                    } else {
    //                        DialogUtil.confirm(MainActivity.this, "发现新版本，是否下载".concat("?\n新版本说明：\n").concat(jsonObj.getString("desc")), callback, new ICallback() {
    //                            @Override
    //                            public void callback(Context ctx) {
    //                                // 取消以后记录时间，更新频率的下一次（即24小时后再提示）
    //                                String last = String.valueOf(System.currentTimeMillis());
    //                                PropertiesUtil.write(ctx, LAST_UPTIME, last);
    //                            }
    //                        });
    //                    }
    //                }
    //            }
    //        }
    //    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == 0) {
                Uri uri = arg2.getData();
                final String picPath = FileUtils.getPath2(activity, uri);
                //                NetWork.submit(activity, new serverSubmitFjType(FileTypeEnums.RWGLXPFJ.name(),picPath,new StateValueChange() {
                //                    @Override
                //                    public void onStateChange(boolean state) {
                //                        if(state){
                //                            NetWork.submit(activity, new UploadFile(picPath));
                //                        }
                //                    }
                //                }));
                NetWork.submit(activity, new UploadFile(picPath));
            }
        }
    }

    /**
     * 上传附件
     * @author Administrator
     *
     */
    private class UploadFile implements INetWork {
        private String picPath;

        @Override
        public boolean validate() {
            return true;
        }

        public UploadFile(String picPath) {
            this.picPath = picPath;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data d = new Data("circle/pushToCircle.json");
            d.addData("user_id", "0000000000");
            d.addData("address", "0000000000");
            d.addData("fbtype", "0000000000");
            d.addData("description", "0000000000");
            d.addData("path", picPath);
            d.addPath(picPath);
            return d;
        }

        @Override
        public void result(String result) throws Exception {
            //                JSONObject o = new JSONObject(result);
            //                if (o.getBoolean("state")) {
            //                    NetWork.submit(activity, serverSubmitFj);
            //                }
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
        }
    }
}
