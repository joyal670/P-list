package com.protenium.irohub.ui.main.dashboard.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.ui.main.dashboard.activity.HomeActivity;
import com.protenium.irohub.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.setting_spinner)
    RelativeLayout setting_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setOnClicks();
    }

    private void setOnClicks() {
        setting_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpBottomSheet();
            }
        });

    }

    private void setUpBottomSheet() {

        BottomSheetDialog bottom = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        View view = this.getLayoutInflater().inflate(R.layout.layout_bottomsheet_select_lang, null);

        ImageView close = view.findViewById(R.id.ivCloseSelectlanguage);
        MaterialButton selectBtn = view.findViewById(R.id.SelectLanguageBtn);
        RadioButton selectArabic = view.findViewById(R.id.SelectArabic);
        RadioButton selectEnglish = view.findViewById(R.id.SelectEnglish);

        close.setOnClickListener(it -> bottom.dismiss());
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectArabic.isChecked()) {
                    bottom.dismiss();
                    CommonUtils.changeLanguageAwareContext(SettingsActivity.this, "ar");
                    Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else if (selectEnglish.isChecked()) {
                    bottom.dismiss();
                    CommonUtils.changeLanguageAwareContext(SettingsActivity.this, "en");
                    Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    CommonUtils.showWarning(SettingsActivity.this, getString(R.string.select_lang));
                }
            }
        });


        bottom.setContentView(view);
        bottom.show();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_settings;
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