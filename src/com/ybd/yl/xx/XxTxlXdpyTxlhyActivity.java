/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ybd.common.L;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 消息-通讯录-新的朋友-通讯录好友
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxTxlXdpyTxlhyActivity extends BaseActivity implements OnClickListener {
    private ListView          txlListView;
    private BaseAdapter       txlAdapter;
    List<Map<String, Object>> list            = new ArrayList<Map<String, Object>>();
    // 存放国标一级汉字不同读音的起始区位码
    static final int[]        secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594,
            2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925,
            5249, 5600                       };
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[]       firstLetter     = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z' };
    static final int          GB_SP_DIFF      = 160;

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl_xdpy_txlhy);
        initPublicView("您的通讯录好友", R.drawable.login_fh, "邀请", null, this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        list.clear();
        list.addAll(getContacts(activity));
        txlListView = (ListView) findViewById(R.id.txl_lv);
        txlAdapter = new XxTxlXdpyTxlhyAdapter(list, this);
        txlListView.setAdapter(txlAdapter);
        txlAdapter.notifyDataSetChanged();

        txlListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Map<String, Object> map = ((Map<String, Object>) list.get(arg2));
                if (PaseJson.getMapMsg(map, "isSelect").equals("")
                    || PaseJson.getMapMsg(map, "isSelect").equals("0")) {
                    map.put("isSelect", "1");
                } else {
                    map.put("isSelect", "0");
                }
                txlAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //        Intent intent = new Intent();
        switch (v.getId()) {//邀请按钮的点击事件
            case R.id.right_rl:
                String phones = "";
                for (Map<String, Object> map : list) {
                    if (PaseJson.getMapMsg(map, "isSelect").equals("1")) {
                        phones += PaseJson.getMapMsg(map, "phone") + ";";
                    }
                }
                sendSMS("艺乃心声，论是谈非。艺论，艺品天地，降薪臻品，邀您共同赏鉴。www.yilun.com", phones);
                break;
            default:
                break;
        }
    }

    private void sendSMS(String smsBody, String phones){
        Uri smsToUri = Uri.parse("smsto:" + phones.substring(0, phones.length()-1));
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        startActivity(intent);
    }

    /**
     * 获取所有联系人内容
     * @param context
     * @param address
     * @return
     */
    public List<Map<String, Object>> getContacts(Context context) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuilder sb = new StringBuilder();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
            ContactsContract.Contacts.DISPLAY_NAME + " collate NOCASE ASC");
        if (cursor.moveToFirst()) {
            String lastCode = "";//
            do {
                Map<String, Object> map = new HashMap<String, Object>();
                String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //第一条不用换行
                if (sb.length() == 0) {
                    sb.append(name);
                } else {
                    sb.append("\n" + name);
                }
                map.put("name", name);
                String s = getSpells(name).substring(0, 1).toUpperCase();
                map.put("first_letter", s);
                if (lastCode.equals(s)) {
                    map.put("isSame", "1");
                } else {
                    map.put("isSame", "0");
                }
                lastCode = s;
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null,
                    null);
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    // 添加Phone的信息
                    sb.append("\t").append(phoneNumber);
                    map.put("phone", phoneNumber);
                    break;
                }
                L.v(sb.toString());
                list.add(map);
                phones.close();

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characters.length(); i++) {

            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
                // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell));
            }
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

}
