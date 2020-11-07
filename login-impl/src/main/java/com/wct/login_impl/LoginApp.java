package com.wct.login_impl;

import android.app.Application;
import android.util.Log;

import com.wct.base.BaseApp;
import com.wct.componentbase.ServiceManager;
import com.wct.login_api.IAccountService;

public class LoginApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }


    @Override
    public void init(Application application) {
        Log.i("Application", "LoginApp init");
        ServiceManager.registerService(IAccountService.class, new AccountService());
    }
}
