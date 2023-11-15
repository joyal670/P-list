package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.notifications;

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
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.CourierNotificationList;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.CourierNotifications;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.update_rate.UpdateRateDriverActivity;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class CourierNotificationActivity extends BaseActivity implements CourierNotificationsControllerCallback, OnRecyclerItemClickListener {

    ICourierNotificationsController vendor_notifications_controller;
    CourierNotifyAdapter adapter;
    RecyclerView list;
    private ArrayList<CourierNotificationList> notif;
    private View noDataFound;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initToolbar();

        initView();

        token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, "");

//        Intent intent = getIntent();
//        if (intent != null) {
//            token = intent.getStringExtra("token");
//        }

        vendor_notifications_controller = new CourierNotificationController(this);
        vendor_notifications_controller.onNotifyController(token);
    }

    private void initView() {

        list = (RecyclerView) findViewById(R.id.rv_notify);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new VerticalSpaceItemDecoration(12));
        noDataFound = findViewById(R.id.noDataFound);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notification");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onGetNotifyDetails(CourierNotifications notify) {
        notif = notify.getReviewService();

        if (notif.size() > 0) {
            list.setHasFixedSize(true);
            adapter = new CourierNotifyAdapter(CourierNotificationActivity.this, notif);
            adapter.setClickListener(this);
            list.setAdapter(adapter);
        } else {
            noDataFound.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onGetNotifyDetailsFailed(String notify) {
        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
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
    public void showLoadingDialog(boolean show) {
        if (show) showProgress();
        else dismissProgress();
    }

    @Override
    public void onClick(View view, int position, int serviceType) {
        Intent i = new Intent(this, UpdateRateDriverActivity.class);
        i.putExtra("price_driver", notif.get(position).getRateCardCourier().get(0));
        i.putExtra("priceDriverDuration", notif.get(0).getRateCardCourier().get(0).getDuration());
        i.putExtra("rate_card_type", Constants.RATE_CARD_UPDATE);
        startActivity(i);
    }
}


