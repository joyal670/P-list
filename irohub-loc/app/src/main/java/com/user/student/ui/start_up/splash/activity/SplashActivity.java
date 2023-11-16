package com.user.student.ui.start_up.splash.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;

import com.user.student.base.BaseActivity;
import com.user.student.databinding.ActivitySplashBinding;
import com.user.student.ui.start_up.auth.activity.AuthActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    @Override
    public ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void setupUI() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}