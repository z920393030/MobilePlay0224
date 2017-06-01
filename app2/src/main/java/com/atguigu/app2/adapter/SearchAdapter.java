package com.atguigu.app2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.app2.R;
import com.atguigu.app2.domain.SearchBean;
import com.atguigu.app2.utils.Utils;
import com.squareup.picasso.Picasso;

import org.xutils.image.ImageOptions;

import java.util.List;

/**
 * Created by My on 2017/5/28.
 */

public class SearchAdapter extends BaseAdapter {
    private final Context context;
    List<SearchBean.ItemsBean> datas;
    private Utils utils;
    private ImageOptions imageOptions;

    public SearchAdapter(Context context, List<SearchBean.ItemsBean> datas) {
        this.context = context;
        this.datas = datas;
        utils = new Utils();
        imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(R.drawable.video_default)
                .setLoadingDrawableId(R.drawable.video_default)
                .build();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public SearchBean.ItemsBean getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_net_video,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SearchBean.ItemsBean trailersBean = datas.get(position);

        viewHolder.tv_name.setText(trailersBean.getItemTitle());
        viewHolder.tv_size.setText(trailersBean.getDatecheck());
        viewHolder.tv_duration.setText(trailersBean.getKeywords());
        Picasso.with(context)
                .load(trailersBean.getItemImage().getImgUrl1())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.iv_icon);


        return convertView;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
    }
}
