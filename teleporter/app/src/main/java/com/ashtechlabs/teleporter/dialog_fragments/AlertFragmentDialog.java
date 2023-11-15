package com.ashtechlabs.teleporter.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import com.ashtechlabs.teleporter.R;

/**
 * Created by VIDHU on 10/20/2016.
 */

public class AlertFragmentDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private String tag_dialog;
    private String message, title;

    public static AlertFragmentDialog newInstance(String title, String message) {
        AlertFragmentDialog frag = new AlertFragmentDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        message = getArguments().getString("message");
        title = getArguments().getString("title");

        return new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Ok", this)
                .create();
    }

//    @Override
//    public void onAttach(Context activity) {
//        super.onAttach(activity);
//        try {
//            alertDialogClickListener = (OnAlertDialogClickListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + "attaching d fragment failed!");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        alertDialogClickListener = null;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (DialogInterface.BUTTON_NEGATIVE == which) {
            dismiss();
        }
    }
}

