package com.wct.share_api;

import android.widget.Toast;

import static com.wct.componentbase.ServiceManager.getApplication;


/**
 * @author wangchenteng
 * @date 2020/10/18
 * @desc
 */
public class ShareServiceEmpty implements IShareService {
    private final String TAG = this.getClass().getName();

    @Override
    public void share(String content) {
        Toast.makeText(getApplication(), "这是share 的空实现", Toast.LENGTH_SHORT).show();
    }
}
