package com.wct.login_impl;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/loginimpl/login")
public class LoginActivity extends AppCompatActivity {

    private TextView tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);

        initView();
        updateLoginState();
    }

    private void initView() {
        tvState = (TextView) findViewById(R.id.tv_login_state);
    }

    public void login(View view) {
        AccountUtils.userInfo = new UserInfo("9802071", "wangchenteng");
        updateLoginState();
    }

    private void updateLoginState() {
        tvState.setText("这里是登录界面：" + (AccountUtils.userInfo == null ? "未登录" : AccountUtils.userInfo.getUserName()));
    }

    public void exit(View view) {
        AccountUtils.userInfo = null;
        updateLoginState();
    }

    public void loginShare(View view) {
        ARouter.getInstance().build("/share/share").withString("share_content", "这篇组件化的文章还挺不错的").navigation();
    }
}
