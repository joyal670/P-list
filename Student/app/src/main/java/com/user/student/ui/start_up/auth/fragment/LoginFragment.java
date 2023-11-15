package com.user.student.ui.start_up.auth.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.user.student.MainActivity;
import com.user.student.R;
import com.user.student.databinding.FragmentLoginBinding;
import com.user.student.ui.start_up.auth.activity.AuthActivity;
import com.user.student.ui.start_up.splash.activity.SplashActivity;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        setOnClicks();
        return binding.getRoot();
    }

    private void setOnClicks() {

        binding.loginBtn.setOnClickListener( view -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}