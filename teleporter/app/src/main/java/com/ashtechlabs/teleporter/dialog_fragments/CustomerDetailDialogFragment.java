package com.ashtechlabs.teleporter.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.common_interfaces.CustomerDetailDialogListener;

/**
 * Created by VIDHU on 2/10/2017.
 */

public class CustomerDetailDialogFragment extends DialogFragment {

    private CustomerDetailDialogListener detailDialogListener;

    public static CustomerDetailDialogFragment newInstance(String name, String mobileNumber) {
        CustomerDetailDialogFragment frag = new CustomerDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("phone", mobileNumber);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_customer_detail);
        dialog.show();

        TextView tvName = (TextView)dialog.findViewById(R.id.tvCustomerName);
        TextView tvMobileNumber = (TextView)dialog.findViewById(R.id.tvMobileNumber);

        if(getArguments()!=null){
            tvName.setText(getArguments().getString("name"));
            tvMobileNumber.setText(getArguments().getString("phone"));
        }

        dialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialog.findViewById(R.id.tvAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(detailDialogListener!=null){
                    detailDialogListener.onAcceptTheOrder();
                }
                //parcelItemDialogListener.onDismissParcelItemDialog(true);
            }
        });

        return dialog;
    }

    public void setListner(CustomerDetailDialogListener detailDialogListener) {
        this.detailDialogListener = detailDialogListener;
    }


}
