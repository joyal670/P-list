package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pending_jobs;

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
import com.ashtechlabs.teleporter.adapters.WareHousePendingJobAdapter;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.PendingJob;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class WarehousePendingJobActivity extends BaseActivity implements WarehousePendingJobsControllerCallback {
    private WareHousePendingJobAdapter adapter;
    private IWarehousePendingJobsController driver_pendingjobsController;
    private RecyclerView recyclerView;
    private EditText editText;
    private TextView tvHolder;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<WareHouseList> deliveryService;
    private String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_jobs);
        serUpToolbar();

        initView();

        driver_pendingjobsController = new WarehousePendingJobsController(this);
        driver_pendingjobsController.onDriverDetails(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_pending);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        tvHolder = (TextView) findViewById(R.id.tvHolder);
        editText = (EditText) findViewById(R.id.search);

    }

    private void serUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Pending Jobs");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
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
    public void onGetPendingJobs(PendingJob deliveryServices) {
        deliveryService = deliveryServices.getDeliveryService();

        adapter = new WareHousePendingJobAdapter(WarehousePendingJobActivity.this, deliveryService);
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

    @Override
    public void onGetPendingJobsFailed(String message) {
        CommonUtils.showToast(this, message);
    }


    private void addTextListener() {

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final ArrayList<WareHouseList> filteredList = new ArrayList<>();

                if (deliveryService != null) {

                    for (int i = 0; i < deliveryService.size(); i++) {

                        if (deliveryService.get(i).getOrder_number().equals("")) {
                            text = deliveryService.get(i).getOrderid().toLowerCase();
                        } else {
                            text = deliveryService.get(i).getOrder_number().toLowerCase();
                        }

                        if (text.contains(query)) {

                            filteredList.add(deliveryService.get(i));
                        }
                    }
                    adapter = new WareHousePendingJobAdapter(WarehousePendingJobActivity.this, filteredList);
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
