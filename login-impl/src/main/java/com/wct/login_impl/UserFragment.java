package com.wct.login_impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_user, container, false);
        TextView tvName = view.findViewById(R.id.tv_user_name);
        tvName.setText(AccountUtils.userInfo == null ? "用户未登录" : "登录用户：" + AccountUtils.userInfo.getUserName());
        return view;
    }
}
