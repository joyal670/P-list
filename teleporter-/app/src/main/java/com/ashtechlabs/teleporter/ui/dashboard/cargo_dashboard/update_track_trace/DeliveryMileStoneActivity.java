package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_track_trace;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DeliveryMileStoneActivity extends BaseActivity implements IDeliveryMileStoneActivityControllerCallback, CompoundButton.OnCheckedChangeListener {


    RadioGroup rg_air, rg_sea;
    RadioButton rb_order, rb_pickup1;
    RadioButton rb_cfs, rb_discharge, rb_arrival, rb_dispatched, rb_freight, rb_delivered, rb_other_air, rb_other_sea;
    RadioButton rb_Sorder, rb_pickup, rb_Sulddischarge, rb_Sarrival, rb_Sdispatched, rb_arrivalULD, rb_docllect, rb_customer;
    LinearLayout Lay_sea, Lay_air;
    int radio, order_status;
    String Order;
    String OrderID, transport;
    Button btMakePayment;
    ArrayList<String> sea_status, air_status;
    CardView containerOther;
    EditText etMessage;
    private IDeliveryMileStoneActivityController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_mile_stone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Delivery Mile Stone");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        sea_status = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.sea_status)));
        air_status = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.air_status)));
        Bundle extras = getIntent().getExtras();
        transport = extras.getString("transport");
        OrderID = extras.getString("OrderID");
        order_status = Integer.parseInt(extras.getString("order_status"));

        Log.d("%%%%%%%%order", "  " + order_status);

        mController = new DeliveryMileStoneActivityController(this);
        mController.getTrackTrace(OrderID);
        Lay_sea = (LinearLayout) findViewById(R.id.Lay_sea);
        Lay_air = (LinearLayout) findViewById(R.id.Lay_air);

        rg_air = (RadioGroup) findViewById(R.id.rg_air);
        rg_sea = (RadioGroup) findViewById(R.id.rg_sea);

        rb_cfs = (RadioButton) findViewById(R.id.rb_cfs);
        rb_pickup = (RadioButton) findViewById(R.id.rb_pickup);
        rb_pickup1 = (RadioButton) findViewById(R.id.rb_pickup1);
        rb_order = (RadioButton) findViewById(R.id.rb_order);
        rb_discharge = (RadioButton) findViewById(R.id.rb_discharge);
        rb_arrival = (RadioButton) findViewById(R.id.rb_arrival);
        rb_dispatched = (RadioButton) findViewById(R.id.rb_dispatched);
        rb_freight = (RadioButton) findViewById(R.id.rb_freight);
        rb_delivered = (RadioButton) findViewById(R.id.rb_delivered);

        rb_Sorder = (RadioButton) findViewById(R.id.rb_Sorder);
        rb_Sulddischarge = (RadioButton) findViewById(R.id.rb_Sulddischarge);
        rb_Sarrival = (RadioButton) findViewById(R.id.rb_Sarrival);
        rb_Sdispatched = (RadioButton) findViewById(R.id.rb_Sdispatched);
        rb_arrivalULD = (RadioButton) findViewById(R.id.rb_arrivalULD);
        rb_docllect = (RadioButton) findViewById(R.id.rb_docllect);
        rb_customer = (RadioButton) findViewById(R.id.rb_customer);

        //rb_delay_in_delivery_air = (RadioButton) findViewById(R.id.rb_delay_in_delivery_air);
        //rb_delay_in_delivery_sea = (RadioButton) findViewById(R.id.rb_delay_in_delivery_sea);
        rb_other_air = (RadioButton) findViewById(R.id.rb_other_air);
        rb_other_air.setOnCheckedChangeListener(this);
        rb_other_sea = (RadioButton) findViewById(R.id.rb_other_sea);
        rb_other_sea.setOnCheckedChangeListener(this);
        etMessage = (EditText) findViewById(R.id.etMessage);
        containerOther = (CardView) findViewById(R.id.containerOther);

        btMakePayment = (Button) findViewById(R.id.btMakePayment);


//        if (transport.equals("0") || transport.equals("2")) {
//            Log.d("%%%%%%%%transport", transport);
//            Lay_air.setVisibility(View.VISIBLE);
//            Lay_sea.setVisibility(View.GONE);
//
//            if (order_status == 0) {
//                RadioButton button = (RadioButton) rg_air.getChildAt(0);
//                button.setChecked(true);
//            } else {
//                RadioButton button = (RadioButton) rg_air.getChildAt(order_status - 1);
//                button.setChecked(true);
//            }
//
//        } else if (transport.equals("1") || transport.equals("3")) {
//            Log.d("######transport", transport);
//            Lay_sea.setVisibility(View.VISIBLE);
//            Lay_air.setVisibility(View.GONE);
//
//            if (order_status == 0) {
//                RadioButton button = (RadioButton) rg_sea.getChildAt(0);
//                button.setChecked(true);
//            } else {
//                RadioButton button = (RadioButton) rg_sea.getChildAt(order_status - 1);
//                button.setChecked(true);
//            }
//        }


        rg_air.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                radio = group.indexOfChild(radioButton);

                Order = String.valueOf(radio);
                Log.d("%%%%%%%%ID1", OrderID + "  " + Order);
            }
        });

        rg_sea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                radio = group.indexOfChild(radioButton);

                Order = String.valueOf(radio);
                Log.d("%%%%%%%%ID", OrderID + "  " + Order);
            }
        });


        btMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_other_air.isChecked() || rb_other_sea.isChecked()) {
                    mController.onTrackTrace(OrderID, Order, etMessage.getText().toString());
                } else {
                    mController.onTrackTrace(OrderID, Order, "");
                }

                Log.d("#######DDDDD", OrderID + "  " + Order);
            }
        });


    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (show) showProgress();
        else dismissProgress();
    }

    @Override
    public void getTrackTrace(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getTrackTraceFailed(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGetOrderStatus(int orderStatus) {

        this.order_status = orderStatus;

        if (transport.equals("0") || transport.equals("2")) {

            if (order_status == 0) {
                RadioButton button = (RadioButton) rg_air.getChildAt(0);
                button.setChecked(true);
            } else {
                RadioButton button = (RadioButton) rg_air.getChildAt(order_status - 1);
                button.setChecked(true);
            }

        } else if (transport.equals("1") || transport.equals("3")) {

            if (order_status == 0) {
                RadioButton button = (RadioButton) rg_sea.getChildAt(0);
                button.setChecked(true);
            } else {
                RadioButton button = (RadioButton) rg_sea.getChildAt(order_status - 1);
                button.setChecked(true);
            }
        }

        containerOther.setVisibility(View.GONE);

    }

    @Override
    public void onGetOrderStatus(int orderStatus, String message) {

        this.order_status = orderStatus;

        if (transport.equals("0") || transport.equals("2")) {
            RadioButton button = (RadioButton) rg_air.getChildAt(orderStatus - 1);
            button.setChecked(true);

        } else if (transport.equals("1") || transport.equals("3")) {
            RadioButton button = (RadioButton) rg_sea.getChildAt(orderStatus - 1);
            button.setChecked(true);
        }

        containerOther.setVisibility(View.VISIBLE);
        etMessage.setText(message);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            containerOther.setVisibility(View.VISIBLE);
        } else {
            containerOther.setVisibility(View.GONE);
        }
    }


}
