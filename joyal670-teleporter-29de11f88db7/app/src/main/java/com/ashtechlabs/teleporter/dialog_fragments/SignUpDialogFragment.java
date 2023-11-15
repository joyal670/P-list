package com.ashtechlabs.teleporter.dialog_fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.SignUpDialogListener;
import com.ashtechlabs.teleporter.util.GlobalUtils;

/**
 * Created by VIDHU on 2/10/2017.
 */

public class SignUpDialogFragment extends DialogFragment implements View.OnClickListener{

    private SignUpDialogListener signUpDialogListener;

    public static SignUpDialogFragment newInstance() {
        return new SignUpDialogFragment();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_signup_as);
        dialog.show();

        TextView tvDriver = (TextView)dialog.findViewById(R.id.tvDriver);
        tvDriver.setOnClickListener(this);
        TextView tvVendors = (TextView)dialog.findViewById(R.id.tvVendors);
        tvVendors.setOnClickListener(this);
        TextView tvStorage = (TextView)dialog.findViewById(R.id.tvStorage);
        tvStorage.setOnClickListener(this);

        return dialog;
    }

        @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            signUpDialogListener = (SignUpDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "attaching d fragment failed!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        signUpDialogListener = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tvStorage:
                signUpDialogListener.onWhichModeSelected(GlobalUtils.MODE_STORAGE);
                break;

            case R.id.tvVendors:
                signUpDialogListener.onWhichModeSelected(GlobalUtils.MODE_CARGO);
                break;

            case R.id.tvDriver:
                signUpDialogListener.onWhichModeSelected(GlobalUtils.MODE_COURIER);
                break;
        }

    }
}
