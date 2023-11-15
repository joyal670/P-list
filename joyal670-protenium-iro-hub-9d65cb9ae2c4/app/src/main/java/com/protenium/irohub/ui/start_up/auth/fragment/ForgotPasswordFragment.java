package com.protenium.irohub.ui.start_up.auth.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.ui.start_up.auth.viewmodel.AuthViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {

    private AuthViewModel authViewModel;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.send)
    Button send;


    public static ForgotPasswordFragment newInstance() {
        Bundle args = new Bundle();
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, view);
        authViewModel = ViewModelProviders.of((AppCompatActivity) context).get(AuthViewModel.class);
        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });
        send.setOnClickListener(this);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send: {
                checkFields();
            }
        }

    }

    private void checkFields() {
        if (TextUtils.isEmpty(send.getText())) {
            email.setError("Email is required");
        }else {
            forgotPassword();
        }
    }

    private void forgotPassword() {
        authViewModel.userForgotPassword(email.getText().toString()).observe(this, response ->{
            if(response.getStatus()){
                Toast.makeText(getContext(), "Forgot Password link sent to mail", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}