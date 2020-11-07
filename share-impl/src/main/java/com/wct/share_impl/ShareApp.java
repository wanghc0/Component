package com.wct.share_impl;

import android.app.Application;

import com.wct.base.BaseApp;
import com.wct.componentbase.ServiceManager;
import com.wct.share_api.IShareService;

/**
 * @author wangchenteng
 * @date 2020/10/24
 * @desc
 */
public class ShareApp extends BaseApp {

    @Override
    public void init(Application application) {
        ServiceManager.registerService(IShareService.class, new ShareService());
    }
}
