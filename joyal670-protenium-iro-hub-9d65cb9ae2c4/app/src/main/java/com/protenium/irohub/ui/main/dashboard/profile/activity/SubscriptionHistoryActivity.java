package com.protenium.irohub.ui.main.dashboard.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.profile.adapter.SubscriptionAdapter;
import com.protenium.irohub.ui.main.dashboard.profile.viewmodel.SubscriptionHistoryViewModel;
import com.protenium.irohub.ui.start_up.auth.activity.AuthActivity;
import com.protenium.irohub.ui.start_up.splash.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionHistoryActivity extends BaseActivity implements SubscriptionAdapter.SelectedId {

    private SubscriptionHistoryViewModel subscriptionHistoryViewModel;

    @BindView(R.id.recycerViewSubscriptionHistory)
    RecyclerView recycerViewSubscriptionHistory;

    @BindView(R.id.content)
    ConstraintLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        subscriptionHistoryViewModel = ViewModelProviders.of(this).get(SubscriptionHistoryViewModel.class);
        subscriptionHistoryViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        getSubscriptionDetails();
    }

    private void getSubscriptionDetails() {
        subscriptionHistoryViewModel.getSubscription(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), "en").observe(this, response -> {
            if (response.getStatus()) {

                recycerViewSubscriptionHistory.setLayoutManager(new LinearLayoutManager(this));
                recycerViewSubscriptionHistory.setAdapter(new SubscriptionAdapter(this, response.getData().getSubscriptions()));

                if (response.getData().getSubscriptions().size() == 0) {
                    recycerViewSubscriptionHistory.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                } else {
                    recycerViewSubscriptionHistory.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                }

            } else {
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_subscription_history;
    }

    @Override
    public boolean setToolbar() {
        return false;
    }

    @Override
    public boolean hideStatusBar() {
        return false;
    }

    @Override
    public boolean setFullScreen() {
        return false;
    }

    @Override
    public void setValue(int selectedId) {
        Intent intent = new Intent(this, SubscriptionHistoryDetailsActivity.class);
        intent.putExtra("id", String.valueOf(selectedId));
        startActivity(intent);
        finish();
    }
}