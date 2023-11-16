package com.user.student.base;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;


public abstract class BaseActivity<DB extends ViewBinding> extends AppCompatActivity {

    DB binding;
    public abstract DB getViewBinding();
    public abstract void setupUI();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        setupUI();

    }



}
