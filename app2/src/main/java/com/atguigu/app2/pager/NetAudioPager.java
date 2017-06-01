package com.atguigu.app2.pager;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.app2.R;
import com.atguigu.app2.adapter.NetAudioFragmentAdapter;
import com.atguigu.app2.domain.NetAudioBean;
import com.atguigu.app2.fragment.BaseFragment;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.iflytek.cloud.VerifierResult.TAG;

/**
 * Created by My on 2017/5/22.
 */

public class NetAudioPager extends BaseFragment {
    private String url = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";

    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;
    private NetAudioFragmentAdapter myAdapter;

    @Override
    public View initView() {
        Log.e(TAG, "网络音频UI被初始化了");
        View view = View.inflate(context, R.layout.fragment_net_audio, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "网络视频数据初始化了...");


        getDataFromNet();
    }

    private void processData(String json) {
        NetAudioBean netAudioBean = new Gson().fromJson(json, NetAudioBean.class);
        List<NetAudioBean.ListBean> datas = netAudioBean.getList();
        if(datas != null && datas.size() >0){
            tvNomedia.setVisibility(View.GONE);
            myAdapter = new NetAudioFragmentAdapter(context,datas);
            listview.setAdapter(myAdapter);
        }else{
            tvNomedia.setVisibility(View.VISIBLE);

        }
        progressbar.setVisibility(View.GONE);
    }

    private void getDataFromNet() {
        RequestParams reques = new RequestParams(url);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
