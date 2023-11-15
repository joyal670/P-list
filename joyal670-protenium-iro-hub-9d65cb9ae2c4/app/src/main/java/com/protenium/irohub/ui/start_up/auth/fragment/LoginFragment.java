package com.protenium.irohub.ui.start_up.auth.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.activity.HomeActivity;
import com.protenium.irohub.ui.start_up.auth.viewmodel.AuthViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private AuthViewModel authViewModel;

    @BindView(R.id.signup)
    TextView signup;

    @BindView(R.id.tvskip)
    TextView tvskip;

    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;

    @BindView(R.id.signin)
    Button signin;

    @BindView(R.id.username)
    EditText etEmailOrNumber;

    @BindView(R.id.password)
    EditText etPassword;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        listener.clearBackStack();
        authViewModel = ViewModelProviders.of((AppCompatActivity) context).get(AuthViewModel.class);

        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });
        signup.setOnClickListener(this);
        tvskip.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        signin.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.signup: {
                listener.replaceFragment(SignUpFragment.newInstance());
                break;
            }
            case R.id.tvskip: {
                break;
            }

            case R.id.tvForgotPassword: {
                listener.replaceFragment(ForgotPasswordFragment.newInstance());
                break;
            }

            case R.id.signin: {
                checkFields();
                break;

            }


        }
    }

    private void checkFields() {
        if (TextUtils.isEmpty(etEmailOrNumber.getText())) {
            etEmailOrNumber.setError("Email is required");
        } else if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Password is required");
        } else {
            customerLogin();
        }
    }

    private void customerLogin() {
        authViewModel.userLogin(etEmailOrNumber.getText().toString(), etPassword.getText().toString(), "we").observe(this, loginResponse -> {
            if (loginResponse.getStatus()) {
                SharedPrefs.setBoolean(SharedPrefs.Keys.IS_LOGIN, true);
                SharedPrefs.setString(SharedPrefs.Keys.CUSTOMER_TOKEN, loginResponse.getUser().getAccessToken());
                SharedPrefs.setString(SharedPrefs.Keys.CUSTOMER_ID, loginResponse.getUser().getId().toString());
                SharedPrefs.setString(SharedPrefs.Keys.CUSTOMER_EMAIL, loginResponse.getUser().getEmail());
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                startActivity(intent);
                requireActivity().finishAffinity();

                Toast.makeText(getContext(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}