package com.atguigu.app2.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.atguigu.app2.domain.Lyric;

import java.util.ArrayList;


/**
 * Created by My on 2017/5/26.
 */

public class LyricView extends TextView {
    private Paint paintGreen;
    private Paint paintWhite;
    private ArrayList<Lyric> lyrics;
    private int index = 0;
    private int width;
    private int height;
    private float textHeight = 20;
    private int currentPosition;

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setAntiAlias(true);
        paintGreen.setTextSize(16);
        paintGreen.setTextAlign(Paint.Align.CENTER);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setAntiAlias(true);
        paintWhite.setTextSize(16);
        paintWhite.setTextAlign(Paint.Align.CENTER);

        lyrics = new ArrayList<>();
        Lyric lyric = new Lyric();
        for(int i = 0; i < 1000; i++) {
            lyric.setContent("aaaaaaaaaaaa_" + i);
            lyric.setSleepTime(2000);
            lyric.setTimePoint(2000*i);
            lyrics.add(lyric);
            lyric = new Lyric();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lyrics != null && lyrics.size() > 0){
            String currentContent = lyrics.get(index).getContent();
            canvas.drawText(currentContent, width / 2, height / 2, paintGreen);
            float tempY = height / 2;
            for (int i = index - 1; i >= 0; i--){
                String preContent = lyrics.get(i).getContent();
                tempY = tempY - textHeight;
                if (tempY < 0){
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
            canvas.drawText("没有找到歌词...", getWidth() / 2, getHeight() / 2, paintGreen);
        }

    }

    public void setNextShowLyric(int currentPosition) {
        this.currentPosition = currentPosition;
        if (lyrics == null || lyrics.size() == 0){
            return;
        }
        for (int i = 1; i < lyrics.size(); i++) {

            if (currentPosition < lyrics.get(i).getTimePoint()) {
                int tempIndex = i - 1;
                if (currentPosition >= lyrics.get(tempIndex).getTimePoint()) {
                    index = tempIndex;
                }
            }
        }
        invalidate();
    }
}
