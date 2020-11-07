package com.wct.login_impl;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wct.base.BaseApp;

public class ComponentApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 ARouter
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);

        LoginApp loginApp = new LoginApp();
        loginApp.onCreate();
        loginApp.init(this);

        init(this);
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }


    @Override
    public void init(Application application) {
        Log.i("Application", "LoginApp init");

    }
}
