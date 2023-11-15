package com.protenium.irohub.base;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.protenium.irohub.R;
import com.protenium.irohub.dialog.ProgressDialogFragment;

public abstract class BaseActivity extends AppCompatActivity {

    private DialogFragment progressDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hideStatusBar()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (setFullScreen()) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(setLayout());
        if (setToolbar()) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

    }

    public abstract int setLayout();

    public abstract boolean setToolbar();

    public abstract boolean hideStatusBar();

    public abstract boolean setFullScreen();

    public void showProgress() {
        progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.show(getSupportFragmentManager(), null);
    }

    public void dismissProgress() {
        if (progressDialogFragment != null) {
            progressDialogFragment.dismiss();
        }
    }
}
