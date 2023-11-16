package com.ashtechlabs.teleporter.ui.pricing;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.OnItemClickListener;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_rate.UpdateRateVendorActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.update_rate.UpdateRateDriverActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.update_rate.UpdateRateWarehouseActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.update_rate.UpdateRateTruckingActivity;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceCourier;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceDriverList;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTrucking;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PricingWareHouse;
import com.ashtechlabs.teleporter.ui.pricing.prcing_driver.DriverRatesAdapter;
import com.ashtechlabs.teleporter.ui.pricing.prcing_driver.PricingDriverController;
import com.ashtechlabs.teleporter.ui.pricing.prcing_driver.PricingDriverControllerCallback;
import com.ashtechlabs.teleporter.ui.pricing.prcing_driver.PricingDriverService;
import com.ashtechlabs.teleporter.ui.pricing.prcing_trucking.PricingTruckService;
import com.ashtechlabs.teleporter.ui.pricing.prcing_trucking.PricingTruckingController;
import com.ashtechlabs.teleporter.ui.pricing.prcing_trucking.PricingTruckingControllerCallback;
import com.ashtechlabs.teleporter.ui.pricing.prcing_trucking.TruckingRatesAdapter;
import com.ashtechlabs.teleporter.ui.pricing.pricing_cargo.PricingVendorAdapter;
import com.ashtechlabs.teleporter.ui.pricing.pricing_cargo.PricingVendorController;
import com.ashtechlabs.teleporter.ui.pricing.pricing_cargo.PricingVendorControllerCallback;
import com.ashtechlabs.teleporter.ui.pricing.pricing_cargo.PricingVendorService;
import com.ashtechlabs.teleporter.ui.pricing.pricing_ware_house.PricingWareHouseController;
import com.ashtechlabs.teleporter.ui.pricing.pricing_ware_house.PricingWareHouseService;
import com.ashtechlabs.teleporter.ui.pricing.pricing_ware_house.PricingWarehouseControllerCallback;
import com.ashtechlabs.teleporter.ui.pricing.pricing_ware_house.WarehouseRatesAdapter;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;

public class PricingActivity extends BaseActivity implements PricingDriverControllerCallback, PricingWarehouseControllerCallback,
        PricingVendorControllerCallback, PricingTruckingControllerCallback, OnItemClickListener {

    public String[] array_delivery;
    //public String[] array_mover;
    public String[] array_vendor;
    int mode = GlobalUtils.MODE_COURIER;
    String token = "";
    RecyclerView listPricing;
    TextView tvHolder;
    EditText searchFrom, searchTo;
    LinearLayoutManager gridLayoutManager;

    DriverRatesAdapter adapter;
    TruckingRatesAdapter tadapter;
    WarehouseRatesAdapter adapter_warehouse;
    PricingVendorAdapter pricingVendorAdapter;

    PricingDriverController pricingController;
    PricingWareHouseController pricingWareHouseController;
    PricingVendorController pricingVendorController;
    PricingTruckingController pricingTruckingController;

    ArrayList<PriceCourier> priceDriver = new ArrayList<>();
    ArrayList<PriceTrucking> priceTrucking = new ArrayList<>();
    ArrayList<PricingWareHouse> priceWarehouse = new ArrayList<>();
    ArrayList<PriceVendor> priceVendor = new ArrayList<>();

    ArrayList<PriceCourier> filteredList;
    ArrayList<PricingWareHouse> filteredListStorage;
    ArrayList<PriceVendor> filteredListVendor;
    ArrayList<PriceTrucking> filteredListTruck;

    private String text = "";
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        String type = getIntent().getStringExtra("mode");
        token = getIntent().getStringExtra("token");
        mode = Integer.parseInt(type);

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

        setupToolbar();

        array_delivery = getResources().getStringArray(R.array.array_delivery);
        //array_mover = getResources().getStringArray(R.array.array_driver_service);
        array_vendor = getResources().getStringArray(R.array.array_commodity_copy);

        initUI();


    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (mode) {
            case GlobalUtils.MODE_COURIER:
                priceDriver.clear();
                pricingController = new PricingDriverService(this);
                pricingController.onPricingController(token);
                break;
            case GlobalUtils.MODE_STORAGE:
                priceWarehouse.clear();
                pricingWareHouseController = new PricingWareHouseService(this);
                pricingWareHouseController.onPricingController(token);
                searchTo.setVisibility(View.GONE);
                searchFrom.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchFrom.setHint("By location");
                break;
            case GlobalUtils.MODE_CARGO:
                priceVendor.clear();
                pricingVendorController = new PricingVendorService(this);
                pricingVendorController.onPricingController(token);
                break;
            case GlobalUtils.MODE_TRUCKING:
                priceTrucking.clear();
                pricingTruckingController = new PricingTruckService(this);
                pricingTruckingController.onPricingController(token);
                break;
        }
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Price");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    private void initUI() {
        searchFrom = (EditText) findViewById(R.id.searchFrom);
        searchFrom.setTag("from");
        addTextListener(searchFrom);
        searchTo = (EditText) findViewById(R.id.searchTo);
        searchTo.setTag("to");
        addTextListener(searchTo);
        tvHolder = (TextView) findViewById(R.id.tvHolder);
        listPricing = (RecyclerView) findViewById(R.id.listPricing);
        gridLayoutManager = new LinearLayoutManager(this);
        listPricing.setLayoutManager(gridLayoutManager);
    }


    private void addTextListener(final EditText editText) {

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

                switch (mode) {
                    case GlobalUtils.MODE_COURIER:
                        filteredList = new ArrayList<>();
                        if (priceDriver != null) {
                            for (int i = 0; i < priceDriver.size(); i++) {

                                if (editText.getTag().equals("from")) {
                                    text = priceDriver.get(i).getFromLocation().toLowerCase();
                                } else {
                                    text = priceDriver.get(i).getToLocation().toLowerCase();
                                }

                                if (text.contains(query)) {

                                    filteredList.add(priceDriver.get(i));
                                }
                            }

                            adapter = new DriverRatesAdapter(PricingActivity.this, filteredList);
                            adapter.setClickListener(PricingActivity.this);
                            listPricing.setAdapter(adapter);

                            if (!(filteredList.size() > 0)) {
                                tvHolder.setVisibility(View.VISIBLE);
                                tvHolder.setText("No rate card found!");
                            } else {
                                tvHolder.setVisibility(View.GONE);
                            }

                            if (count == 0) {
                                filteredList = null;
                            }

                        }


                        break;
                    case GlobalUtils.MODE_STORAGE:

                        filteredListStorage = new ArrayList<>();
                        if (priceDriver != null) {
                            for (int i = 0; i < priceWarehouse.size(); i++) {

                                if (editText.getTag().equals("from")) {
                                    text = priceWarehouse.get(i).getLocation().toLowerCase();
                                }

                                if (text.contains(query)) {

                                    filteredListStorage.add(priceWarehouse.get(i));
                                }
                            }

                            adapter_warehouse = new WarehouseRatesAdapter(PricingActivity.this, filteredListStorage);
                            adapter_warehouse.setClickListener(PricingActivity.this);
                            listPricing.setAdapter(adapter_warehouse);

                            if (!(filteredListStorage.size() > 0)) {
                                tvHolder.setVisibility(View.VISIBLE);
                                tvHolder.setText("No rate card found!");
                            } else {
                                tvHolder.setVisibility(View.GONE);
                            }

                            if (count == 0) {
                                filteredListStorage = null;
                            }
                        }


                        break;
                    case GlobalUtils.MODE_CARGO:

                        filteredListVendor = new ArrayList<>();
                        if (priceDriver != null) {
                            for (int i = 0; i < priceVendor.size(); i++) {

                                if (editText.getTag().equals("from")) {
                                    text = priceVendor.get(i).getFromLocation().toLowerCase();
                                } else {
                                    text = priceVendor.get(i).getToLocation().toLowerCase();
                                }

                                if (text.contains(query)) {

                                    filteredListVendor.add(priceVendor.get(i));
                                }
                            }

                            pricingVendorAdapter = new PricingVendorAdapter(PricingActivity.this, filteredListVendor);
                            pricingVendorAdapter.setClickListener(PricingActivity.this);
                            listPricing.setAdapter(pricingVendorAdapter);


                            if (!(filteredListVendor.size() > 0)) {
                                tvHolder.setVisibility(View.VISIBLE);
                                tvHolder.setText("No rate card found!");
                            } else {
                                tvHolder.setVisibility(View.GONE);
                            }

                            if (count == 0) {
                                filteredListVendor = null;
                            }

                        }


                        break;
                    case GlobalUtils.MODE_TRUCKING:

                        filteredListTruck = new ArrayList<>();
                        if (priceDriver != null) {
                            for (int i = 0; i < priceTrucking.size(); i++) {

                                if (editText.getTag().equals("from")) {
                                    text = priceTrucking.get(i).getFromLocation().toLowerCase();
                                } else {
                                    text = priceTrucking.get(i).getToLocation().toLowerCase();
                                }

                                if (text.contains(query)) {

                                    filteredListTruck.add(priceTrucking.get(i));
                                }
                            }

                            tadapter = new TruckingRatesAdapter(PricingActivity.this, filteredListTruck);
                            tadapter.setClickListener(PricingActivity.this);
                            listPricing.setAdapter(tadapter);

                            if (!(filteredListTruck.size() > 0)) {
                                tvHolder.setVisibility(View.VISIBLE);
                                tvHolder.setText("No rate card found!");
                            } else {
                                tvHolder.setVisibility(View.GONE);
                            }

                            if (count == 0) {
                                filteredListTruck = null;
                            }
                        }

                        break;
                }


            }
        });
    }


    @Override
    public void onGetPricingDetails(PriceDriverList reviews) {
        priceDriver = reviews.getPricingService();
        //  mpDriver = reviews.getPricingrate();
        // priceDriver.addAll(mpDriver);
//        Log.d("%%$$$%%%%",priceDriver.size()+"");
        if (priceDriver.size() > 0) {
            tvHolder.setVisibility(View.GONE);
//        switch (mode) {
//            case GlobalUtils.MODE_COURIER:
            Log.d("%%$$$###", priceDriver.size() + "");
            adapter = new DriverRatesAdapter(PricingActivity.this, priceDriver);
            adapter.setClickListener(this);
            listPricing.setAdapter(adapter);
            //  break;
        } else {
            tvHolder.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onGetPricingWareHouseDetails(ArrayList<PricingWareHouse> reviews) {
        priceWarehouse = reviews;
        if (priceWarehouse.size() > 0) {
            tvHolder.setVisibility(View.GONE);
//        switch (mode) {
//            case GlobalUtils.MODE_STORAGE:
            adapter_warehouse = new WarehouseRatesAdapter(PricingActivity.this, priceWarehouse);
            adapter_warehouse.setClickListener(this);
            listPricing.setAdapter(adapter_warehouse);
            //       break;
        } else {
            tvHolder.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onGetPricingFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }

    @Override
    public void onRateCardDeleted(String message) {
        if (mode == GlobalUtils.MODE_COURIER) {
            priceDriver.remove(position);
            adapter.notifyDataSetChanged();
        } else if (mode == GlobalUtils.MODE_TRUCKING) {
            priceTrucking.remove(position);
            tadapter.notifyDataSetChanged();
        } else if (mode == GlobalUtils.MODE_CARGO) {
            priceVendor.remove(position);
            pricingVendorAdapter.notifyDataSetChanged();
        } else {
            priceWarehouse.remove(position);
            adapter_warehouse.notifyDataSetChanged();
        }
        CommonUtils.showToast(getApplicationContext(), message);
    }

    @Override
    public void onGetPricingVendor(ArrayList<PriceVendor> reviews) {
        priceVendor = reviews;
        if (reviews.size() > 0) {
            tvHolder.setVisibility(View.GONE);
//        switch (mode) {
//            case GlobalUtils.MODE_CARGO:
            Log.d("CARGO##", priceVendor.size() + "....");
            pricingVendorAdapter = new PricingVendorAdapter(PricingActivity.this, reviews);
            pricingVendorAdapter.setClickListener(this);
            listPricing.setAdapter(pricingVendorAdapter);
            //   break;
        } else {
            tvHolder.setVisibility(View.VISIBLE);
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
    public void showLoadingDialog(boolean show) {
        if (show) showProgress();
        else dismissProgress();
    }

    @Override
    public void onGetTruckingPricingDetails(ArrayList<PriceTrucking> priceTruckings) {
        priceTrucking = priceTruckings;
        if (priceTruckings.size() > 0) {
            tvHolder.setVisibility(View.GONE);
//        switch (mode) {
//            case GlobalUtils.MODE_TRUCKING:
            Log.d("priceTruckings@@@@@", priceTruckings.size() + "....");
            tadapter = new TruckingRatesAdapter(this, priceTruckings);
            tadapter.setClickListener(this);
            listPricing.setAdapter(tadapter);
//                break;
        } else {
            tvHolder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(View view, int position, int which) {
        if (which == GlobalUtils.MODE_COURIER) {
            PriceCourier priceCourier;
            Intent i = new Intent(this, UpdateRateDriverActivity.class);
            if (filteredList != null) {
                priceCourier = filteredList.get(position);
            } else {
                priceCourier = priceDriver.get(position);
            }
            i.putExtra("price_driver", priceCourier);
            i.putExtra("priceDriverDuration", priceCourier.getDuration());
            i.putExtra("rate_card_type", Constants.RATE_CARD_UPDATE);
            startActivity(i);
        } else if (which == GlobalUtils.MODE_TRUCKING) {
            PriceTrucking priceTruckin;
            Intent i = new Intent(this, UpdateRateTruckingActivity.class);
            if (filteredListTruck != null) {
                priceTruckin = filteredListTruck.get(position);
            } else {
                priceTruckin = priceTrucking.get(position);
            }
            i.putExtra("price_trucking", priceTruckin);
            i.putExtra("rate_card_type", Constants.RATE_CARD_UPDATE);
            startActivity(i);
        } else if (which == GlobalUtils.MODE_CARGO) {
            PriceVendor priceVendo;
            Intent i = new Intent(this, UpdateRateVendorActivity.class);
            if (filteredListVendor != null) {
                priceVendo = filteredListVendor.get(position);
            } else {
                priceVendo = priceVendor.get(position);
            }
            i.putExtra("priceVendor", priceVendo);
            i.putExtra("rate_card_type", Constants.RATE_CARD_UPDATE);
            startActivity(i);
        } else {
            PricingWareHouse pricingWareHouse;
            Intent i = new Intent(this, UpdateRateWarehouseActivity.class);
            if (filteredListStorage != null) {
                pricingWareHouse = filteredListStorage.get(position);
            } else {
                pricingWareHouse = priceWarehouse.get(position);
            }
            i.putExtra("pricingWareHouse", pricingWareHouse);
            i.putExtra("rate_card_type", Constants.RATE_CARD_UPDATE);
            startActivity(i);
        }

        searchFrom.setText("");
        searchTo.setText("");
        searchFrom.requestFocus();
    }

    @Override
    public void onRateCardDelete(View view, int position, int which) {
        this.position = position;
        if (which == GlobalUtils.MODE_COURIER) {
            pricingController.onDeleteRateCard(token, priceDriver.get(position).getId());

        } else if (which == GlobalUtils.MODE_TRUCKING) {
            pricingTruckingController.onDeleteRateCard(token, priceTrucking.get(position).getId());

        } else if (which == GlobalUtils.MODE_CARGO) {
            pricingVendorController.onDeleteRateCard(token, priceVendor.get(position).getId());
        } else {
            pricingWareHouseController.onDeleteRateCard(token, priceWarehouse.get(position).getID());
        }
    }
}