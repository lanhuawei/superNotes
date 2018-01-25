package com.example.a85285.sqltest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button textbtn, imgbtn, videobtn;//分别表示文本，图片，视频的按钮
    private ListView lv;//列表
    private Intent i;//用于控制页面跳转去向
    private MyAdapter adapter;//定义适配器
    private NotesDB notesDB;//定义数据库对象
    private SQLiteDatabase dbReader;//定义权限
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化
    }

    public void initView() {
        //找id操作
        lv = (ListView) findViewById(R.id.list);
        textbtn = (Button) findViewById(R.id.text);
        imgbtn = (Button) findViewById(R.id.img);
        videobtn = (Button) findViewById(R.id.video);
        //添加监听事件
        textbtn.setOnClickListener(this);
        imgbtn.setOnClickListener(this);
        videobtn.setOnClickListener(this);
        //获取数据库的读权限
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
        //给列表添加监听事件，采用内部类实现，实现列表到具体页面的跳转
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cursor.moveToPosition(position);//给出游标位置
                Intent i = new Intent(MainActivity.this, SelectAct.class);//创建intent对象
                i.putExtra(NotesDB.ID, cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH, cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                i.putExtra(NotesDB.VIDEO, cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(i);
            }
        });
    }

    public void onClick(View v) {//响应函数，根据按钮按下不同跳转到不同页面
        i = new Intent(this, AddContent.class);
        switch (v.getId()) {        //根据不同按钮按下进行跳转和传递
            case R.id.text:
                i.putExtra("flag", "1");
                startActivity(i);
                break;
            case R.id.img:
                i.putExtra("flag", "2");
                startActivity(i);
                break;
            case R.id.video:
                i.putExtra("flag", "3");
                startActivity(i);
                break;
        }

    }

    public void selectDB() {    //查询数据库并绑定适配器
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    protected void onResume() {
        super.onResume();
        selectDB();
    }

}