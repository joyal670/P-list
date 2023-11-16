package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.order_info_detail;

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
import com.ashtechlabs.teleporter.adapters.TruckingLoadAdapter;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.CustomerDetailDialogListener;
import com.ashtechlabs.teleporter.dialog_fragments.CustomerDetailDialogFragment;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.ICustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.IJobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.TruckingDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderDetailTruckingActivity extends BaseActivity implements View.OnClickListener,
        CustomerDetailControllerCallback, JobStateControllerCallback, CustomerDetailDialogListener,
        IOrderDetailTruckingActivityControllerCallback {

    TextView tvFromLocation, tvToLocation, etWeight, tvDate, tvTime, tvUnit, tvName, tvMobile, tvAddress,
            tvPayment, tvDelName, tvDelMobile, tvDelAddress, tvPickName, tvPickMobile, tvPickAddress, tvPricing, tvDuration;
    Button btnAccept, btnReject;
    int pos;
    ArrayList<String> array_payment, array_currency;
    TruckingMpService myLogs;
    String Dphone, Dpincode, Daddress, Dlandmark, Dcity, Dstate, Dstatus, Dname;
    String Pphone, Ppincode, Paddress, Plandmark, Pcity, Pstate, Pstatus, Pname;
    TextView etCommodityDetail;
    private CheckBox cbInsurance;
    private ICustomerDetailController ICustomerDetailController;
    private IJobStateController IJobStateController;
    private IOrderDetailTruckingActivityController iOrderDetailTruckingActivityController;
    private LinearLayout containerOther;
    private TextView tvUnitVolume, etVolume, tvInsuranceAmount;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_trucking);

        setupToolbar();
        array_payment = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_payment)));
        array_currency = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_currency)));
        Bundle extra = getIntent().getExtras();
        myLogs = extra.getParcelable("orderinfolist");
        pos = extra.getInt("pos");

        cbInsurance = (CheckBox) findViewById(R.id.cbInsurance);
        containerOther = (LinearLayout) findViewById(R.id.containerOther);
        tvDelName = (TextView) findViewById(R.id.tvDelName);
        tvDelMobile = (TextView) findViewById(R.id.tvDelMobile);
        tvDelAddress = (TextView) findViewById(R.id.tvDelAddress);
        tvPickName = (TextView) findViewById(R.id.tvPickName);
        tvPickMobile = (TextView) findViewById(R.id.tvPickMobile);
        tvPickAddress = (TextView) findViewById(R.id.tvPickAddress);
        tvPricing = (TextView) findViewById(R.id.tvPricing);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        etCommodityDetail = (TextView) findViewById(R.id.etCommodityDetail);

        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnReject = (Button) findViewById(R.id.btnReject);
        tvFromLocation = (TextView) findViewById(R.id.tvFromLocation);
        tvToLocation = (TextView) findViewById(R.id.tvToLocation);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        etWeight = (TextView) findViewById(R.id.etWeight);
        etVolume = (TextView) findViewById(R.id.etVolume);
        tvUnitVolume = (TextView) findViewById(R.id.tvUnitVolume);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvInsuranceAmount = (TextView) findViewById(R.id.tvInsuranceAmount);

        tvPayment = (TextView) findViewById(R.id.tvPayment);


        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        ICustomerDetailController = new CustomerDetailController(this);
        IJobStateController = new JobStateController(this);
        iOrderDetailTruckingActivityController = new OrderDetailTruckingActivityController(this);
        iOrderDetailTruckingActivityController.onDeliveryAndPickupAddressDriver(myLogs.getId());

        tvFromLocation.setText(myLogs.getFromlocation());
        tvToLocation.setText(myLogs.getTolocation());
        // Log.d("####",myLogs.getWeight());

        double weight = myLogs.getWeight();

        if (myLogs.getWeight_unit().equals("Pounds/cm") || (myLogs.getWeight_unit().equals("Pounds/mm"))) {
            weight = weight * 2.2;
        }

        etWeight.setText(String.valueOf(weight));
        etVolume.setText(String.valueOf(myLogs.getVolume()));
        tvUnit.setText("KG");
        tvUnitVolume.setText("CBM");
        tvDate.setText(myLogs.getDate().substring(0, 10));
        if (myLogs.getDate().length() > 11) {
            tvTime.setText(myLogs.getDate().substring(11));
        }
        tvPayment.setText(array_payment.get(Integer.parseInt(myLogs.getPayment())));
        tvPricing.setText(myLogs.getPrice() + " " + array_currency.get(myLogs.getCurrency()).substring(0, 3));
//        if (myLogs.getItemtype().equals("Others")) {
//            containerOther.setVisibility(View.VISIBLE);
//            etCommodityDetail.setText(myLogs.getCommodity_description());
//        }
       // Log.d("myLogs.getItemtype()", myLogs.getItemtype());
        String duration = myLogs.getDuration();
        if (Integer.parseInt(duration) > 24) {
            tvDuration.setText(myLogs.getDuration());
            tvDuration.setText(getDaysAndHours(duration));
        } else {
            tvDuration.setText(String.format("%s Hrs", duration));
        }

        if (myLogs.getAddInsurance() == 1) {
            cbInsurance.setChecked(true);
            tvInsuranceAmount.setVisibility(View.VISIBLE);
            tvInsuranceAmount.setText(myLogs.getInsuranceAmount()+" "+array_currency.get(myLogs.getCurrency()).substring(0, 3));

        } else {
            tvInsuranceAmount.setVisibility(View.GONE);
            cbInsurance.setChecked(false);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rvLoadDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TruckingLoadAdapter(this, myLogs.getTruckingLoads()));

        Log.d("###price", myLogs.getPrice() + "" + myLogs.getDuration());

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
                ICustomerDetailController.onCustomerController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), myLogs.getOrderid(), myLogs.getOrdertype());
                break;

            case R.id.btnReject:
                IJobStateController.onJobState(myLogs.getId(), GlobalUtils.JOB_REJECTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));

        }
    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    public void dismissLoadingIndicator() {

    }

    @Override
    public void onGetCustomerDetails(String name, String phone) {
        CustomerDetailDialogFragment customerDetailDialogFragment = CustomerDetailDialogFragment.newInstance(name, phone);
        customerDetailDialogFragment.setListner(this);
        customerDetailDialogFragment.show(getSupportFragmentManager(), "customer_detail");
    }

    @Override
    public void onGetCustomerDetailsFailed(String message) {
        //  onOrderInfoAdapterCallback.showMessageAlert("Failed", message);
        Toast.makeText(this, "Failed....", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetJobStateSuccess(String message) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, TruckingDashBoardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    @Override
    public void onGetJobStateFailed(String message) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();

        // onOrderInfoAdapterCallback.showMessageAlert("Failed", message);
    }

    @Override
    public void onAcceptTheOrder() {
        IJobStateController.onJobState(myLogs.getId(), GlobalUtils.JOB_ACCEPTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));

    }

    @Override
    public void showLoadingDialog(boolean show) {

    }

    @Override
    public void getDeliveryAndPickupAddressDriver(String Address) {
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
    public void getDeliveryAndPickupAddressDriverFailed(String Failed) {

    }
}
