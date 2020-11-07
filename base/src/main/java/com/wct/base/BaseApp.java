package com.wct.base;

import android.app.Application;

/**
 * @author wangchenteng
 * @date 2020/8/31
 * @desc
 */
public abstract class BaseApp extends Application {
    public abstract void init(Application application);
}
