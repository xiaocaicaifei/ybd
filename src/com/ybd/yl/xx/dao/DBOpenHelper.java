package com.ybd.yl.xx.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author  coolszy
 * @blog    http://blog.csdn.net/coolszy
 * 创建本地Sqlite数据库的类
 */
public class DBOpenHelper extends SQLiteOpenHelper
{
	private static final int VERSION = 1;
	private static final String DBNAME = "yllun.db";
	private static DBOpenHelper mInstance = null;
	public DBOpenHelper(Context context)
	{
		super(context, DBNAME, null, VERSION);
	}
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
	 public  static DBOpenHelper getInstance(Context context) {  
	     if (mInstance == null) {  
	           mInstance = new  DBOpenHelper(context);  
	     }  
	     return mInstance;  
	 };  

	@Override
	public void onCreate(SQLiteDatabase db)
	{
	    //单人聊天的用户表(用户id,用户的名字，用户头像，该用户未读消息数量，最后一次聊天的时间，最后一次聊天的内容,聊天用户的聊天的ID,消息的类型 1，对话类型；2艺论视频；3艺论新闻；4艺论群组)
		db.execSQL("create table tb_talk_user (id integer primary key,userid text,username text,usericon text,unreadnum integer,lasttalktime text,lasttalkmsg text,voipaccount text,msgtype text)");
		//单人聊天的内容表（用户的id，用户的头像,聊天的内容，聊天的类型（0：自己发的；1别人发的））
		db.execSQL("create table tb_talk_msg (id integer primary key,userid text,usericon text,talkmsg text,talktime,sendertype text)");
		 
//		//群组聊天的用户表(用户id,用户的名字，用户头像，该用户未读消息数量，最后一次聊天的时间，最后一次聊天的内容,聊天用户的聊天的ID,消息的类型 1，对话类型；2艺论视频；3艺论新闻；4艺论群组)
//        db.execSQL("create table tb_talk_user (id integer primary key,groupid text,username text,usericon text,unreadnum integer,lasttalktime text,lasttalkmsg text,voipaccount text,msgtype text)");
//        //群组聊天的内容表（用户的id，用户的头像,聊天的内容，聊天的类型（0：自己发的；1别人发的））
//        db.execSQL("create table tb_talk_msg (id integer primary key,userid text,usericon text,talkmsg text,talktime,sendertype text)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}

}
