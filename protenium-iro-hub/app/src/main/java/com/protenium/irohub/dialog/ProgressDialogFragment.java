package com.protenium.irohub.dialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseDialogFragment;


public class ProgressDialogFragment extends BaseDialogFragment {


    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_dialog, container, false);
        return view;
    }

}
