package com.protenium.irohub.ui.main.dashboard.more.contact_us;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsActivity extends BaseActivity {

    @BindView(R.id.etcontactName)
    EditText etcontactName;
    @BindView(R.id.etcontactPhoneNumber)
    EditText etcontactPhoneNumber;
    @BindView(R.id.etcontactEmail)
    EditText etcontactEmail;
    @BindView(R.id.etcontactMessage)
    EditText etcontactMessage;
    @BindView(R.id.contactSubmitBtn)
    MaterialButton contactSubmitBtn;
    private ContactUsViewModel contactUsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        contactUsViewModel = ViewModelProviders.of(this).get(ContactUsViewModel.class);
        contactUsViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        setOnClick();
    }

    private void setOnClick() {

        contactSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etcontactName.getText().toString();
                String number = etcontactPhoneNumber.getText().toString();
                String email = etcontactEmail.getText().toString();
                String message = etcontactMessage.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    CommonUtils.showWarning(ContactUsActivity.this, "Name is required");
                } else if (TextUtils.isEmpty(number)) {
                    CommonUtils.showWarning(ContactUsActivity.this, "Phone number is requires");
                } else if (TextUtils.isEmpty(email)) {
                    CommonUtils.showWarning(ContactUsActivity.this, "Email address is required");
                } else if (TextUtils.isEmpty(message)) {
                    CommonUtils.showWarning(ContactUsActivity.this, "Message is requires");
                } else {
                    contactUsViewModel.getContactUs(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), name, number, email, message, "en").observe(ContactUsActivity.this, contactUsResponse -> {
                        if (contactUsResponse.getStatus()) {
                            CommonUtils.showWarning(ContactUsActivity.this, contactUsResponse.getMessage());
                        } else {
                            CommonUtils.showWarning(ContactUsActivity.this, contactUsResponse.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_contact_us;
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