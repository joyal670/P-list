package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.order_info_detail;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.CargoLoadAdapter;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.CustomerDetailDialogListener;
import com.ashtechlabs.teleporter.dialog_fragments.CustomerDetailDialogFragment;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.ICustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.IJobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateControllerCallback;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderDetailVendorActivity extends BaseActivity implements View.OnClickListener,
        CustomerDetailControllerCallback, JobStateControllerCallback, CustomerDetailDialogListener, IOrderDetailVendorActivityControllerCallback {
    ArrayList<String> arrayUnits, arrayCommodity, arrayCurrency, array_payment, array_service;
    Button btnAccept, btnReject;
    VendorList myLogs;
    int pos;
    String Dphone, Dpincode, Daddress, Dlandmark, Dcity, Dstate, Dstatus, Dname;
    String Pphone, Ppincode, Paddress, Plandmark, Pcity, Pstate, Pstatus, Pname;
    private IOrderDetailVendorActivityController mController;
    private TextView tvDate, tvCargoType, tvCurrency, tvTime, tvUnit, tvCommodity, tvDelName, tvDelMobile, tvDelAddress, tvPickName, tvPickMobile, tvPickAddress, tvPricing, tvDuration;
    private TextView tvFromLocation, tvToLocation, tvPayment, tv_commodity_description;
    private TextView etValue, etCommodityDetail, etWeight, tvservice;
    private CheckBox cbInsurance;
    private TextView tvShipmentType;

    private LinearLayout containerOther;
    private ICustomerDetailController ICustomerDetailController;
    private IJobStateController IJobStateController;
    private int additional_info = -1;
    private int perishable_det = 0;
    private TextView tvUnitVolume, etVolume, tvInsuranceAmount;

    private RecyclerView rvLoadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_vendor);
        setupToolbar();

        arrayUnits = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_unit)));
        arrayCommodity = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_commodity_copy)));
        arrayCurrency = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_currency)));
        array_payment = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_payment)));
        array_service = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_service)));

        Bundle extra = getIntent().getExtras();
        myLogs = extra.getParcelable("vendor_order_info");
        pos = extra.getInt("pos");


        tvShipmentType = (TextView) findViewById(R.id.tvShipmentType);
        tvFromLocation = (TextView) findViewById(R.id.tvFromLocation);
        tvToLocation = (TextView) findViewById(R.id.tvToLocation);
        containerOther = (LinearLayout) findViewById(R.id.containerOther);
        tvPricing = (TextView) findViewById(R.id.tvPricing);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvservice = (TextView) findViewById(R.id.tvservice);

        tvDelName = (TextView) findViewById(R.id.tvDelName);
        tvDelMobile = (TextView) findViewById(R.id.tvDelMobile);
        tvDelAddress = (TextView) findViewById(R.id.tvDelAddress);
        tvPickName = (TextView) findViewById(R.id.tvPickName);
        tvPickMobile = (TextView) findViewById(R.id.tvPickMobile);
        tvPickAddress = (TextView) findViewById(R.id.tvPickAddress);


        tvCargoType = (TextView) findViewById(R.id.tvCargoType);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        tvCommodity = (TextView) findViewById(R.id.tvCommodity);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        tvPayment = (TextView) findViewById(R.id.tvPayment);
        tv_commodity_description = (TextView) findViewById(R.id.tv_commodity_description);


        etValue = (TextView) findViewById(R.id.etValue);
        etCommodityDetail = (TextView) findViewById(R.id.etCommodityDetail);
        etWeight = (TextView) findViewById(R.id.etWeight);
        etVolume = (TextView) findViewById(R.id.etVolume);
        tvUnitVolume = (TextView) findViewById(R.id.tvUnitVolume);
        tvInsuranceAmount = (TextView) findViewById(R.id.tvInsuranceAmount);
//        etWidth = (TextView) findViewById(R.id.etWidth);
//        etHeight = (TextView) findViewById(R.id.etHeight);
//        etBreadth = (TextView) findViewById(R.id.etBreadth);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnReject = (Button) findViewById(R.id.btnReject);


        cbInsurance = (CheckBox) findViewById(R.id.cbInsurance);


        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);

        ICustomerDetailController = new CustomerDetailController(this);
        IJobStateController = new JobStateController(this);
        mController = new OrderDetailVendorActivityController(this);
        mController.onDeliveryAndPickupAddress(myLogs.getId());

        tvPricing.setText(myLogs.getPrice() + " " + arrayCurrency.get(myLogs.getCurrency()).substring(0, 3));
        tvFromLocation.setText(myLogs.getFromLocation());
        tvToLocation.setText(myLogs.getToLocation());
        tvCommodity.setText(arrayCommodity.get(Integer.parseInt(myLogs.getCommodityType())));
        etValue.setText(String.valueOf(myLogs.getCommodity_value()));
        tvUnit.setText("KG");
        tvUnitVolume.setText("CBM");

        double weight = myLogs.getWeight();

        if (myLogs.getWeight_unit().equals("Pounds/cm") || (myLogs.getWeight_unit().equals("Pounds/mm"))) {
            weight = weight * 2.2;
        }
        etWeight.setText(String.valueOf(Math.round(weight)));
        etVolume.setText(String.valueOf(myLogs.getVolume()));


        String duration = myLogs.getDuration();
        if (Integer.parseInt(duration) > 24) {
            tvDuration.setText(myLogs.getDuration());
            tvDuration.setText(getDaysAndHours(duration));
        } else {
            tvDuration.setText(String.format("%s Hrs", duration));
        }


        additional_info = Integer.parseInt(myLogs.getAdditionalInfo());
        if (additional_info == 0) {
            tvCargoType.setText("General");
        } else if (additional_info == 1) {
            tvCargoType.setText("Hazardous");
        } else if (additional_info == 2) {
            perishable_det = Integer.parseInt(myLogs.getPerishableData());
            if (perishable_det == 1) {
                tvCargoType.setText("Perishable - Cool");
            }
            if (perishable_det == 2) {
                tvCargoType.setText("Perishable - Cold");
            }
            if (perishable_det == 3) {
                tvCargoType.setText("Perishable - Frozen");
            }
        }

        if (myLogs.getAddInsurance() == 1) {
            cbInsurance.setChecked(true);
            tvInsuranceAmount.setVisibility(View.VISIBLE);
            tvInsuranceAmount.setText(myLogs.getInsuranceAmount() + " " + arrayCurrency.get(myLogs.getCurrency()).substring(0, 3));
        } else {
            cbInsurance.setChecked(false);
            tvInsuranceAmount.setVisibility(View.GONE);
        }

        if (myLogs.getShipment() == 0) {
            tvShipmentType.setText("Single");
        } else {
            tvShipmentType.setText("Regular");
        }

        if (myLogs.getDate().length() > 11) {
            tvTime.setText(myLogs.getDate().substring(11));
        }
        tvDate.setText(myLogs.getDate().substring(0, 10));

        tvCurrency.setText(arrayCurrency.get(myLogs.getCurrency()));
        tvservice.setText(array_service.get(Integer.parseInt(myLogs.getTransport())));
        tvPayment.setText(array_payment.get(Integer.parseInt(myLogs.getPayment())));

        if (Integer.parseInt(myLogs.getCommodityType()) + 1 == (arrayCommodity.size())) {
            containerOther.setVisibility(View.VISIBLE);
            etCommodityDetail.setText(myLogs.getCommodity_description());
        }

        rvLoadDetails = (RecyclerView) findViewById(R.id.rvLoadDetails);
        rvLoadDetails.setLayoutManager(new LinearLayoutManager(this));
        rvLoadDetails.setAdapter(new CargoLoadAdapter(this, myLogs.getCargoLoads()));

    }

    private String getDaysAndHours(String duration) {
        int day = Integer.parseInt(duration);
        StringBuilder stringBuilder = new StringBuilder();
        int days = day / 24;
        stringBuilder.append(days).append("Days");
        stringBuilder.append(" ");
        int hours = day % 24;
        if (hours == 0) {
            stringBuilder.append(00).append("Hrs");
        } else {
            stringBuilder.append(hours).append("Hrs");
        }
        return stringBuilder.toString();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Order Detail");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAccept:
                ICustomerDetailController.onCustomerController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""), myLogs.getOrderid(), myLogs.getOrdertype());

                break;

            case R.id.btnReject:
                IJobStateController.onJobState(myLogs.getId(), GlobalUtils.JOB_REJECTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));


        }
    }

    @Override
    public void onAcceptTheOrder() {
        IJobStateController.onJobState(myLogs.getId(), GlobalUtils.JOB_ACCEPTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));

    }

    @Override
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }

    @Override
    public void onGetJobStateSuccess(String message) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        //  onOrderInfoAdapterCallback.showMessageAlert("Successful", message);
        myLogs.setState(GlobalUtils.JOB_ACCEPTED);
        Intent i = new Intent(this, CargoDashBoardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);


    }

    @Override
    public void onGetJobStateFailed(String message) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetCustomerDetails(String name, String phone) {
        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.setListner(this);
        customerDetailDialogFragment.show(getSupportFragmentManager(), "customer_detail");
    }

    @Override
    public void onGetCustomerDetailsFailed(String message) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog(boolean show) {

    }

    @Override
    public void getDeliveryAndPickupAddress(String Address) {

        JSONObject jObj = null;
        try {
            jObj = new JSONObject(Address);
            Log.d("##########ADDD", Address);
            String code = jObj.getString("code");
            if (code.equals("success")) {
                JSONObject jsonObject = jObj.getJSONObject("data");
                JSONArray delivery = jsonObject.getJSONArray("delivery_address");
                JSONObject jsonObject1 = delivery.getJSONObject(0);
                Dname = jsonObject1.getString("name");
                Dphone = jsonObject1.getString("phone");
                Dpincode = jsonObject1.getString("pincode");
                Daddress = jsonObject1.getString("address");
                Dlandmark = jsonObject1.getString("landmark");
                Dcity = jsonObject1.getString("city");
                Dstate = jsonObject1.getString("state");
                Dstatus = jsonObject1.getString("status");


                JSONArray pickup = jsonObject.getJSONArray("pickup_address");
                JSONObject jsonObject2 = pickup.getJSONObject(0);
                Pname = jsonObject2.getString("name");
                Pphone = jsonObject2.getString("phone");
                Ppincode = jsonObject2.getString("pincode");
                Paddress = jsonObject2.getString("address");
                Plandmark = jsonObject2.getString("landmark");
                Pcity = jsonObject2.getString("city");
                Pstate = jsonObject2.getString("state");
                Pstatus = jsonObject2.getString("status");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvDelName.setText(Dname);
        tvDelMobile.setText(Dphone);
        tvDelAddress.setText(Daddress + "," + Dlandmark + "," + Dcity + "," + Dpincode + "\n" + Dstate);
        tvPickName.setText(Pname);
        tvPickMobile.setText(Pphone);
        tvPickAddress.setText(Paddress + "," + Plandmark + "," + Pcity + "," + Ppincode + "\n" + Pstate);
    }

    @Override
    public void getDeliveryAndPickupAddressFailed(String Failed) {

    }
}
