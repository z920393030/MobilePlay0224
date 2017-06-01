package com.atguigu.app2.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.atguigu.app2.R;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by My on 2017/5/28.
 */

public class ShowImageAndGifActivity extends AppCompatActivity {

    private String url;
    private ImageOptions imageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_and_gif);

        url = getIntent().getStringExtra("url");
        LogUtil.e(url);

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

        imageOptions = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(5))
                .setIgnoreGif(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.video_default)
                .setFailureDrawableId(R.drawable.video_default)
                .build();
        x.image().bind(photoView, url, imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                attacher.update();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }
}
