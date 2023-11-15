package com.protenium.irohub.ui.main.dashboard.more.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseFragment;
import com.protenium.irohub.ui.main.dashboard.more.about_us.AboutUsActivity;
import com.protenium.irohub.ui.main.dashboard.more.contact_us.ContactUsActivity;
import com.protenium.irohub.ui.main.dashboard.more.privacy_policy.PrivacyPolicyActivity;
import com.protenium.irohub.ui.main.dashboard.more.terms_and_conditions.TermsAndConditionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoreFragment extends BaseFragment {

    @BindView(R.id.tvAboutUs)
    TextView tvAboutUs;

    @BindView(R.id.tvContactUs)
    TextView tvContactUs;

    @BindView(R.id.tvPrivacyPolicy)
    TextView tvPrivacyPolicy;

    @BindView(R.id.tvTermsAndConditions)
    TextView tvTermsAndConditions;

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);

        setOnClicks();

        return view;
    }

    private void setOnClicks() {
        tvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        tvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        tvTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TermsAndConditionsActivity.class);
                startActivity(intent);
            }
        });

    }
}