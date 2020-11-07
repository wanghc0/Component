package com.wct.share_impl;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wct.componentbase.ServiceManager;
import com.wct.login_api.IAccountService;
import com.wct.share.R;

@Route(path = "/share/share")
public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        if (getIntent() != null) {
            String content = getIntent().getStringExtra("share_content");
            if (!TextUtils.isEmpty(content)) {
                ((TextView) findViewById(R.id.share_content)).setText(content);
            }
        }
    }

    public void shareLogin(View view) {
//        ARouter.getInstance().build("/loginimpl/login").navigation(); // 通过路由的方式实现页面跳转

        IAccountService accountService = ServiceManager.getService(IAccountService.class);
        accountService.startLoginActivity(this); //通过接口的方式实现页面跳转
    }
}
