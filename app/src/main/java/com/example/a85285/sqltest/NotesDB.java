package com.example.a85285.sqltest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes";//表名
    public static final String CONTENT = "content";//文本内容
    public static final String PATH = "path";//图片路径
    public static final String VIDEO = "video";//视频
    public static final String ID = "_id";//id自增且唯一表示元组
    public static final String TIME = "time";//记录存储的时间

    public NotesDB(Context context) {
        super(context, "notes", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID  //创建表，表中含有ID,文本内容，图片路径，视频路径以及存储的当前时间
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT
                + " TEXT NOT NULL," + PATH + " TEXT NOT NULL," + VIDEO
                + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
