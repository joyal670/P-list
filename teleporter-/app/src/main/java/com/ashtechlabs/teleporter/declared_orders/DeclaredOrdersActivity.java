package com.ashtechlabs.teleporter.declared_orders;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.OnRecyclerItemClickListener;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.update_rate.UpdateRateDriverActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_rate.UpdateRateVendorActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.DeclaredOrders;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.update_rate.UpdateRateWarehouseActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.update_rate.UpdateRateTruckingActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

public class DeclaredOrdersActivity extends BaseActivity implements DeclaredOrdersContract.DeclaredOrdersView,
        OnRecyclerItemClickListener, View.OnClickListener{

    private ActionBar actionBar;
    private TextView tvHolder;
    private RecyclerView rvDeclaredOrders;
    private DeclaredOrdersAdapter mDeclaredOrdersAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DeclaredOrders> declaredOrderses;
    private int mode;
    private String token;

//    private FloatingActionButton fabCall, fabChat;
//    private FloatingActionMenu fabMenu;
    private DeclaredOrdersContract.DeclaredOrdersPresenter declaredOrderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declared_orders);

        setUpToolbar();

        mLayoutManager = new LinearLayoutManager(this);

        Intent intent = getIntent();
        if (intent != null) {
            mode = Integer.parseInt(intent.getStringExtra("mode"));
            token = intent.getStringExtra("token");

            if(mode == 0){
                token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER,"");
            }else if(mode == 1){
                token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING,"");
                //token = "5a8a908aca272";
            }else if(mode == 2){
                token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO,"");
            }else{
                token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE,"");
            }
        }

        initViews();

        declaredOrderPresenter = new DeclaredOrdersPresenterImpl(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        declaredOrderPresenter.getDeclaredOrders(mode,token);
    }

    private void initViews() {
        tvHolder = (TextView) findViewById(R.id.tvHolder);
        rvDeclaredOrders = (RecyclerView) findViewById(R.id.rvDeclaredOrders);
        rvDeclaredOrders.setHasFixedSize(true);
        rvDeclaredOrders.setLayoutManager(mLayoutManager);
        //rvDeclaredOrders.addItemDecoration(new VerticalSpaceItemDecoration(16));

//        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
//        fabCall = (FloatingActionButton) findViewById(R.id.fabCall);
//        fabCall.setOnClickListener(this);
//        fabChat = (FloatingActionButton) findViewById(R.id.fabChat);
//        fabChat.setOnClickListener(this);

}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.fabCall:
//                if (checkCallPermission()) {
//                    launchCallFragment();
//                } else {
//                    CommonUtils.showToast(this, "Allow Call permission from Settings, to make a call");
//                }
//                break;
//
//            case R.id.fabChat:
//                startChat();
//                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_support:
                startSupport();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSupport() {

//        if(fabMenu.isMenuButtonHidden()) {
//            fabMenu.showMenuButton(true);
//            fabMenu.open(true);
//        }else{
//            fabMenu.open(true);
//        }
    }


    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onLoadingItemFailed(String message) {
        CommonUtils.showToast(getApplicationContext(),message);
        tvHolder.setVisibility(View.VISIBLE);
        tvHolder.setText(message);
    }

    @Override
    public void setDeclaredOrders(ArrayList<DeclaredOrders> declaredOrders) {
        this.declaredOrderses = declaredOrders;
        if (declaredOrders != null && declaredOrders.size() > 0) {
            tvHolder.setVisibility(View.GONE);
            mDeclaredOrdersAdapter = new DeclaredOrdersAdapter(this, declaredOrderses);
            rvDeclaredOrders.setAdapter(mDeclaredOrdersAdapter);
            mDeclaredOrdersAdapter.setClickListener(this);
        } else {
            tvHolder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view, int position, int serviceType) {

        if (serviceType == Constants.SERVICE_CARGO) {
            Intent intent = new Intent(this, UpdateRateVendorActivity.class);
            intent.putExtra("rate_card_type", Constants.RATE_CARD_ADD);

            intent.putExtra("id", declaredOrderses.get(position).getId());
            Log.d("IDDDDDDDDDD", " "+declaredOrderses.get(position).getId());
            intent.putExtra("commodity_type", declaredOrderses.get(position).getCommodityType());
            intent.putExtra("from_location", declaredOrderses.get(position).getFromLocation());
            intent.putExtra("to_location", declaredOrderses.get(position).getToLocation());
            intent.putExtra("service_mode", declaredOrderses.get(position).getServiceMode());
            intent.putExtra("hazardous", declaredOrderses.get(position).getHazardous());
            intent.putExtra("insurance", declaredOrderses.get(position).getAddInsurance());
            intent.putExtra("additional_info", declaredOrderses.get(position).getAdditionalInfo());
            intent.putExtra("perishable_det", declaredOrderses.get(position).getPerishableData());
            //intent.putExtra("shipment", declaredOrderses.get(position).get());
            startActivity(intent);

        } else if (serviceType == Constants.SERVICE_STORAGE){
            Intent intent = new Intent(this, UpdateRateWarehouseActivity.class);
            intent.putExtra("rate_card_type", Constants.RATE_CARD_ADD);

            intent.putExtra("id", declaredOrderses.get(position).getId());
            Log.d("IDDDDDDDDDD", " "+declaredOrderses.get(position).getId());
            intent.putExtra("location", declaredOrderses.get(position).getLocation());
            intent.putExtra("from_date", declaredOrderses.get(position).getFromDate());
            intent.putExtra("to_date", declaredOrderses.get(position).getToDate());
            intent.putExtra("additional_info", declaredOrderses.get(position).getAdditionalInfo());
            intent.putExtra("perishable_data", declaredOrderses.get(position).getPerishableData());
            //intent.putExtra("space", declaredOrderses.get(position).getCbm());
            //intent.putExtra("item_type", declaredOrderses.get(position).getItemType());
            intent.putExtra("commodity_type", declaredOrderses.get(position).getCommodityType());

            startActivity(intent);
        }else if (serviceType == Constants.SERVICE_COURIER){
            Intent intent = new Intent(this, UpdateRateDriverActivity.class);
            intent.putExtra("rate_card_type", Constants.RATE_CARD_ADD);

            intent.putExtra("id", declaredOrderses.get(position).getId());
            Log.d("IDDDDDDDDDD", " "+declaredOrderses.get(position).getId());
            intent.putExtra("from_location", declaredOrderses.get(position).getFromLocation());
            intent.putExtra("to_location", declaredOrderses.get(position).getToLocation());
            intent.putExtra("service_type", declaredOrderses.get(position).getServiceType());
            intent.putExtra("item_type", declaredOrderses.get(position).getItemType());


            startActivity(intent);
        }else {
            Intent intent = new Intent(this, UpdateRateTruckingActivity.class);
            intent.putExtra("rate_card_type", Constants.RATE_CARD_ADD);
            intent.putExtra("id", declaredOrderses.get(position).getId());
            Log.d("IDDDDDDDDDD", " "+declaredOrderses.get(position).getId());
            intent.putExtra("from_location", declaredOrderses.get(position).getFromLocation());
            intent.putExtra("to_location", declaredOrderses.get(position).getToLocation());
            intent.putExtra("service_type", declaredOrderses.get(position).getServiceType());
            intent.putExtra("delivery_type", declaredOrderses.get(position).getItemType());
            intent.putExtra("vehicle_type", declaredOrderses.get(position).getVehicleType());
            intent.putExtra("sub_vehicle_type", declaredOrderses.get(position).getSubVehicleType());

            startActivity(intent);
        }

        Log.d("ORDER DETAIL", "ID " + declaredOrderses.get(position).getId() + " , " + "SERVICE TYPE " + declaredOrderses.get(position).getServiceType());

    }
}
