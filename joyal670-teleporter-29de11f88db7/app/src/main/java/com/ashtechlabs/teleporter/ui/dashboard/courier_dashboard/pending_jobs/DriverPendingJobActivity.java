package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pending_jobs;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.PendingJobsAdapter;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DeliveryService;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class DriverPendingJobActivity extends BaseActivity implements DriverPendingJobsControllerCallback {

    private IDriverPendingJobsController driver_pendingJobsController;
    private RecyclerView recyclerView;
    private TextView tvHolder;
    private EditText editText;
    private PendingJobsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<DeliveryService> deliveryService;
    private String[] array_delivery;
    //private String[] array_mover;
    private String text = "";
    private ArrayList<MyLogsAndOrderInfoDriver> pendingJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_jobs);

        setupToolBar();

        array_delivery = getResources().getStringArray(R.array.array_delivery);
        //array_mover = getResources().getStringArray(R.array.array_driver_service);

        initViews();

        driver_pendingJobsController = new DriverPendingJobsController(this);
        driver_pendingJobsController.getPendingJobs(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));
    }

    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.rv_pending);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        tvHolder = (TextView) findViewById(R.id.tvHolder);
        editText = (EditText) findViewById(R.id.search);

    }

    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Pending Jobs");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
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
    public void onGetPendingJobs(DriverOrder deliveryServices) {


        ArrayList<DeliveryService> deliveryServiceArrayList = deliveryServices.getDeliveryService();
     //   ArrayList<MpService> mpServiceArrayList = deliveryServices.getMpServices();

        pendingJobsList = new ArrayList<>();

        for (int i = 0; i < deliveryServiceArrayList.size(); i++) {

            getPendingJobDelivery(deliveryServiceArrayList.get(i));

        }

       /* for (int i = 0; i < mpServiceArrayList.size(); i++) {

            getPendingJobMp(mpServiceArrayList.get(i));

        }
*/

        adapter = new PendingJobsAdapter(DriverPendingJobActivity.this, pendingJobsList);
        recyclerView.setAdapter(adapter);
        addTextListener();

        if (adapter.getItemCount() > 0) {
            editText.setVisibility(View.VISIBLE);
            tvHolder.setVisibility(View.GONE);
        } else {
            tvHolder.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
        }
    }



    private void getPendingJobDelivery(DeliveryService deliveryService) {

        pendingJobsList.add(new MyLogsAndOrderInfoDriver(deliveryService.getId(),
                deliveryService.getDate().substring(11), deliveryService.getDate().substring(0, 10),
                deliveryService.getFromlocation(), deliveryService.getTolocation(),
                deliveryService.getState(), array_delivery[Integer.valueOf(deliveryService.getItemtype())],
                deliveryService.getOrderid(), deliveryService.getOrdertype(),
                deliveryService.getCommodity_description(),deliveryService.getPayment(),
                deliveryService.getWeight(),deliveryService.getWeight_unit(),
                deliveryService.getOrder_number(),deliveryService.getPrice(),deliveryService.getCurrency(),deliveryService.getDuration()));
    }

    @Override
    public void onGetPendingJobsFailed(String message) {
        CommonUtils.showToast(this, message);
    }



    private void addTextListener() {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final ArrayList<MyLogsAndOrderInfoDriver> filteredList = new ArrayList<>();

                if (pendingJobsList != null) {

                    for (int i = 0; i < pendingJobsList.size(); i++) {

                        if (pendingJobsList.get(i).getOrder_number().equals("")) {
                            text = pendingJobsList.get(i).getOrderid().toLowerCase();
                        } else {
                            text = pendingJobsList.get(i).getOrder_number().toLowerCase();
                        }


                        if (text.contains(query)) {

                            filteredList.add(pendingJobsList.get(i));
                        }
                    }
                    adapter = new PendingJobsAdapter(DriverPendingJobActivity.this, filteredList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                if (adapter.getItemCount() > 0)
                    tvHolder.setVisibility(View.GONE);
                else tvHolder.setVisibility(View.VISIBLE);

            }
        });
    }


    @Override
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }
}
