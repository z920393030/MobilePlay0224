package com.atguigu.app2.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.atguigu.app2.domain.Lyric;
import com.atguigu.app2.utils.DensityUtil;

import java.util.ArrayList;


/**
 * Created by My on 2017/5/26.
 */

public class LyricView extends TextView {
    private final Context context;
    private Paint paintGreen;
    private Paint paintWhite;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;
    private int index = 0;
    private float textHeight = 20;
    private int currentPosition;
    private long timePoint;
    private long sleepTime;

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        textHeight = DensityUtil.dip2px(context,20);
        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setAntiAlias(true);
        paintGreen.setTextSize(DensityUtil.dip2px(context,16));
        paintGreen.setTextAlign(Paint.Align.CENTER);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setAntiAlias(true);
        paintWhite.setTextSize(DensityUtil.dip2px(context,16));
        paintWhite.setTextAlign(Paint.Align.CENTER);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lyrics != null && lyrics.size() > 0) {

            if(index != lyrics.size()-1){
                float push = 0;
                if (sleepTime == 0) {
                    push = 0;
                } else {
                    push = ((currentPosition - timePoint) / sleepTime) * textHeight;
                }
                canvas.translate(0, -push);
            }

            String currentContent = lyrics.get(index).getContent();
            canvas.drawText(currentContent, width / 2, height / 2, paintGreen);
            float tempY = height / 2;

            for (int i = index - 1; i >= 0; i--) {

                String preContent = lyrics.get(i).getContent();

                tempY = tempY - textHeight;
                if (tempY < 0) {
                    break;
                }

                canvas.drawText(preContent, width / 2, tempY, paintWhite);

            }

            tempY = height / 2;

            for (int i = index + 1; i < lyrics.size(); i++) {
                String nextContent = lyrics.get(i).getContent();

                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }

                canvas.drawText(nextContent, width / 2, tempY, paintWhite);
            }

        } else {
            canvas.drawText("没有找到歌词...", width / 2, height / 2, paintGreen);
        }
    }

    public void setNextShowLyric(int currentPosition) {
        this.currentPosition = currentPosition;
        if (lyrics == null || lyrics.size() == 0)
            return;

        for (int i = 1; i < lyrics.size(); i++) {

            if (currentPosition < lyrics.get(i).getTimePoint()) {
                int tempIndex = i - 1;
                if (currentPosition >= lyrics.get(tempIndex).getTimePoint()) {
                    index = tempIndex;
                    timePoint = lyrics.get(index).getTimePoint();
                    sleepTime = lyrics.get(index).getSleepTime();

                }
            }else {
                index  = i;
            }
        }
        invalidate();
    }

    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;

    }
}
