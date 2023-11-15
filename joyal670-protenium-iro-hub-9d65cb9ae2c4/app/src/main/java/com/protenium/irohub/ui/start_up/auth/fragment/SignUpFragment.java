package com.protenium.irohub.ui.start_up.auth.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.activity.HomeActivity;
import com.protenium.irohub.ui.start_up.auth.viewmodel.AuthViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private AuthViewModel authViewModel;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.gender)
    TextView gender;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.confirmpassword)
    EditText confirmpassword;

    @BindView(R.id.phoneno)
    EditText phoneno;

    @BindView(R.id.alternativeno)
    EditText alternativeno;

    @BindView(R.id.chkTermsAndConditions)
    CheckBox chkTermsAndConditions;

    @BindView(R.id.signup2)
    Button signup2;


    public static SignUpFragment newInstance() {
        Log.e("TAG", "newInstance: ");
        Bundle args = new Bundle();
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        authViewModel = ViewModelProviders.of((AppCompatActivity) context).get(AuthViewModel.class);
        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });
        signup2.setOnClickListener(this);
        gender.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup2: {
                checkFields();
                break;
            }
            case R.id.gender: {
                setUpBottomSheetDialog();
                break;
            }

        }
    }

    private void setUpBottomSheetDialog() {
        BottomSheetDialog bottom = new BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog);
        View view = requireActivity().getLayoutInflater().inflate(R.layout.layout_bottomsheet, null);
        RadioGroup SelectGenderRadioGrp = view.findViewById(R.id.SelectGenderRadioGrp);
        ImageView close = view.findViewById(R.id.ivCloseSelectGender);
        MaterialButton selectBtn = view.findViewById(R.id.SelectGenderBtn);

        SelectGenderRadioGrp.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.SelectGenderMale: {
                    gender.setText("Male");

                    break;
                }
                case R.id.SelectGenderFemale: {
                    gender.setText("Female");
                    break;
                }
            }
        });

        close.setOnClickListener(it -> bottom.dismiss());

        selectBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(gender.getText().toString())) {
                gender.setError("Gender is required");
            } else {
                bottom.dismiss();
            }
        });

        bottom.setContentView(view);
        bottom.show();


    }

    private void checkFields() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Name is required");
        } else if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Email is required");
        } else if (TextUtils.isEmpty(gender.getText().toString())) {
            gender.setError("Gender is required");
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Password is required");
        } else if (TextUtils.isEmpty(confirmpassword.getText().toString())) {
            confirmpassword.setError("Confirm password is required");
        } else if (TextUtils.isEmpty(phoneno.getText().toString())) {
            phoneno.setError("Phone no is required");
        } else if (TextUtils.isEmpty(alternativeno.getText().toString())) {
            alternativeno.setError("Alternative no is required");
        } else if (!chkTermsAndConditions.isChecked()) {
            chkTermsAndConditions.setError("Please accept terms and conditions");
        } else if (!(password.getText().toString()).equals(confirmpassword.getText().toString())) {
            confirmpassword.setError("Password miss match");
        } else {
            registerAccount();
        }

    }

    private void registerAccount() {
        String _gender = "";
        if (gender.getText().toString().equals("Male")) {
            _gender = "M";
        } else if (gender.getText().toString().equals("Female")) {
            _gender = "F";
        }

        authViewModel.userRegister(name.getText().toString(), email.getText().toString(), phoneno.getText().toString(), alternativeno.getText().toString(), password.getText().toString(), _gender, 1, "en", "qwe").observe(this, registerResponse -> {
            if (registerResponse.getStatus()) {
                SharedPrefs.setString("test","login");
                SharedPrefs.setBoolean(SharedPrefs.Keys.IS_LOGIN, true);
                SharedPrefs.setString(SharedPrefs.Keys.CUSTOMER_TOKEN, registerResponse.getUser().getAccessToken());
                SharedPrefs.setString(SharedPrefs.Keys.CUSTOMER_ID, registerResponse.getUser().getId().toString());
                SharedPrefs.setString(SharedPrefs.Keys.CUSTOMER_EMAIL, registerResponse.getUser().getEmail());
                Toast.makeText(getContext(), "" + registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                startActivity(intent);
                requireActivity().finishAffinity();
            } else {
                Toast.makeText(getContext(), "" + registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}