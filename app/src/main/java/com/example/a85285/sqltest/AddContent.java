package com.example.a85285.sqltest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends AppCompatActivity implements View.OnClickListener {

    private String val; //查询数据库并绑定适配器
    private Button savebtn, deletebtn;
    private EditText ettext;
    private ImageView c_img;
    private VideoView c_video;
    private NotesDB notesDB;//添加数据库对象
    private SQLiteDatabase dbWriter;//获取权限
    private File phoneFile, videoFile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        val = getIntent().getStringExtra("flag");//接收标记
        //获取当前界面的id
        savebtn = (Button) findViewById(R.id.save);
        deletebtn = (Button) findViewById(R.id.delete);
        ettext = (EditText) findViewById(R.id.ettext);
        c_img = (ImageView) findViewById(R.id.c_img);
        c_video = (VideoView) findViewById(R.id.c_video);
        //给保存和取消按钮添加监听事件
        savebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);
        //新建数据库对象，并获取权限
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        initView();//初始化
    }

    public void initView() {
        if (val.equals("1")) {  //如果传过来的是文字
            c_img.setVisibility(View.GONE);//隐藏图片和视频
            c_video.setVisibility(View.GONE);
        }
        if (val.equals("2")) {//如果传过来的是图片
            c_img.setVisibility(View.VISIBLE);//显示图片，隐藏视频
            c_video.setVisibility(View.GONE);
            Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用照相机
            phoneFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".jpg");//寻找SD卡路径，并按照当前时间对图片命名
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));//存储
            startActivityForResult(iimg, 1);

        }
        if (val.equals("3")) { //如果传过来的是视频
            c_img.setVisibility(View.GONE);//隐藏图片，显示视频
            c_video.setVisibility(View.VISIBLE);
            Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".mp4");
            video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(video, 2);
        }
    }

    public void onClick(View v) {//复写监听事件
        switch (v.getId()) {
            case R.id.save:
                addDB();
                finish();
                break;
            case R.id.delete:
                finish();
                break;
        }

    }

    public void addDB() {   //给数据库中的表添加元组
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, ettext.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.PATH, phoneFile + "");
        cv.put(NotesDB.VIDEO, videoFile + "");
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    public String getTime() {   //获取当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { //代表传递的是图片
            Bitmap bitmap = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());//获取路径以展示图片
            c_img.setImageBitmap(bitmap);
        }
        if (requestCode == 2) { //代表传递的是视频
            c_video.setVideoURI(Uri.fromFile(videoFile));
            c_video.start();
        }
    }

}