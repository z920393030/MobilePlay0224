package com.atguigu.app2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.atguigu.app2.fragment.BaseFragment;
import com.atguigu.app2.pager.LocalAudioPager;
import com.atguigu.app2.pager.LocalVideoPager;
import com.atguigu.app2.pager.NetAudioPager;
import com.atguigu.app2.pager.NetVideoPager;
import com.atguigu.app2.pager.RecyclerViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    private BaseFragment tempFragment;
    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isGrantExternalRW(this);
        rg_main = (RadioGroup)findViewById(R.id.rg_main);
        initFragment();
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_main.check(R.id.rb_local_video);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetAudioPager());
        fragments.add(new NetVideoPager());
        fragments.add(new RecyclerViewPager());
    }


    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_local_video:
                    position = 0;
                    break;
                case R.id.rb_local_audio:
                    position = 1;
                    break;

                case R.id.rb_net_audio:
                    position = 2;
                    break;

                case R.id.rb_net_video:
                    position  = 3;
                    break;
                case R.id.recyclerview:
                    position  = 4;
                    break;
            }
            BaseFragment currentFragment = fragments.get(position);
            addFragment(currentFragment);
        }
    }

    private void addFragment(BaseFragment currentFragment) {
        if(tempFragment != currentFragment){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if(!currentFragment.isAdded()){
                if(tempFragment != null){
                    ft.hide(tempFragment);
                }
                ft.add(R.id.fl_content,currentFragment);
            }else{
                if(tempFragment != null){
                    ft.hide(tempFragment);
                }
                ft.show(currentFragment);

            }

            ft.commit();
            tempFragment = currentFragment;

        }
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            if(position!= 0){
                rg_main.check(R.id.rb_local_video);
                return true;
            }else if(!isExit){
                Toast.makeText(MainActivity.this, "再按一次推出软件", Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
