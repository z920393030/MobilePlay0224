package com.atguigu.mobileplay2.pager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.mobileplay2.R;
import com.atguigu.mobileplay2.activity.SystemVideoPlayerActivity;
import com.atguigu.mobileplay2.adapter.LocalVideoAdapter;
import com.atguigu.mobileplay2.domain.MediaItem;
import com.atguigu.mobileplay2.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by My on 2017/5/19.
 */

public class LocalVideoPager extends BaseFragment {

    private ListView lv;
    private TextView tv_nodata;
    private ArrayList<MediaItem> mediaItems;
    private LocalVideoAdapter adapter;
    @Override
    public View initView() {
        View  view = View.inflate(context, R.layout.fragment_local_video_pager,null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaItem item = adapter.getItem(position);
                Toast.makeText(context, ""+item.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,SystemVideoPlayerActivity.class);
                intent.setDataAndType(Uri.parse(item.getData()),"video/*");

                startActivity(intent);


            }
        });
        return view;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems!=null && mediaItems.size() >0) {
                tv_nodata.setVisibility(View.GONE);

                adapter = new LocalVideoAdapter(context,mediaItems);
                lv.setAdapter(adapter);

            }else {
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void initData() {
        super.initData();
        getData();
    }

    private void getData() {
        new Thread(){
            public void run(){
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频在sdcard上的名称
                        MediaStore.Video.Media.DURATION,//视频时长
                        MediaStore.Video.Media.SIZE,//视频文件的大小
                        MediaStore.Video.Media.DATA//视频播放地址
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);

                if(cursor != null) {
                    while (cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                        mediaItems.add(new MediaItem(name,duration,size,data));
                        handler.sendEmptyMessage(0);
                    }
                    cursor.close();
                }
            }
        }.start();
    }
}

