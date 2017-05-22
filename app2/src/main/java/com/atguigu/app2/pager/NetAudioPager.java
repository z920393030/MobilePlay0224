package com.atguigu.app2.pager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.app2.fragment.BaseFragment;

/**
 * Created by My on 2017/5/22.
 */

public class NetAudioPager extends BaseFragment {
    private TextView textView;


    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("网络音乐的内容");
    }
}
