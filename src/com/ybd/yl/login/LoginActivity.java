package com.ybd.yl.login;

import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.FileUtils;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 快捷登录注册接口
 * @author cyf
 * @version $Id: LoginActivity.java, v 0.1 2015-10-27 下午5:30:45 cyf Exp $
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private ImageView loginShowImageView;                     //首页的展示图
    private ImageView wbImageView;                            //微信登录按钮
    private ImageView wxImageView;                            //登录按钮
    private Button    dlButton;                               //登录按钮
    private Button    zcButton;                               //注册按钮
    ImageLoader       imageLoader = ImageLoader.getInstance();

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.login);
//        initPublicView("登录");
        //        setStateColor(1);
        init();
        NetWork.submit(activity, new GetPhoto());
    }

    /**
     * 初始化界面信息
     */
    private void init() {
        loginShowImageView = (ImageView) findViewById(R.id.login_show_image);
        wbImageView = (ImageView) findViewById(R.id.login_wb);
        wbImageView.setOnClickListener(this);
        wxImageView = (ImageView) findViewById(R.id.login_wx);
        wxImageView.setOnClickListener(this);
        dlButton = (Button) findViewById(R.id.login_dl);
        dlButton.setOnClickListener(this);
        zcButton = (Button) findViewById(R.id.login_zc);
        zcButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {

        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.login_dl:
                intent.setClass(activity, LoginLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.login_zc:
                PropertiesUtil.write(activity, PropertiesUtil.LOGINTYPE, "1");
                intent.setClass(activity, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.login_wb:
                PropertiesUtil.write(activity, PropertiesUtil.LOGINTYPE, "2");
                intent.setClass(activity, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.login_wx:
                PropertiesUtil.write(activity, PropertiesUtil.LOGINTYPE, "3");
                intent.setClass(activity, RegistActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 查询显示图片的信息
     */
    class GetPhoto implements INetWork {
        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("versions/loadVersions.json");
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> map = (Map<String, Object>) PaseJson.paseJsonToObject(result);
            Map<String, Object> map2 = (Map<String, Object>) map.get("data");
            String[] path = (String[]) map2.get("photos");
            imageLoader.displayImage(C.IP + path[0], loginShowImageView,
                MainApplication.getOptions());
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
                //                NetWork.submit(activity, new UploadFile(picPath));
            }
        }
    }

    //    /**
    //     * 上传附件
    //     * @author Administrator
    //     *
    //     */
    //    private class UploadFile implements INetWork {
    //        private String picPath;
    //
    //        @Override
    //        public boolean validate() {
    //            return true;
    //        }
    //
    //        public UploadFile(String picPath) {
    //            this.picPath = picPath;
    //        }
    //
    //        @Override
    //        public Data getSubmitData() throws Exception {
    //            Data d = new Data("circle/pushToCircle.json");
    //            d.addData("user_id", "0000000000");
    //            d.addData("address", "0000000000");
    //            d.addData("fbtype", "0000000000");
    //            d.addData("description", "0000000000");
    //            d.addData("path", picPath);
    //            d.addPath(picPath);
    //            return d;
    //        }
    //
    //        @Override
    //        public void result(String result) throws Exception {
    //            //                JSONObject o = new JSONObject(result);
    //            //                if (o.getBoolean("state")) {
    //            //                    NetWork.submit(activity, serverSubmitFj);
    //            //                }
    //            Log.v("dddd", result);
    //            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
    //        }
    //    }
}
