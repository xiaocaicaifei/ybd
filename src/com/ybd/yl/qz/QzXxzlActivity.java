package com.ybd.yl.qz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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
import com.ybd.yl.gr.GrIndexAdapter;

/**
 * 圈子-详细资料
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class QzXxzlActivity extends BaseActivity implements OnClickListener {

    private ImageView                 txImageView;                                       //头像
    private ImageView                 zjImageView;                                       //专家标志
    private TextView                  titleTextView;                                     //用户的你能
    private ImageView                 dvImageView;                                       //大V标志
    private TextView                  xgbzTextView;                                      //修改备注
    private TextView                  ddTextView;                                        //地点
    private TextView                  qmTextView;                                        //签名
    private TextView                  xyTextView;                                        //信用的值
    private TextView                  gmTextView;                                        //购买的值
    private TextView                  mcTextView;                                        //卖出的值
    private TextView                  fsTextView;                                        //粉丝的值
    private TextView                  sjTextView;                                        //手机的值
    private TextView                  yxTextView;                                        //邮箱地点值
    private GridView                  xcGridView;
    BaseAdapter                       xcAdapter;
    ImageLoader                       imageLoader = ImageLoader.getInstance();
    private Button                    pbButton;                                          //屏蔽
    private Button                    swzjButton;                                        //设为专家
    private Button                    fsxxButton;                                        //发送消息
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Map<String, Object>       map;                                                //当前查看用户的对象

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.qz_xxzl);
        initPublicView("详细资料", R.drawable.login_fh, 0, null, null);
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
        xyTextView = (TextView) findViewById(R.id.xy_value_tv);
        gmTextView = (TextView) findViewById(R.id.gm_value_tv);
        mcTextView = (TextView) findViewById(R.id.mc_value_tv);
        fsTextView = (TextView) findViewById(R.id.fs_value_tv);
        sjTextView = (TextView) findViewById(R.id.sj_tv);
        yxTextView = (TextView) findViewById(R.id.yx_tv);
        xcGridView = (GridView) findViewById(R.id.xc_gv);
        xcAdapter = new GrIndexAdapter(list, activity);
        xcGridView.setAdapter(xcAdapter);
        xcAdapter.notifyDataSetChanged();
        pbButton = (Button) findViewById(R.id.pb_b);
        pbButton.setOnClickListener(this);
        swzjButton = (Button) findViewById(R.id.swzj_b);
        swzjButton.setOnClickListener(this);
        fsxxButton = (Button) findViewById(R.id.fsxx_b);
        fsxxButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.xgbz_tv:

                break;
            case R.id.pb_b:
                if (PaseJson.getMapMsg(map, "is_black").equals("0")) {
                    NetWork.submit(activity, new pbNetwork("true"));
                } else {
                    NetWork.submit(activity, new pbNetwork("false"));
                }
                break;
            case R.id.swzj_b:
                if(PaseJson.getMapMsg(map, "is_export").equals("0")){
                    NetWork.submit(activity, zjNetWork);
                }else{
                    NetWork.submit(activity, qxzjNetWork);
                }
                break;
            case R.id.fsxx_b:

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
                                          data.addData("brelate_id", QzXxzlActivity.this
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
                                              swzjButton.setText("设为专家");
                                          } else {
                                              zjImageView.setVisibility(View.GONE);
                                              swzjButton.setText("取消专家");
                                          }
                                          if (PaseJson.getMapMsg(map, "is_black").equals("0")) {
                                              pbButton.setText("屏蔽");
                                          } else {
                                              pbButton.setText("取消屏蔽");
                                          }
                                          if (PaseJson.getMapMsg(map, "is_bv").equals("0")) {
                                              dvImageView.setVisibility(View.INVISIBLE);
                                          } else {
                                              dvImageView.setVisibility(View.GONE);
                                          }
                                          titleTextView.setText(PaseJson
                                              .getMapMsg(map, "nick_name"));
                                          ddTextView.setText(PaseJson.getMapMsg(map, "address"));
                                          qmTextView.setText(PaseJson.getMapMsg(map, "new_sign"));
                                          xyTextView.setText(PaseJson.getMapMsg(map,
                                              "degree_credit") + "分");
                                          gmTextView.setText(PaseJson.getMapMsg(map, "buy_vol")
                                                             + "件");
                                          mcTextView.setText(PaseJson.getMapMsg(map, "sale_vol")
                                                             + "件");
                                          fsTextView.setText(PaseJson.getMapMsg(map,
                                              "followers_count"));
                                          sjTextView.setText(PaseJson.getMapMsg(map, "mobile"));
                                          yxTextView.setText(PaseJson.getMapMsg(map, "email"));
                                          list.clear();
                                          list.addAll((List<Map<String, Object>>) map.get("photos"));
                                          xcAdapter.notifyDataSetChanged();
                                      }
                                  };
    /**
     * 设为专家
     */
    private INetWork zjNetWork    = new INetWork() {

                                      @Override
                                      public boolean validate() {
                                          return true;
                                      }

                                      @Override
                                      public Data getSubmitData() throws Exception {
                                          Data data = new Data("exportrecord/expertSet.json");
                                          data.addData("relate_id",
                                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                                          data.addData("brelate_id",
                                              PaseJson.getMapMsg(map, "user_id"));
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
    private INetWork qxzjNetWork  = new INetWork() {

                                      @Override
                                      public boolean validate() {
                                          return true;
                                      }

                                      @Override
                                      public Data getSubmitData() throws Exception {
                                          Data data = new Data("exportrecord/cancelExport.json");
                                          data.addData("relate_id",
                                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                                          data.addData("brelate_id",
                                              PaseJson.getMapMsg(map, "user_id"));
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
