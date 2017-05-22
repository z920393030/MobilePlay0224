package com.atguigu.app2.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.app2.R;
import com.atguigu.app2.domain.MediaItem;
import com.atguigu.app2.utils.Utils;

import java.util.ArrayList;

/**
 * Created by My on 2017/5/22.
 */

public class LocalVideoAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<MediaItem> datas;
    private Utils utils;

    public LocalVideoAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.datas = mediaItems;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public MediaItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_local_video,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MediaItem mediaItem = datas.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
        viewHolder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));


        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
    }
}
