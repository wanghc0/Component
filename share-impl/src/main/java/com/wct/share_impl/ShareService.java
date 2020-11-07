package com.wct.share_impl;

import android.widget.Toast;

import com.wct.componentbase.ServiceManager;
import com.wct.login_api.IAccountService;
import com.wct.share_api.IShareService;

import static com.wct.componentbase.ServiceManager.getApplication;

class ShareService implements IShareService {
    private final String TAG = "ShareService";

    @Override
    public void share(String content) {

//        IAccountService newAccountService = new AccountService();//无法通过new 获取类的实例

        IAccountService accountService = (IAccountService) ServiceManager.getService(IAccountService.class);
        if (accountService.isLogin()) {
            Toast.makeText(getApplication(), "分享成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "用户未登录, 分享失败", Toast.LENGTH_SHORT).show();
        }
    }
}
