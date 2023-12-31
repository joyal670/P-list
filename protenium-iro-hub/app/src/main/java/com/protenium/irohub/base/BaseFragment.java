package com.protenium.irohub.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.protenium.irohub.commoninterface.FragmentTransactionListener;
import com.protenium.irohub.dialog.ProgressDialogFragment;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    public Context context;
    public FragmentTransactionListener listener;
    private DialogFragment progressDialogFragment;

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
        View view = setView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        return view;
    }

    public abstract View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public void showProgress(){
        progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.show(getFragmentManager(), null);
    }

    public void dismissProgress(){
        if (progressDialogFragment != null) {
            progressDialogFragment.dismiss();
        }
    }
}
