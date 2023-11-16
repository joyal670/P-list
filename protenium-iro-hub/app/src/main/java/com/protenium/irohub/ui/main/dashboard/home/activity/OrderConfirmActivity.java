package com.protenium.irohub.ui.main.dashboard.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.ui.main.dashboard.activity.HomeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderConfirmActivity extends BaseActivity {

    @BindView(R.id.selectMealBtn)
    MaterialButton selectMealBtn;

    @BindView(R.id.tvDurationValue)
    TextView tvDurationValue;

    @BindView(R.id.tvDuration)
    TextView tvDuration;

    @BindView(R.id.tvPlan)
    TextView tvPlan;

    @BindView(R.id.tvBasicFee)
    TextView tvBasicFee;

    @BindView(R.id.tvCarb)
    TextView tvCarb;

    @BindView(R.id.tvCrabPrice)
    TextView tvCrabPrice;

    @BindView(R.id.tvProtein)
    TextView tvProtein;

    @BindView(R.id.tvProteinPrice)
    TextView tvProteinPrice;

    @BindView(R.id.tvNonStop)
    TextView tvNonStop;

    @BindView(R.id.tvNonStopPrice)
    TextView tvNonStopPrice;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.ivPayment)
    ImageView ivPayment;

    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;

    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;

    String payment_method = "";
    String address = "";
    private ArrayList<String> keyList = new ArrayList<>();
    private ArrayList<String> valueList = new ArrayList<>();
    private String meal_id = "";
    private String unique = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        keyList = getIntent().getStringArrayListExtra("keys");
        valueList = getIntent().getStringArrayListExtra("values");
        meal_id = getIntent().getStringExtra("meal_id");
        unique = getIntent().getStringExtra("unique");
        payment_method = getIntent().getStringExtra("paymentMethod");
        address = getIntent().getStringExtra("address");

        setValues();
        setOnClick();


    }

    private void setValues() {

        tvDuration.setText(keyList.get(1));
        tvDurationValue.setText(valueList.get(1) + " " + "weeks");

        tvPlan.setText(keyList.get(2));
        tvBasicFee.setText("KDW" + " " + valueList.get(2));

        tvCarb.setText(keyList.get(3));
        tvCrabPrice.setText("KWD" + " " + valueList.get(3));

        tvProtein.setText(keyList.get(4));
        tvProteinPrice.setText("KWD" + " " + valueList.get(4));

        tvNonStop.setText(keyList.get(5));
        tvNonStopPrice.setText("KWD" + "" + valueList.get(5));

        tvSubTotal.setText("KWD" + " " + valueList.get(6));
        tvGrandTotal.setText("KWD" + " " + valueList.get(6));

        tvAddress.setText(address);
        if (payment_method.equals("1")) {
            ivPayment.setImageResource(R.drawable.sample_netcard);
        } else {
            ivPayment.setImageResource(R.drawable.sample_visa);
        }


    }

    private void setOnClick() {

        selectMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_order_confirm;
    }

    @Override
    public boolean setToolbar() {
        return false;
    }

    @Override
    public boolean hideStatusBar() {
        return false;
    }

    @Override
    public boolean setFullScreen() {
        return false;
    }
}