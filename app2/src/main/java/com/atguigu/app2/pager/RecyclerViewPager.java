package com.atguigu.app2.pager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.app2.R;
import com.atguigu.app2.adapter.RecyclerFragmentAdapter;
import com.atguigu.app2.domain.RecyclerBean;
import com.atguigu.app2.fragment.BaseFragment;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by My on 2017/5/31.
 */

public class RecyclerViewPager extends BaseFragment {
    private String url = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;
    private RecyclerFragmentAdapter myAdapter;

    //重写视图
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_recyclerview, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams reques = new RequestParams(url);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e("onSuccess==" + result);
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

    /**
     * 解析数据和显示数据
     *
     * @param json
     */
    private void processData(String json) {
        //解析数据
        RecyclerBean recyclerBean = new Gson().fromJson(json, RecyclerBean.class);
        List<RecyclerBean.ListBean> datas = recyclerBean.getList();
        String text = datas.get(0).getText();
        if (datas != null && datas.size() > 0) {
            tvNomedia.setVisibility(View.GONE);
            myAdapter = new RecyclerFragmentAdapter(context, datas);
            recyclerview.setAdapter(myAdapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        } else {
            //没有数据
            tvNomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
