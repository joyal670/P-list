package com.user.student.ui.start_up.auth.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.user.student.base.BaseActivity;
import com.user.student.databinding.ActivityAuthBinding;
import com.user.student.ui.start_up.auth.adapter.ViewPagerAdapter;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private String[] labels = new String[]{"Login", "Sign up"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(labels[position]);
        }).attach();
    }

}