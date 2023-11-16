package com.protenium.irohub.ui.start_up.auth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.commoninterface.FragmentTransactionListener;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.activity.HomeActivity;
import com.protenium.irohub.ui.start_up.auth.fragment.LoginFragment;

public class AuthActivity extends BaseActivity implements
        FragmentTransactionListener {

    private Fragment currentFragment;
    private boolean doubleBackToExitPressedOnce;

    @Override
    public int setLayout() {
        return R.layout.activity_auth;
    }

    @Override
    public boolean setToolbar() {
        return false;
    }

    @Override
    public boolean hideStatusBar() {
        return false;
    }

    @Override
    public boolean setFullScreen() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchFragment(LoginFragment.newInstance());
    }

    private void launchFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.flContainer, fragment, null);
        if (!(fragment instanceof LoginFragment)) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        currentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof LoginFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click 'BACK' again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        launchFragment(fragment);
    }

    @Override
    public void replaceFragment(String fragmentTag) {

    }

    @Override
    public void popBackStackReplaceFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack();
        launchFragment(fragment);
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void clearBackStack() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void hideToolbar(boolean hideToolbar) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPrefs.getBoolean(SharedPrefs.Keys.IS_LOGIN, false)){
            Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}