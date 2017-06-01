package com.atguigu.app2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.app2.R;
import com.atguigu.app2.domain.NetAudioBean;
import com.atguigu.app2.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by My on 2017/5/28.
 */

public class NetAudioFragmentAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<NetAudioBean.ListBean> datas;
    private static final int TYPE_VIDEO = 0;

    private static final int TYPE_IMAGE = 1;

    private static final int TYPE_TEXT = 2;

    private static final int TYPE_GIF = 3;


    private static final int TYPE_AD = 4;

    public NetAudioFragmentAdapter(Context context, List<NetAudioBean.ListBean> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        NetAudioBean.ListBean listBean = datas.get(position);
        String type = listBean.getType();
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;
        }
        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public NetAudioBean.ListBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = initView(convertView, getItemViewType(position), datas.get(position));

        return convertView;
    }


    private View initView(View convertView, int itemViewType, NetAudioBean.ListBean mediaItem) {
        switch (itemViewType) {

            case TYPE_VIDEO:

                VideoHoder videoHoder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_video_item, null);
                    videoHoder = new VideoHoder(convertView);
                    convertView.setTag(videoHoder);
                } else {
                    videoHoder = (VideoHoder) convertView.getTag();
                }

                videoHoder.setData(mediaItem);

                break;
            case TYPE_IMAGE:
                ImageHolder imageHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_image_item, null);
                    imageHolder = new ImageHolder(convertView);
                    convertView.setTag(imageHolder);
                } else {
                    imageHolder = (ImageHolder) convertView.getTag();
                }
                imageHolder.setData(mediaItem);
                break;
            case TYPE_TEXT:

                TextHolder textHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_text_item, null);
                    textHolder = new TextHolder(convertView);

                    convertView.setTag(textHolder);
                } else {
                    textHolder = (TextHolder) convertView.getTag();
                }

                textHolder.setData(mediaItem);

                break;
            case TYPE_GIF:

                GifHolder gifHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_gif_item, null);
                    gifHolder = new GifHolder(convertView);

                    convertView.setTag(gifHolder);
                } else {
                    gifHolder = (GifHolder) convertView.getTag();
                }

                gifHolder.setData(mediaItem);

                break;
            case TYPE_AD:

                ADHolder adHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.all_ad_item, null);
                    adHolder = new ADHolder(convertView);
                    convertView.setTag(adHolder);
                } else {
                    adHolder = (ADHolder) convertView.getTag();
                }

                break;
        }
        return convertView;
    }


    class BaseViewHolder {
        ImageView ivHeadpic;
        TextView tvName;
        TextView tvTimeRefresh;
        ImageView ivRightMore;
        ImageView ivVideoKind;
        TextView tvVideoKindText;
        TextView tvShenheDingNumber;
        TextView tvShenheCaiNumber;
        TextView tvPostsNumber;
        LinearLayout llDownload;

        public BaseViewHolder(View convertView) {
            ivHeadpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvTimeRefresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
            ivRightMore = (ImageView) convertView.findViewById(R.id.iv_right_more);
            ivVideoKind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
            tvVideoKindText = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
            tvShenheDingNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
            tvShenheCaiNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
            tvPostsNumber = (TextView) convertView.findViewById(R.id.tv_posts_number);
            llDownload = (LinearLayout) convertView.findViewById(R.id.ll_download);
        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
                x.image().bind(ivHeadpic, mediaItem.getU().getHeader().get(0));
            }
            if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
                tvName.setText(mediaItem.getU().getName() + "");
            }

            tvTimeRefresh.setText(mediaItem.getPasstime());

            List<NetAudioBean.ListBean.TagsBean> tagsEntities = mediaItem.getTags();
            if (tagsEntities != null && tagsEntities.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < tagsEntities.size(); i++) {
                    buffer.append(tagsEntities.get(i).getName() + " ");
                }
                tvVideoKindText.setText(buffer.toString());
            }


            tvShenheDingNumber.setText(mediaItem.getUp());
            tvShenheCaiNumber.setText(mediaItem.getDown() + "");
            tvPostsNumber.setText(mediaItem.getForward() + "");

        }


    }

    class VideoHoder extends BaseViewHolder {
        Utils utils;
        TextView tvContext;
        JCVideoPlayerStandard jcvVideoplayer;
        TextView tvPlayNums;
        TextView tvVideoDuration;
        ImageView ivCommant;
        TextView tvCommantContext;

        VideoHoder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            utils = new Utils();
            tvPlayNums = (TextView) convertView.findViewById(R.id.tv_play_nums);
            tvVideoDuration = (TextView) convertView.findViewById(R.id.tv_video_duration);
            ivCommant = (ImageView) convertView.findViewById(R.id.iv_commant);
            tvCommantContext = (TextView) convertView.findViewById(R.id.tv_commant_context);
            jcvVideoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);
        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);

            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());

            boolean setUp = jcvVideoplayer.setUp(
                    mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");
            if (setUp) {
                Glide.with(mContext).load(mediaItem.getVideo().getThumbnail().get(0)).into(jcvVideoplayer.thumbImageView);
            }
            tvPlayNums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
            tvVideoDuration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

        }

    }

    class ImageHolder extends BaseViewHolder {
        TextView tvContext;
        ImageView ivImageIcon;

        ImageHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);

        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());

            ivImageIcon.setImageResource(R.drawable.bg_item);
            if (mediaItem.getImage() != null && mediaItem.getImage() != null && mediaItem.getImage().getSmall() != null) {
                Glide.with(mContext).load(mediaItem.getImage().getDownload_url().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImageIcon);
            }


        }
    }

    class TextHolder  extends BaseViewHolder{
        TextView tvContext;

        TextHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);


        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
        }
    }

    class GifHolder extends BaseViewHolder{
        TextView tvContext;
        ImageView ivImageGif;
        private ImageOptions imageOptions;

        GifHolder(View convertView) {
            super(convertView);
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageGif = (ImageView) convertView.findViewById(R.id.iv_image_gif);

            imageOptions = new ImageOptions.Builder()
                    .setSize(ViewGroup.LayoutParams.WRAP_CONTENT, -2)
                    .setRadius(DensityUtil.dip2px(5))
                    .setIgnoreGif(false)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.video_default)
                    .setFailureDrawableId(R.drawable.video_default)
                    .build();

        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());

            if (mediaItem.getGif() != null && mediaItem.getGif() != null && mediaItem.getGif().getImages() != null) {
                x.image().bind(ivImageGif, mediaItem.getGif().getImages().get(0), imageOptions);
            }

        }
    }

    class ADHolder{
        TextView tvContext;
        ImageView ivImageIcon;
        Button btnInstall;

        ADHolder(View convertView) {
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            btnInstall = (Button) convertView.findViewById(R.id.btn_install);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
        }
    }


}
