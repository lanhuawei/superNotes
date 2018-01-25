package com.example.a85285.sqltest;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

    private Context context;//文本内容
    private Cursor cursor;//游标，用于查询数据库
    private LinearLayout layout;

    public MyAdapter(Context context, Cursor cursor) {//初始化
        this.context = context;
        this.cursor = cursor;

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//获取布局
        //定义布局对象
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.cell, null);
        //获取布局文件内容
        TextView contenttv = (TextView) layout.findViewById(R.id.list_content);
        TextView timetv = (TextView) layout.findViewById(R.id.list_time);
        ImageView imgiv = (ImageView) layout.findViewById(R.id.list_img);
        ImageView videoiv = (ImageView) layout.findViewById(R.id.list_video);//用视频的第一帧图片作为显示
        //获取游标位置
        cursor.moveToPosition(position);
        //传递数据库的内容到布局
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String url = cursor.getString(cursor.getColumnIndex("path"));
        String urlvideo = cursor.getString(cursor.getColumnIndex("video"));
        //加载相应内容
        contenttv.setText(content);
        timetv.setText(time);
        imgiv.setImageBitmap(getImageThumbnail(url, 200, 200));
        videoiv.setImageBitmap(getVideoThumnail(urlvideo, 200, 200,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        return layout;
    }

    public Bitmap getImageThumbnail(String uri, int width, int height) {//获取图片的缩略图
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);//获取文件
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;//获得缩略后的宽度和高度
        int beHeight = options.outHeight / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);//重新获取，得到缩略后的图片
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private Bitmap getVideoThumnail(String uri, int width, int height, int kind) {//添加视频的缩略图
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}