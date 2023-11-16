package com.protenium.irohub.ui.start_up.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.ui.start_up.auth.activity.AuthActivity;

public class SplashActivity extends BaseActivity {

    Handler handler;

    @Override
    public int setLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean setToolbar() {
        return false;
    }

    @Override
    public boolean hideStatusBar() {
        return true;
    }

    @Override
    public boolean setFullScreen() {
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}