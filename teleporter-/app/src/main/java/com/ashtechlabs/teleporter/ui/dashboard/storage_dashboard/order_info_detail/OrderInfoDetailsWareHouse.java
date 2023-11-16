package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.order_info_detail;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.common_interfaces.CustomerDetailDialogListener;
import com.ashtechlabs.teleporter.dialog_fragments.CustomerDetailDialogFragment;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.CustomerDetailControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller.ICustomerDetailController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.IJobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateController;
import com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller.JobStateControllerCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OrderInfoDetailsWareHouse extends BaseActivity implements View.OnClickListener,
        CustomerDetailControllerCallback, JobStateControllerCallback, CustomerDetailDialogListener, IOrderDetailWareHouseActivityControllerCallback {
    WareHouseList myLogs;
    int pos;
    Button btnAccept, btnReject;
    SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private TextView tvFromDate, tvStorageType, tvToDate, tvType, tvCommodity, tvCurrency, tvInsuranceAmount;
    private TextView etSpaceRequired, etValue, etCommodityDetail;
    private TextView tvLocation, tvPayment, tvPricing, tvDuration;
    private LinearLayout containerOther;
    private CheckBox cbInsurance;
    private ArrayList<String> arrayType;
    private ArrayList<String> arrayCommodity;
    private ArrayList<String> arrayCurrency;
    private ArrayList<String> array_payment;
    private ICustomerDetailController ICustomerDetailController;
    private IJobStateController IJobStateController;
    private int additional_info = -1;
    private int perishable_det = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_order_info_details);
        setupToolbar();

        arrayType = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_type)));
        arrayCommodity = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_commodity_copy)));
        arrayCurrency = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_currency)));
        array_payment = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_payment)));


        Bundle extra = getIntent().getExtras();
        myLogs = extra.getParcelable("orderinfolist");
        pos = extra.getInt("pos");

        cbInsurance = (CheckBox) findViewById(R.id.cbInsurance);
        tvPricing = (TextView) findViewById(R.id.tvPricing);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvStorageType = (TextView) findViewById(R.id.tvStorageType);
        tvFromDate = (TextView) findViewById(R.id.tvFromDate);
        tvToDate = (TextView) findViewById(R.id.tvToDate);
        tvType = (TextView) findViewById(R.id.tvType);
        tvCommodity = (TextView) findViewById(R.id.tvCommodity);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        containerOther = (LinearLayout) findViewById(R.id.containerOther);
        tvInsuranceAmount = (TextView) findViewById(R.id.tvInsuranceAmount);

        etSpaceRequired = (TextView) findViewById(R.id.etSpaceRequired);
        etValue = (TextView) findViewById(R.id.etValue);
        etCommodityDetail = (TextView) findViewById(R.id.etCommodityDetail);
        tvPayment = (TextView) findViewById(R.id.tvPayment);
        tvPricing = (TextView) findViewById(R.id.tvPricing);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        ICustomerDetailController = new CustomerDetailController(this);
        IJobStateController = new JobStateController(this);
        tvLocation.setText(myLogs.getLocation());
        tvFromDate.setText(myLogs.getFromDate());
        tvToDate.setText(myLogs.getToDate());
        etSpaceRequired.setText(myLogs.getCBM() + " CBM");
        tvType.setText(arrayType.get(myLogs.getType_of_good()));
        tvCommodity.setText(arrayCommodity.get(myLogs.getCommodityType()));
        etValue.setText(String.valueOf(myLogs.getCommodity_value()));
        tvCurrency.setText(arrayCurrency.get(myLogs.getCurrency()));
        tvPayment.setText(array_payment.get(myLogs.getPayment()));
        tvPricing.setText(myLogs.getPrice() + " " + arrayCurrency.get(myLogs.getCurrency()).substring(0, 3));
        tvDuration.setText(getNoOfDays(myLogs.getFromDate(), myLogs.getToDate()) + " Days");

        if (myLogs.getCommodityType() + 1 == (arrayCommodity.size())) {
            containerOther.setVisibility(View.VISIBLE);
            etCommodityDetail.setText(myLogs.getCommodity_description());
        }

        additional_info = Integer.parseInt(myLogs.getAdditionalInfo());
        if (additional_info == 0) {
            tvStorageType.setText("General");
        } else if (additional_info == 1) {
            tvStorageType.setText("Hazardous");
        } else if (additional_info == 2) {
            perishable_det = Integer.parseInt(myLogs.getPerishableData());
            if (perishable_det == 1) {
                tvStorageType.setText("Perishable - Cool");
            }
            if (perishable_det == 2) {
                tvStorageType.setText("Perishable - Cold");
            }
            if (perishable_det == 3) {
                tvStorageType.setText("Perishable - Frozen");
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

        //     mController.onDeliveryAndPickupAddressWareHouse(myLogs.getId());

    }

    private String getNoOfDays(String fromDate, String toDate) {

        try {
            Date date1 = myFormat.parse(fromDate);
            Date date2 = myFormat.parse(toDate);
            long diff = date2.getTime() - date1.getTime();
            return String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
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
                ICustomerDetailController.onCustomerController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""), myLogs.getOrderid(), myLogs.getOrdertype());


                break;

            case R.id.btnReject:
                IJobStateController.onJobState(myLogs.getId(), GlobalUtils.JOB_REJECTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));


        }
    }

    @Override
    public void onAcceptTheOrder() {
        IJobStateController.onJobState(myLogs.getId(), GlobalUtils.JOB_ACCEPTED, GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));

    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    public void dismissLoadingIndicator() {

    }

    @Override
    public void onGetJobStateSuccess(String message) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        //  onOrderInfoAdapterCallback.showMessageAlert("Successful", message);
        Intent i = new Intent(this, WareHouseDashBoardActivity.class);
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
    public void getDeliveryAndPickupAddressWareHouse(String Address) {
        /*JSONObject jObj = null;
        try {
            jObj = new JSONObject(Address);
            Log.d("##########ADDD",Address);
            String code= jObj.getString("code");
            if (code.equals("success")) {
                JSONObject jsonObject = jObj.getJSONObject("data");
                JSONArray delivery = jsonObject.getJSONArray("delivery_address");
                JSONObject jsonObject1 = delivery.getJSONObject(0);
                Dname     = jsonObject1.getString("name");
                Dphone    = jsonObject1.getString("phone");
                Dpincode  = jsonObject1.getString("pincode");
                Daddress  = jsonObject1.getString("address");
                Dlandmark = jsonObject1.getString("landmark");
                Dcity     = jsonObject1.getString("city");
                Dstate    = jsonObject1.getString("state");
                Dstatus   = jsonObject1.getString("status");


                JSONArray pickup = jsonObject.getJSONArray("pickup_address");
                JSONObject jsonObject2 = pickup.getJSONObject(0);
                Pname     = jsonObject2.getString("name");
                Pphone    = jsonObject2.getString("phone");
                Ppincode  = jsonObject2.getString("pincode");
                Paddress  = jsonObject2.getString("address");
                Plandmark = jsonObject2.getString("landmark");
                Pcity     = jsonObject2.getString("city");
                Pstate    = jsonObject2.getString("state");
                Pstatus   = jsonObject2.getString("status");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvDelName.setText(Dname);
        tvDelMobile.setText(Dphone);
        tvDelAddress.setText(Daddress+","+Dlandmark+","+Dcity+","+Dpincode+"\n"+Dstate);
        tvPickName.setText(Pname);
        tvPickMobile.setText(Pphone);
        tvPickAddress.setText(Paddress+","+Plandmark+","+Pcity+","+Ppincode+"\n"+Pstate);
*/
    }

    @Override
    public void getDeliveryAndPickupAddressFailedWareHouse(String Failed) {

    }
}
