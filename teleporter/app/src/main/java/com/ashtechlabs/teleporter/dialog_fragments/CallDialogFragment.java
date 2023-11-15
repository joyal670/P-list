package com.ashtechlabs.teleporter.dialog_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;

/**
 * Created by VIDHU on 10/20/2016.
 */

public class CallDialogFragment extends DialogFragment implements View.OnClickListener {

    private String tag_dialog;
    private String message, title;
    //private OnAlertDialogClickListener alertDialogClickListener;
    private TextView tvPhoneNumber;

    public static CallDialogFragment newInstance(String title, String message) {
        CallDialogFragment frag = new CallDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("title", message);
        frag.setArguments(args);
        return frag;
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

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//
//        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//        return dialog;
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Dialog);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = inflater.inflate(R.layout.dialog_call_fragment, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view) {

        view.findViewById(R.id.tvCall).setOnClickListener(this);
        view.findViewById(R.id.tvCancel).setOnClickListener(this);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);

    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        alertDialogClickListener = null;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        if (DialogInterface.BUTTON_NEGATIVE == which) {
//            dismiss();
//        }
//        if (DialogInterface.BUTTON_POSITIVE == which) {
//            alertDialogClickListener.onPositiveButtonClick(tag_dialog);
//        }
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCall) {
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvPhoneNumber.getText().toString()));
            startActivity(i);
            dismiss();
        }

        if (v.getId() == R.id.tvCancel) {
            dismiss();
        }
    }
}

