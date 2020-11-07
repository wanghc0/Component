package com.wct.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wct.component.R;
import com.wct.componentbase.ServiceManager;
import com.wct.share_api.IShareService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 跳登录界面
     *
     * @param view
     */
    public void login(View view) {
        ARouter.getInstance().build("/loginimpl/login").navigation();
    }

    /**
     * 跳分享界面
     *
     * @param view
     */
    public void share(View view) {
        ServiceManager.getService(IShareService.class).share("这篇组件化文章写的真好");
    }

    public void jumpShareActivity(View view){
        ARouter.getInstance().build("/share/share").withString("share_content", "从MainActivity跳转而来").navigation();
    }

    /**
     * 跳 FragmentActivity
     *
     * @param view
     */
    public void fragment(View view) {
        startActivity(new Intent(this, FragmentActivity.class));
    }
}
