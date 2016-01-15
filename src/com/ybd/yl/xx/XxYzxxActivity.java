package com.ybd.yl.xx;

import java.util.Map;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 消息-验证消息
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class XxYzxxActivity extends BaseActivity implements OnClickListener {

    private ImageView                 txImageView;                                       //头像
    private ImageView                 zjImageView;                                       //专家标志
    private TextView                  titleTextView;                                     //用户的你能
    private ImageView                 dvImageView;                                       //大V标志
    private TextView                  xgbzTextView;                                      //修改备注
    private TextView                  ddTextView;                                        //地点
    private TextView                  qmTextView;                                        //签名
    ImageLoader                       imageLoader = ImageLoader.getInstance();
    private Button                    hlButton;                                          //忽略
    private Button                    jrhmdButton;                                        //加入黑名单
    private Button                    tgyzButton;                                        //通过验证
    private Map<String, Object>       map;                                                //当前查看用户的对象

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_yzxx);
        initPublicView("验证消息", R.drawable.login_fh, 0, null, null);
        init();
        NetWork.submit(activity, grzlINetWork);
    }

    /**
     * 初始化控件
     */
    private void init() {
        txImageView = (ImageView) findViewById(R.id.tx_iv);
        zjImageView = (ImageView) findViewById(R.id.zj_iv);
        titleTextView = (TextView) findViewById(R.id.title_tv);
        dvImageView = (ImageView) findViewById(R.id.dv_iv);
        xgbzTextView = (TextView) findViewById(R.id.xgbz_tv);
        xgbzTextView.setOnClickListener(this);
        ddTextView = (TextView) findViewById(R.id.dd_tv);
        qmTextView = (TextView) findViewById(R.id.qm_tv);
        hlButton = (Button) findViewById(R.id.hl_b);
        hlButton.setOnClickListener(this);
        jrhmdButton = (Button) findViewById(R.id.jrhmd_b);
        jrhmdButton.setOnClickListener(this);
        tgyzButton = (Button) findViewById(R.id.tgyz_b);
        tgyzButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            
            case R.id.hl_b:
                    NetWork.submit(activity, new QqNetWork("3"));
                break;
            case R.id.swzj_b:
                NetWork.submit(activity, new pbNetwork("true"));
                break;
            case R.id.tgyz_b:
                NetWork.submit(activity, new QqNetWork("2"));
                break;
            default:
                break;
        }
    }

    private INetWork grzlINetWork = new INetWork() {

                                      @Override
                                      public boolean validate() {
                                          return true;
                                      }

                                      @Override
                                      public Data getSubmitData() throws Exception {
                                          Data data = new Data("auser/loadUserByUserId.json");
                                          data.addData("user_id",
                                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                                          data.addData("brelate_id", XxYzxxActivity.this
                                              .getIntent().getExtras().getString("bcrUserId"));
                                          return data;
                                      }

                                      @SuppressWarnings("unchecked")
                                      @Override
                                      public void result(String result) throws Exception {
                                          map = (Map<String, Object>) ((Map<String, Object>) PaseJson
                                              .paseJsonToObject(result)).get("data");
                                          if (PaseJson.getMapMsg(map, "icon_url").equals("")) {
                                              imageLoader.displayImage(
                                                  PaseJson.getMapMsg(map, "icon_url"), txImageView,
                                                  MainApplication.getRoundOptions());
                                          }
                                          if (PaseJson.getMapMsg(map, "is_export").equals("0")) {
                                              zjImageView.setVisibility(View.INVISIBLE);
//                                              swzjButton.setText("设为专家");
                                          } else {
                                              zjImageView.setVisibility(View.GONE);
//                                              swzjButton.setText("取消专家");
                                          }
//                                          if (PaseJson.getMapMsg(map, "is_black").equals("0")) {
//                                              pbButton.setText("屏蔽");
//                                          } else {
//                                              pbButton.setText("取消屏蔽");
//                                          }
                                          if (PaseJson.getMapMsg(map, "is_bv").equals("0")) {
                                              dvImageView.setVisibility(View.INVISIBLE);
                                          } else {
                                              dvImageView.setVisibility(View.GONE);
                                          }
                                          titleTextView.setText(PaseJson
                                              .getMapMsg(map, "nick_name"));
                                          ddTextView.setText(PaseJson.getMapMsg(map, "address"));
                                          qmTextView.setText(PaseJson.getMapMsg(map, "new_sign"));
                                      }
                                  };
    /**
     * 同意或者忽略用户请求
     */
    class QqNetWork implements INetWork {
        String state;
        public QqNetWork(String state) {
            this.state=state;
        }
                                      @Override
                                      public boolean validate() {
                                          return true;
                                      }

                                      @Override
                                      public Data getSubmitData() throws Exception {
                                          Data data = new Data("myfriend/agreeFriend.json");
                                          data.addData("user_id",
                                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                                          data.addData("fuser_id",
                                              PaseJson.getMapMsg(map, "user_id"));
                                          data.addData("state",state );
                                          data.addData("audit_note", "");
                                          return data;
                                      }

                                      @Override
                                      public void result(String result) throws Exception {
                                          JSONObject object = new JSONObject(result);
                                          if (object.getString("code").equals("0")) {
                                              toastShow("操作成功！");
                                              NetWork.submit(activity, grzlINetWork);
                                          } else {
                                              toastShow("操作失败");
                                          }
                                      }

                                  };
                                  /**
                                   * 屏蔽用户
                                   * 
                                   * @author cyf
                                   * @version $Id: QzXxzlActivity.java, v 0.1 2015-12-24 下午3:49:37 cyf Exp $
                                   */
                                  class pbNetwork  implements INetWork {
                                      private String filter;
                                      public pbNetwork(String filter) {
                                          this.filter=filter;
                                    }
                                      @Override
                                      public boolean validate() {
                                          return true;
                                      }

                                      @Override
                                      public Data getSubmitData() throws Exception {
                                          Data data = new Data("blackrecord/blackSet.json");
                                          data.addData("relate_id",
                                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                                          data.addData("brelate_id",
                                              PaseJson.getMapMsg(map, "user_id"));
                                          data.addData("is_filter", filter);
                                          return data;
                                      }

                                      @Override
                                      public void result(String result) throws Exception {
                                          JSONObject object = new JSONObject(result);
                                          if (object.getString("code").equals("0")) {
                                              toastShow("操作成功！");
                                              NetWork.submit(activity, grzlINetWork);
                                          } else {
                                              toastShow("操作失败");
                                          }
                                      }

                                  };
}
