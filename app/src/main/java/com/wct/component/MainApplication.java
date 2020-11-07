package com.wct.component;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wct.base.AppConfig;
import com.wct.base.BaseApp;
import com.wct.componentbase.ServiceManager;

public class MainApplication extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 ARouter
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);

        ServiceManager.init(this);
        init(this);
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void init(Application application) {
        Log.i("Application", "MainApplication init");
        for (String moduleApp : AppConfig.moduleApps) {
            try {
                Class clazz = Class.forName(moduleApp);
                BaseApp baseApp = (BaseApp) clazz.newInstance();
                baseApp.init(this);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
