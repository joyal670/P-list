package com.user.student.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;



public abstract class BaseFragment<DB extends ViewBinding> extends Fragment {

    public Context context;
    public FragmentTransactionListener listener;
    public abstract DB getViewBinding();
    DB binding;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof FragmentTransactionListener) {
            listener = (FragmentTransactionListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  View view = setView(inflater, container, savedInstanceState);
        binding = getViewBinding();
        return binding.getRoot();
    }

  //public abstract View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

}
