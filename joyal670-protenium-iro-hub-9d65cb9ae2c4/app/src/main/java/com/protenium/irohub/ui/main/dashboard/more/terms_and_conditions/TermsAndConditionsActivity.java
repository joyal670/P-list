package com.protenium.irohub.ui.main.dashboard.more.terms_and_conditions;

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

public class TermsAndConditionsActivity extends BaseActivity {

    @BindView(R.id.termsAndCondition_webView)
    TextView termsAndCondition_webView;
    private TermsAnsConditionsViewModel termsAnsConditionsViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        termsAnsConditionsViewModel = ViewModelProviders.of(this).get(TermsAnsConditionsViewModel.class);
        termsAnsConditionsViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        getTermsAndConditions();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getTermsAndConditions() {
        termsAnsConditionsViewModel.getPrivacyPolicy("en").observe(this, aboutUsResponse -> {
            if (aboutUsResponse.getStatus()) {
                termsAndCondition_webView.setText(Html.fromHtml(aboutUsResponse.getData().getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                CommonUtils.showWarning(this, aboutUsResponse.getMessage());
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_terms_and_conditions;
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