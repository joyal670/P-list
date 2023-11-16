package com.protenium.irohub.ui.main.dashboard.profile.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.profile.activity.DislikesActivity;
import com.protenium.irohub.ui.main.dashboard.profile.activity.MyProfileActivity;
import com.protenium.irohub.ui.main.dashboard.profile.activity.SettingsActivity;
import com.protenium.irohub.ui.main.dashboard.profile.activity.SubscriptionHistoryActivity;
import com.protenium.irohub.ui.start_up.auth.activity.AuthActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyAccountFragment extends BaseFragment {

    @BindView(R.id.tvMyProfile)
    TextView tvMyProfile;

    @BindView(R.id.tvDislike)
    TextView tvDislike;

    @BindView(R.id.tvSubscriptionHistory)
    TextView tvSubscriptionHistory;

    @BindView(R.id.tvSettings)
    TextView tvSettings;

    @BindView(R.id.tvLogout)
    TextView tvLogout;

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, view);

        setOnClicks();

        return view;
    }

    private void setOnClicks() {
        tvMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        tvDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), DislikesActivity.class);
                startActivity(intent);
            }
        });

        tvSubscriptionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SubscriptionHistoryActivity.class);
                startActivity(intent);
            }
        });

        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });

    }

    private void showExitDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.logout_layout);

        ImageView ivClose = dialog.findViewById(R.id.ivCloseLogout);
        MaterialButton okBtnLogout = dialog.findViewById(R.id.okBtnLogout);
        MaterialButton cancelBtnLogout = dialog.findViewById(R.id.cancelBtnLogout);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.clearAllPrefs();
                Intent intent = new Intent(requireContext(), AuthActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        cancelBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}