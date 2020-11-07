package com.wct.login_api;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * @author wangchenteng
 * @date 2020/9/1
 * @desc
 */
public class AccountServiceEmpty implements IAccountService {

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccountId() {
        return null;
    }

    @Override
    public Fragment newUserFragment(AppCompatActivity activity, int containerId, FragmentManager manager, Bundle bundle, String tag) {
        return null;
    }

    @Override
    public void startLoginActivity(Context context) {

    }


}
