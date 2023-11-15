package com.protenium.irohub.ui.main.dashboard.profile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.TextView;

import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.ui.main.dashboard.profile.viewmodel.SubscriptionDetailsModel;
import com.protenium.irohub.ui.main.dashboard.profile.viewmodel.SubscriptionHistoryViewModel;
import com.protenium.irohub.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionHistoryDetailsActivity extends BaseActivity {

    private SubscriptionDetailsModel subscriptionDetailsModel;
    private String selectedId = "";

    @BindView(R.id.tvSubscriptionDetailCategoryPlan)
    TextView tvSubscriptionDetailCategoryPlan;

    @BindView(R.id.tvAmount)
    TextView tvAmount;

    @BindView(R.id.tvDuration)
    TextView tvDuration;

    @BindView(R.id.tvStartDateValue)
    TextView tvStartDateValue;

    @BindView(R.id.tvEndDateValue)
    TextView tvEndDateValue;

    @BindView(R.id.tvDeliveryAddress)
    TextView tvDeliveryAddress;

    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        selectedId = getIntent().getStringExtra("id");
        subscriptionDetailsModel = ViewModelProviders.of(this).get(SubscriptionDetailsModel.class);
        subscriptionDetailsModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        getSubscriptionDetails();
    }

    private void getSubscriptionDetails() {

        subscriptionDetailsModel.getSubscription(selectedId, "en").observe(this, subscriptionDetailsResponse -> {
            if(subscriptionDetailsResponse.getStatus()){

                tvSubscriptionDetailCategoryPlan.setText(subscriptionDetailsResponse.getData().getMealCategoryName());
                tvAmount.setText( "KWD " +subscriptionDetailsResponse.getData().getTotal().toString());
                tvStatus.setText( subscriptionDetailsResponse.getData().getPlanStatus());
                tvDuration.setText(subscriptionDetailsResponse.getData().getDuration().toString() + " " + "Weeks");
                tvStartDateValue.setText(subscriptionDetailsResponse.getData().getPlanStartDate());
                tvEndDateValue.setText(subscriptionDetailsResponse.getData().getPlanEndDate());
                tvDeliveryAddress.setText(subscriptionDetailsResponse.getData().getDeliveryAddress().getId() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getTitle() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getGovernorate() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getArea() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getBlock() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getAvenue() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getStreet() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getBuilding() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getFloor() + " , " +
                        subscriptionDetailsResponse.getData().getDeliveryAddress().getAppartment());
            }else {
                CommonUtils.showWarning(this, subscriptionDetailsResponse.getMessage());
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_subscription_history_details;
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
}