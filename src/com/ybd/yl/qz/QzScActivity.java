package com.ybd.yl.qz;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ybd.common.PicUtil;
import com.ybd.common.PropertiesUtil;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.common.SelectPhotoMultipleActivity;

/**
 * 圈子-上传
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class QzScActivity extends BaseActivity implements OnClickListener {
    private Button   ddButton;      //当代
    private Button   jxdButton;     //近现代
    private Button   gdButton;      //古代
    private TextView pzTextView;    //拍照
    private TextView tpTextView;    //图片
    private TextView wzTextView;    //文字
    private String   selectNd = "2"; //选择年代的值(1当代;2近现代;3古代;)

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.qz_sc);
        initPublicView("上传", R.drawable.login_fh, 0, null, null);
        init();
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
        pzTextView = (TextView) findViewById(R.id.pz_tv);
        pzTextView.setOnClickListener(this);
        tpTextView = (TextView) findViewById(R.id.tp_tv);
        tpTextView.setOnClickListener(this);
        wzTextView = (TextView) findViewById(R.id.wz_tv);
        wzTextView.setOnClickListener(this);
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
            case R.id.pz_tv:
                // 拍照
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 0);
                break;
            case R.id.tp_tv:
                PropertiesUtil.write(activity, "ND", selectNd);//保存年代信息
                intent.setClass(activity, SelectPhotoMultipleActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.wz_tv:
                PropertiesUtil.write(activity, "ND", selectNd);//保存年代信息
                intent.setClass(activity, QzScSctp2Activity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == 0) {//拍照
                String path = PicUtil.savePz(arg2);
                //                Uri imageUri = Uri.parse("file:///" + path);
                //                List<Map<String, Object>> list = null;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("path", "file:///" + path);
                map.put("ispz", "1");
                //                if(this.getIntent().hasExtra("path")){
                //                    list=(List<Map<String, Object>>) this.getIntent().getExtras().getSerializable("path");
                //                }else{
                //                    list=new ArrayList<Map<String,Object>>();
                //                }
                //                list.add(map);
                QzScSctpActivity.list.add(0, map);
                Intent intent = new Intent();
                intent.setClass(activity, QzScSctpActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
