package com.ybd.yl.xx.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ybd.common.C;
import com.ybd.common.tools.PaseJson;

/**
 * 单人聊天的用户表的信息
 * 
 * @author cyf
 * @version $Id: TalkUserDao.java, v 0.1 2016-1-18 下午3:37:48 cyf Exp $
 */
public class XxTxlLtDao {

    private DBOpenHelper   helper;
    private SQLiteDatabase db;

    /**
     * 实例化类的时候，创建数据库对象
     * @param context
     */
    public XxTxlLtDao(Context context) {
        helper = DBOpenHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 消息插入到数据库中
     * 判断消息用户表如果存在更新未读数量，不存在插入
     * 同时插入聊天记录表
     * @param Accessory 
     */
    public void add(Map<String, Object> map) {
        try {
            if (findUserIdExist(PaseJson.getMapMsg(map, "sender_id"))) {
                updateTalkUser(map);
            } else {
                db.execSQL(
                    "insert into tb_talk_user (userid,username,usericon,unreadnum,lasttalktime,lasttalkmsg,voipaccount,msgtype) values (?,?,?,?,?,?,?,?)",
                    new Object[] { PaseJson.getMapMsg(map, "sender_id"),
                            PaseJson.getMapMsg(map, "sender_name"),
                            PaseJson.getMapMsg(map, "sender_icon_url"), 1,
                            PaseJson.getMapMsg(map, "send_time"),
                            PaseJson.getMapMsg(map, "send_content"),
                            PaseJson.getMapMsg(map, "voip_account"),
                            PaseJson.getMapMsg(map, "type") });
//                L.v(PaseJson.getMapMsg(map, "voip_account") + ":::::dao");
            }
            addTalk(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将聊天内容插入到用户聊天表中
     * 
     * @param map
     */
    public void addTalk(Map<String, Object> map) {
        try {
            db.execSQL(
                "insert into tb_talk_msg (userid,usericon,talkmsg,talktime,sendertype) values (?,?,?,?,?)",
                new Object[] { PaseJson.getMapMsg(map, "sender_id"),
                        PaseJson.getMapMsg(map, "sender_icon_url"),
                        PaseJson.getMapMsg(map, "send_content"),
                        PaseJson.getMapMsg(map, "send_time"),
                        PaseJson.getMapMsg(map, "sender_type") });
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询某个用户是否已经存在
     * 
     * @param userid
     */
    public boolean findUserIdExist(String userid) {
        try {
            Cursor cursor = null;
            cursor = db.rawQuery("select userid from tb_talk_user where userid=?",
                new String[] { userid });
            while (cursor.moveToNext()) {
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改未读信息的数量
     * 单单修改数量
     * 根据用户的id
     */
    public boolean updateTalkUser(int num, String userid) {
        try {
            db.execSQL("update tb_talk_user set unreadnum=?  where userid=?", new Object[] { num,
                    userid });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改未读信息的数量（当前的未读数量加1）
     * 同时修改最后一次聊天的内容和最后一次聊天的时间
     * 根据用户的id
     */
    public boolean updateTalkUser(Map<String, Object> map) {
        try {
            db.execSQL(
                "update tb_talk_user set unreadnum=unreadnum+1,lasttalktime=?,lasttalkmsg=? where userid=?",
                new Object[] { PaseJson.getMapMsg(map, "send_time"),
                        PaseJson.getMapMsg(map, "send_content"),
                        PaseJson.getMapMsg(map, "sender_id") });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改未读信息的数量（指定未读信息的数量）
     * 同时修改最后一次聊天的内容和最后一次聊天的时间
     * 根据用户的id
     */
    public boolean updateTalkUser(int num, Map<String, Object> map) {
        try {
            db.execSQL(
                "update tb_talk_user set unreadnum=?,lasttalktime=?,lasttalkmsg=? where userid=?",
                new Object[] { num, PaseJson.getMapMsg(map, "send_time"),
                        PaseJson.getMapMsg(map, "send_content"),
                        PaseJson.getMapMsg(map, "sender_id") });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有列表的信息
     */
    public List<Map<String, Object>> findAllLt() {
        Cursor cursor = null;
        List<Map<String, Object>> list = null;
        try {
            list = new ArrayList<Map<String, Object>>();
            cursor = db
                .rawQuery(
                    "select userid,username,usericon,unreadnum,lasttalktime,lasttalkmsg,voipaccount,msgtype from tb_talk_user ",
                    new String[] {});
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sender_id", cursor.getString(cursor.getColumnIndex("userid")));
                map.put("sender_name", cursor.getString(cursor.getColumnIndex("username")));
                map.put("sender_icon_url", C.IP+cursor.getString(cursor.getColumnIndex("usericon")));
                map.put("unread_num", cursor.getInt(cursor.getColumnIndex("unreadnum")));
                map.put("send_content", cursor.getString(cursor.getColumnIndex("lasttalkmsg")));
                map.put("send_time", cursor.getString(cursor.getColumnIndex("lasttalktime")));
                map.put("voip_account", cursor.getString(cursor.getColumnIndex("voipaccount")));
                map.put("type", cursor.getString(cursor.getColumnIndex("msgtype")));
                list.add(map);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return list;
    }

    /**
     * 查询所有的单个用户的信息
     */
    public List<Map<String, Object>> findUserAllLt(String sender_id) {
        Cursor cursor = null;
        List<Map<String, Object>> list = null;
        try {
            list = new ArrayList<Map<String, Object>>();
            cursor = db
                .rawQuery(
                    "select userid,usericon,talkmsg,sendertype,talktime from tb_talk_msg where userid=? ",
                    new String[] { sender_id });
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sender_id", cursor.getString(cursor.getColumnIndex("userid")));
                map.put("sender_icon_url", cursor.getString(cursor.getColumnIndex("usericon")));
                map.put("send_content", cursor.getString(cursor.getColumnIndex("talkmsg")));
                map.put("send_time", cursor.getString(cursor.getColumnIndex("talktime")));
                map.put("sender_type", cursor.getString(cursor.getColumnIndex("sendertype")));
                list.add(map);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return list;
    }

    /**
     * 根据信息的ID删除对应的信息
     * 删除对应的ID的聊天的信息
     */
    public boolean delete(String userid) {
        try {
            db.execSQL("delete from tb_talk_user where userid=?", new String[] { userid });
            db.execSQL("delete from tb_talk_msg where userid=?", new String[] { userid });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
