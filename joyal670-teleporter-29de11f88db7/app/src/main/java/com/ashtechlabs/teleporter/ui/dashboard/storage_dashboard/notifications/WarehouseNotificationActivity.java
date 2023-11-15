package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.notifications;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.OnRecyclerItemClickListener;
import com.ashtechlabs.teleporter.item_deceration.VerticalSpaceItemDecoration;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WarehouseNotification;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WarehouseNotificationList;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.update_rate.UpdateRateWarehouseActivity;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class WarehouseNotificationActivity extends BaseActivity implements NotificationControllerCallback, OnRecyclerItemClickListener {


    private INotificationsController vendor_notifications_controller;
    private WarehouseNotifyAdapter adapter;
    private RecyclerView list;
    private TextView noDataFound;
    private ArrayList<WarehouseNotificationList> warehouseNotificationList;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setUpToolbar();

        initViews();


        token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, "");

//        Intent intent = getIntent();
//        if (intent != null) {
//            token = intent.getStringExtra("token");
//        }

        vendor_notifications_controller = new NotificationController(this);
        vendor_notifications_controller.onNotifyController(token);
    }

    private void initViews() {

        list = (RecyclerView) findViewById(R.id.rv_notify);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new VerticalSpaceItemDecoration(12));
        noDataFound = (TextView) findViewById(R.id.noDataFound);
    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notification");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onGetNotifyDetails(WarehouseNotification notify) {
        warehouseNotificationList = notify.getReviewService();
        if(warehouseNotificationList.size()>0) {
            adapter = new WarehouseNotifyAdapter(this, warehouseNotificationList);
            adapter.setClickListener(this);
            list.setAdapter(adapter);
        }else{
            noDataFound.setVisibility(View.VISIBLE);
            showLoadingDialog(false);
        }
    }

    public void showLoadingDialog(boolean isShow) {
        if (isShow) showProgress();
        else dismissProgress();
    }

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
        Intent i = new Intent(this, UpdateRateWarehouseActivity.class);
        i.putExtra("pricingWareHouse", warehouseNotificationList.get(position).getRateCardWarehouse().get(0));
        i.putExtra("rate_card_type", Constants.RATE_CARD_UPDATE);
        startActivity(i);
    }
}


