package com.atguigu.app2.pager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.app2.R;
import com.atguigu.app2.activity.AudioPlayerActivity;
import com.atguigu.app2.adapter.LocalVideoAdapter;
import com.atguigu.app2.domain.MediaItem;
import com.atguigu.app2.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by My on 2017/5/22.
 */

public class LocalAudioPager extends BaseFragment {
    private ListView lv;
    private TextView tv_nodata;
    private ArrayList<MediaItem> mediaItems;
    private LocalVideoAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_local_video_pager, null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(context, AudioPlayerActivity.class);

                intent.putExtra("position",position);
                startActivity(intent);


            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getData();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mediaItems != null && mediaItems.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
                adapter = new LocalVideoAdapter(context, mediaItems,false);
                lv.setAdapter(adapter);
            } else {
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    private void getData() {
        new Thread() {
            public void run() {
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                        mediaItems.add(new MediaItem(name, duration, size, data,artist));


                    }

                    cursor.close();
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
