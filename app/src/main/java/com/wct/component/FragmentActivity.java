package com.wct.component;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wct.component.R;
import com.wct.componentbase.ServiceManager;
import com.wct.login_api.IAccountService;


public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        ServiceManager.getService(IAccountService.class).newUserFragment(this, R.id.layout_fragment, getSupportFragmentManager(), null, "");
    }
}
