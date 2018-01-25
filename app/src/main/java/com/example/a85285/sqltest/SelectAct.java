package com.example.a85285.sqltest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectAct extends AppCompatActivity implements View.OnClickListener {

    //定义控件，用于接受ID
    private Button s_delete, s_conf, s_back;//对应三个按钮
    private ImageView s_img;
    private VideoView s_video;
    private EditText s_tv;
    //定义数据库对象和权限
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);

        //寻找ID
        s_delete = (Button) findViewById(R.id.s_delete);
        s_conf = (Button) findViewById(R.id.s_conf);
        s_back = (Button) findViewById(R.id.s_back);
        s_img = (ImageView) findViewById(R.id.s_img);
        s_video = (VideoView) findViewById(R.id.s_video);
        s_tv = (EditText) findViewById(R.id.s_tv);

        //定义数据库对象并获取权限
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        //对删除和取消按钮设置监听器
        s_conf.setOnClickListener(this);
        s_delete.setOnClickListener(this);
        s_back.setOnClickListener(this);

        //选择性显示控件
        if (getIntent().getStringExtra(NotesDB.PATH).equals("null")) {
            s_img.setVisibility(View.GONE);
        } else {
            s_img.setVisibility(View.VISIBLE);
        }
        if (getIntent().getStringExtra(NotesDB.VIDEO).equals("null")) {
            s_video.setVisibility(View.GONE);
        } else {
            s_video.setVisibility(View.VISIBLE);
        }
        s_tv.setText(getIntent().getStringExtra(NotesDB.CONTENT));//显示文字信息
        Bitmap bitmap = BitmapFactory.decodeFile(getIntent()
                .getStringExtra(NotesDB.PATH));
        s_img.setImageBitmap(bitmap);//显示图片信息
        s_video.setVideoURI(Uri.parse(getIntent().getStringExtra(NotesDB.VIDEO)));//显示视频信息
        s_video.start();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s_delete://点击取消按钮
                deleteDate();
                finish();
                break;
            case R.id.s_conf://点击确定
                confirmDate();
                finish();
                break;
            case R.id.s_back://点击取消
                finish();
                break;

        }
    }

    public void deleteDate() {
        dbWriter.delete(NotesDB.TABLE_NAME,
                "_id=" + getIntent().getIntExtra(NotesDB.ID, 0), null);
    }

    public void confirmDate(){
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, s_tv.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        dbWriter.update(NotesDB.TABLE_NAME,cv,"_id=" + getIntent().getIntExtra(NotesDB.ID, 0),null);
    }

    public String getTime() {   //获取当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }
}