package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pending_jobs;

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
import com.ashtechlabs.teleporter.adapters.TruckingPendingJobsAdapter;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.MyLogsAndOrderInfoTrucking;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingOrder;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingService;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class TruckingPendingJobActivity extends BaseActivity implements TruckingPendingJobsControllerCallback {

    private ITruckingPendingJobsController driver_pendingJobsController;
    private RecyclerView recyclerView;
    private TextView tvHolder;
    private EditText editText;
    private TruckingPendingJobsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<TruckingService> truckingService;
    private String[] array_delivery;
    private String[] array_mover;
    private String text = "";
    private ArrayList<MyLogsAndOrderInfoTrucking> pendingJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_jobs);

        setupToolBar();

        array_delivery = getResources().getStringArray(R.array.array_delivery);
        array_mover = getResources().getStringArray(R.array.array_driver_service);

        initViews();

        driver_pendingJobsController = new TruckingPendingJobsController(this);
        driver_pendingJobsController.getPendingJobs(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));
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
    public void onGetPendingJobs(TruckingOrder deliveryServices) {

        ArrayList<TruckingMpService> truckingMpServiceArrayList = deliveryServices.getTruckingMpServices();

        pendingJobsList = new ArrayList<>();

        for (int i = 0; i < truckingMpServiceArrayList.size(); i++) {
            getPendingJobMp(truckingMpServiceArrayList.get(i));
        }

        adapter = new TruckingPendingJobsAdapter(TruckingPendingJobActivity.this, pendingJobsList);
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

    private void getPendingJobMp(TruckingMpService truckingMpService) {

        pendingJobsList.add(new MyLogsAndOrderInfoTrucking(truckingMpService.getId(),
                truckingMpService.getDate().substring(11), truckingMpService.getDate().substring(0, 10),
                truckingMpService.getFromlocation(), truckingMpService.getTolocation(),
                truckingMpService.getState(), CommonUtils.getVechicleType(truckingMpService.getVehicletype(), truckingMpService.getSubVehicleType()),
                truckingMpService.getOrderid(), truckingMpService.getOrdertype(), truckingMpService.getCommodity_description(),
                truckingMpService.getPayment(), truckingMpService.getWeight(),
                truckingMpService.getWeight_unit(), truckingMpService.getOrder_number(), truckingMpService.getPrice(),
                truckingMpService.getCurrency(), truckingMpService.getDuration(), truckingMpService.getSubVehicleType(),
                truckingMpService.getAddInsurance(), truckingMpService.getVolume(), truckingMpService.getVolumeUnit()));
    }


    @Override
    public void onGetPendingJobsFailed(String message) {
        CommonUtils.showToast(this, message);
    }

    /* private void getPendingJobsDelivery(TruckingService truckingService) {
         pendingJobsList = new ArrayList<>();
         pendingJobsList.add(new MyLogsAndOrderInfoTrucking(truckingService.getId(),
                 truckingService.getDate().substring(10), truckingService.getDate().substring(0, 9),
                 truckingService.getFromlocation(), truckingService.getTolocation(),
                 truckingService.getState(), array_delivery[Integer.valueOf(truckingService.getItemtype())],
                 truckingService.getOrderid(), truckingService.getOrdertype(),
                 truckingService.getCommodity_description(), truckingService.getPayment(),
                 truckingService.getWeight(), truckingService.getWeight_unit(),
                 truckingService.getOrder_number(), truckingService.getPrice(), truckingService.getCurrency(), truckingService.getDuration()));
     }
 */
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

                final ArrayList<MyLogsAndOrderInfoTrucking> filteredList = new ArrayList<>();

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
                    adapter = new TruckingPendingJobsAdapter(TruckingPendingJobActivity.this, filteredList);
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
