package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.notifications;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.OnRecyclerItemClickListener;
import com.ashtechlabs.teleporter.item_deceration.VerticalSpaceItemDecoration;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorNotificationList;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorNotifications;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_rate.UpdateRateVendorActivity;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

 public class CargoNotificationActivity extends BaseActivity implements ICargoNotificationsControllerCallback, OnRecyclerItemClickListener {

    ICargoNotificationsController vendor_notifications_controller;
    CargoNotifyAdapter adapter;
    RecyclerView list;
    private ArrayList<VendorNotificationList> vendorNotificationList;
    private View noDataFound;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setupToolbar();

        initView();
        token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, "");

//        Intent intent = getIntent();
//        if (intent != null) {
//            token = intent.getStringExtra("token");
//        }

        vendor_notifications_controller = new CargoNotificationsController(this);
        vendor_notifications_controller.onNotifyController(token);
    }

    private void initView() {
        list = (RecyclerView) findViewById(R.id.rv_notify);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new VerticalSpaceItemDecoration(12));
        noDataFound = findViewById(R.id.noDataFound);
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notification");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    public void onGetNotifyDetails(VendorNotifications notify) {
        vendorNotificationList = notify.getReviewService();
        if(vendorNotificationList.size()>0){
            adapter = new CargoNotifyAdapter(CargoNotificationActivity.this, vendorNotificationList);
            adapter.setClickListener(this);
            list.setAdapter(adapter);

        }else{
            noDataFound.setVisibility(View.VISIBLE);
            showLoadingDialog(false);
        }

    }

    @Override
    public void onGetNotifyDetailsFailed(String message) {
        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingDialog(boolean isShow) {
        if (isShow) showProgress();
        else dismissProgress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view, int position, int serviceType) {

        Intent i = new Intent(this, UpdateRateVendorActivity.class);
        i.putExtra("priceVendor", vendorNotificationList.get(position).getRateCardVendor().get(0));
        i.putExtra("rate_card_type", Constants.RATE_CARD_UPDATE);
        startActivity(i);
    }
}


