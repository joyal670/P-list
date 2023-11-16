package com.protenium.irohub.ui.main.dashboard.profile.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.profile.viewmodel.ProfileViewModel;
import com.protenium.irohub.utils.CommonUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends BaseActivity {

    @BindView(R.id.tvChangePassword)
    TextView tvChangePassword;

    @BindView(R.id.myProfileImageView)
    CircleImageView myProfileImageView;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvUserEmail)
    TextView tvUserEmail;

    @BindView(R.id.tvUserGender)
    TextView tvUserGender;

    @BindView(R.id.tvUserMobile)
    TextView tvUserMobile;

    @BindView(R.id.ivEditProfile)
    ImageView ivEditProfile;
    File selectedFile;
    BottomSheetDialog bottom;
    CircleImageView myProfileImageEdit;
    EditText spProfileGender;
    private ProfileViewModel profileViewModel;
    private String userName = "";
    private String userEmail = "";
    private String userImage = "";
    private String userGender = "";
    private String userMobile = "";
    private String userAltMobile = "";

    @Override
    public int setLayout() {
        return R.layout.activity_my_profile;
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
        ButterKnife.bind(this);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });
        initData();
    }

    private void initData() {
        getUserDetails();
        setOnClicks();
    }

    private void getUserDetails() {
        profileViewModel.getProfileDetails(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), "en").observe(this, getProfileDetailsResponse -> {
            if (getProfileDetailsResponse.getStatus()) {

                userName = getProfileDetailsResponse.getData().getName();
                userEmail = getProfileDetailsResponse.getData().getEmail();
                userImage = getProfileDetailsResponse.getData().getImage();
                userGender = getProfileDetailsResponse.getData().getGender();
                userMobile = getProfileDetailsResponse.getData().getPhone();
                userAltMobile = getProfileDetailsResponse.getData().getAlternativePhone();


                Glide.with(MyProfileActivity.this).load(userImage).placeholder(R.drawable.ic_profile).into(myProfileImageView);
                tvUserName.setText(userName);
                tvUserEmail.setText(userEmail);
                tvUserGender.setText(userGender);
                tvUserMobile.setText(userMobile);
            } else {
                CommonUtils.showWarning(MyProfileActivity.this, getProfileDetailsResponse.getMessage());
            }
        });
    }

    private void setOnClicks() {

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpChangePasswordBottomSheet();
            }
        });

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpEditProfileBottomSheet();
            }
        });
    }

    private void setUpEditProfileBottomSheet() {
        bottom = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        bottom.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        View view = this.getLayoutInflater().inflate(R.layout.edit_profile, null);

        ImageView close = view.findViewById(R.id.ivCloseEdit);
        FloatingActionButton fabCameraProfile = view.findViewById(R.id.fabCameraProfile);
        myProfileImageEdit = view.findViewById(R.id.myProfileImageEdit);
        EditText etProfileName = view.findViewById(R.id.etProfileName);
        spProfileGender = view.findViewById(R.id.spProfileGender);
        EditText etProfileMobile = view.findViewById(R.id.etProfileMobile);
        EditText etProfileAlternativeMobile = view.findViewById(R.id.etProfileAlternativeMobile);
        MaterialButton selectBtn = view.findViewById(R.id.btnSubmitProfile);

        Glide.with(MyProfileActivity.this).load(userImage).placeholder(R.drawable.ic_profile).into(myProfileImageEdit);
        etProfileName.setText(userName);
        spProfileGender.setText(userGender);
        etProfileMobile.setText(userMobile);
        etProfileAlternativeMobile.setText(userAltMobile);

        fabCameraProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MyProfileActivity.this).crop().compress(1024).maxResultSize(1080, 1080).start();
            }
        });

        spProfileGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGenderBottomSheet();
            }
        });

        close.setOnClickListener(it -> bottom.dismiss());
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etProfileName.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(MyProfileActivity.this, "Name is required");
                } else if (spProfileGender.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(MyProfileActivity.this, "Gender is required");
                } else if (etProfileMobile.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(MyProfileActivity.this, "Mobile is required");
                } else if (etProfileAlternativeMobile.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(MyProfileActivity.this, "Alternate Mobile is required");
                } else {
                    bottom.dismiss();
                    String sp;
                    if (spProfileGender.getText().toString().equals("Male")) {
                        sp = "M";
                    } else {
                        sp = "F";
                    }
                    profileViewModel.updateCustomerProfile(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), etProfileName.getText().toString(), sp, etProfileMobile.getText().toString(), "en", etProfileAlternativeMobile.getText().toString(), selectedFile).observe(MyProfileActivity.this, updateProfileResponse -> {
                        if (updateProfileResponse.getStatus()) {
                            CommonUtils.showWarning(MyProfileActivity.this, updateProfileResponse.getMessage());
                            initData();
                        } else {
                            CommonUtils.showWarning(MyProfileActivity.this, updateProfileResponse.getMessage());
                        }
                    });
                }
            }
        });


        bottom.setContentView(view);
        bottom.show();
    }

    private void setUpGenderBottomSheet() {
        BottomSheetDialog bottom = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        View view = this.getLayoutInflater().inflate(R.layout.layout_bottomsheet, null);
        RadioGroup SelectGenderRadioGrp = view.findViewById(R.id.SelectGenderRadioGrp);
        ImageView close = view.findViewById(R.id.ivCloseSelectGender);
        MaterialButton selectBtn = view.findViewById(R.id.SelectGenderBtn);

        SelectGenderRadioGrp.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.SelectGenderMale: {
                    userGender = "Male";
                    spProfileGender.setText("Male");
                    break;
                }
                case R.id.SelectGenderFemale: {
                    userGender = "Female";
                    spProfileGender.setText("Female");
                    break;
                }
            }
        });

        close.setOnClickListener(it -> bottom.dismiss());

        selectBtn.setOnClickListener(v -> {
            if (spProfileGender.getText().toString().isEmpty()) {
                CommonUtils.showWarning(MyProfileActivity.this, "Gender is required");
            } else {
                bottom.dismiss();
            }
        });

        bottom.setContentView(view);
        bottom.show();


    }

    private void setUpChangePasswordBottomSheet() {

        BottomSheetDialog bottom = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        View view = this.getLayoutInflater().inflate(R.layout.layout_bottomsheet_change_password, null);

        ImageView close = view.findViewById(R.id.ivClose);
        MaterialButton selectBtn = view.findViewById(R.id.btnSubmit);
        EditText etNewPassword = view.findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword);

        close.setOnClickListener(it -> bottom.dismiss());
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNewPassword.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(MyProfileActivity.this, "Password is required");
                } else if (etConfirmPassword.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(MyProfileActivity.this, "Confirm Password is required");
                } else if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    CommonUtils.showWarning(MyProfileActivity.this, "Password mis-matched");
                } else {
                    profileViewModel.updatePassword(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_EMAIL, ""), etNewPassword.getText().toString(), etConfirmPassword.getText().toString(), "en").observe(MyProfileActivity.this, changePasswordResponse -> {
                        if (changePasswordResponse.getStatus()) {
                            CommonUtils.showWarning(MyProfileActivity.this, changePasswordResponse.getMessage());
                        } else {
                            CommonUtils.showWarning(MyProfileActivity.this, changePasswordResponse.getMessage());
                        }
                    });
                }
            }
        });


        bottom.setContentView(view);
        bottom.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImg = data.getData();
            selectedFile = new File(selectedImg.getPath());
            myProfileImageEdit.setImageURI(selectedImg);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}