package com.protenium.irohub.ui.main.dashboard.more.privacy_policy;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends BaseActivity {

    @BindView(R.id.wvAbout)
    TextView wvAbout;
    private PrivacyPolicyViewModel privacyPolicyViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        privacyPolicyViewModel = ViewModelProviders.of(this).get(PrivacyPolicyViewModel.class);
        privacyPolicyViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        getPrivacyPolicy();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getPrivacyPolicy() {
        privacyPolicyViewModel.getPrivacyPolicy("en").observe(this, aboutUsResponse -> {
            if (aboutUsResponse.getStatus()) {
                wvAbout.setText(Html.fromHtml(aboutUsResponse.getData().getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                CommonUtils.showWarning(this, aboutUsResponse.getMessage());
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_privacy_policy;
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
}