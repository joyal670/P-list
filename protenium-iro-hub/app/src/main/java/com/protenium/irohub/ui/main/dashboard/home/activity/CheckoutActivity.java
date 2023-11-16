package com.protenium.irohub.ui.main.dashboard.home.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.myfatoorah.sdk.model.executepayment.MFExecutePaymentRequest;
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentRequest;
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentResponse;
import com.myfatoorah.sdk.model.paymentstatus.MFGetPaymentStatusResponse;
import com.myfatoorah.sdk.utils.MFAPILanguage;
import com.myfatoorah.sdk.utils.MFCurrencyISO;
import com.myfatoorah.sdk.views.MFResult;
import com.myfatoorah.sdk.views.MFSDK;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.model.getAddress.Datum;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.home.adapter.AddressAdapter;
import com.protenium.irohub.ui.main.dashboard.home.viewmodel.CheckOutViewModel;
import com.protenium.irohub.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;

public class CheckoutActivity extends BaseActivity implements AddressAdapter.SelectedValue {

    private final List<Datum> addressList = new ArrayList<>();
    @BindView(R.id.addAddressBtn)
    MaterialButton addAddressBtn;
    @BindView(R.id.makePaymentBtn)
    MaterialButton makePaymentBtn;
    @BindView(R.id.tvPlanDuration)
    TextView tvPlanDuration;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tvMealsName)
    TextView tvMealsName;
    @BindView(R.id.tvBasePrice)
    TextView tvBasePrice;
    @BindView(R.id.tvCrab)
    TextView tvCrab;
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
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.radio_visa)
    RadioButton radio_visa;
    @BindView(R.id.radio_netcard)
    RadioButton radio_netcard;
    @BindView(R.id.tvSelectedAddress)
    TextView tvSelectedAddress;
    @BindView(R.id.cardViewSelectAddress)
    MaterialCardView cardViewSelectAddress;
    @BindView(R.id.tv_selectAddress)
    TextView tv_selectAddress;
    @BindView(R.id.tvGrandTotal)


    TextView tvGrandTotal;
    String payment_method;
    String paymentRef;
    private ArrayList<String> keyList = new ArrayList<>();
    private ArrayList<String> valueList = new ArrayList<>();
    private CheckOutViewModel checkOutViewModel;
    private String selectedAddressId = "";
    private int paymentMethod = 2;
    private String meal_id = "";
    private String unique = "";

    @Override
    public int setLayout() {
        return R.layout.activity_checkout;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        checkOutViewModel = ViewModelProviders.of(this).get(CheckOutViewModel.class);
        checkOutViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        initData();

    }

    private void initData() {
        keyList = getIntent().getStringArrayListExtra("keys");
        valueList = getIntent().getStringArrayListExtra("values");
        meal_id = getIntent().getStringExtra("meal_id");
        unique = getIntent().getStringExtra("unique");

        setValues();
        getAddress();
        setOnclick();

        String paymentUrl = "https://apitest.myfatoorah.com/";
        String PAYMENT_TOKEN = "rLtt6JWvbUHDDhsZnfpAhpYk4dxYDQkbcPTyGaKp2TYqQgG7FGZ5Th_WD53Oq8Ebz6A53njUoo1w3pjU1D4vs_ZMqFiz_j0urb_BH9Oq9VZoKFoJEDAbRZepGcQanImyYrry7Kt6MnMdgfG5jn4HngWoRdKduNNyP4kzcp3mRv7x00ahkm9LAK7ZRieg7k1PDAnBIOG3EyVSJ5kK4WLMvYr7sCwHbHcu4A5WwelxYK0GMJy37bNAarSJDFQsJ2ZvJjvMDmfWwDVFEVe_5tOomfVNt6bOg9mexbGjMrnHBnKnZR1vQbBtQieDlQepzTZMuQrSuKn-t5XZM7V6fCW7oP-uXGX-sMOajeX65JOf6XVpk29DP6ro8WTAflCDANC193yof8-f5_EYY-3hXhJj7RBXmizDpneEQDSaSz5sFk0sV5qPcARJ9zGG73vuGFyenjPPmtDtXtpx35A-BVcOSBYVIWe9kndG3nclfefjKEuZ3m4jL9Gg1h2JBvmXSMYiZtp9MR5I6pvbvylU_PP5xJFSjVTIz7IQSjcVGO41npnwIxRXNRxFOdIUHn0tjQ-7LwvEcTXyPsHXcMD8WtgBh-wxR8aKX7WPSsT1O8d8reb2aR7K3rkV3K82K_0OgawImEpwSvp9MNKynEAJQS6ZHe_J_l77652xwPNxMRTMASk1ZsJL";
        MFSDK.INSTANCE.init(paymentUrl, PAYMENT_TOKEN);
        MFSDK.INSTANCE.setUpActionBar("Checkout", R.color.white, R.color.colorPrimary, true);
    }

    private void getAddress() {
        checkOutViewModel.getAddress(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), "en").observe(this, getAddressResponse -> {
            if (getAddressResponse.getStatus()) {

                addressList.clear();
                addressList.addAll(getAddressResponse.getData());
                String address = "";

                for (int i = 0; i < getAddressResponse.getData().size(); i++) {
                    if (getAddressResponse.getData().get(i).getDefault() == 1) {
                        selectedAddressId = String.valueOf(getAddressResponse.getData().get(i).getId());
                        address = getAddressResponse.getData().get(i).getTitle() + "," + getAddressResponse.getData().get(i).getGovernorate() + "," + getAddressResponse.getData().get(i).getArea()
                                + "," + getAddressResponse.getData().get(i).getBlock() + "," + getAddressResponse.getData().get(i).getAvenue() + "," + getAddressResponse.getData().get(i).getStreet() +
                                "," + getAddressResponse.getData().get(i).getBuilding() + "," + getAddressResponse.getData().get(i).getFloor() + "," + getAddressResponse.getData().get(i).getAppartment();
                    }
                }

                tvSelectedAddress.setText(address);


                if (getAddressResponse.getData().isEmpty()) {
                    cardViewSelectAddress.setVisibility(View.GONE);
                    tv_selectAddress.setVisibility(View.GONE);
                } else {
                    cardViewSelectAddress.setVisibility(View.VISIBLE);
                    tv_selectAddress.setVisibility(View.VISIBLE);
                }

            } else {
                Toast.makeText(CheckoutActivity.this, "" + getAddressResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setValues() {
        tvDuration.setText(keyList.get(1));
        tvPlanDuration.setText(valueList.get(1) + " " + "weeks");

        tvMealsName.setText(keyList.get(2));
        tvBasePrice.setText("KDW" + " " + valueList.get(2));

        tvCrab.setText(keyList.get(3));
        tvCrabPrice.setText("KWD" + " " + valueList.get(3));

        tvProtein.setText(keyList.get(4));
        tvProteinPrice.setText("KWD" + " " + valueList.get(4));

        tvNonStop.setText(keyList.get(5));
        tvNonStopPrice.setText("KWD" + "" + valueList.get(5));

        tvTotalPrice.setText("KWD" + " " + valueList.get(6));
        tvGrandTotal.setText("KWD" + " " + valueList.get(6));
    }

    private void setOnclick() {

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, AddDeliveryAddressActivity.class);
                startActivity(intent);
            }
        });

        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedAddressId.isEmpty()){
                    CommonUtils.showWarning(CheckoutActivity.this, "Address is required");
                }else {
                    MFInitiatePaymentRequest request = new MFInitiatePaymentRequest(Double.parseDouble(valueList.get(6)), MFCurrencyISO.KUWAIT_KWD);
                    MFSDK.INSTANCE.initiatePayment(request, MFAPILanguage.EN, (MFResult<MFInitiatePaymentResponse> result) -> {
                        if (result instanceof MFResult.Success) {
                            setPayment();
                        } else if (result instanceof MFResult.Fail) {
                            CommonUtils.showWarning(CheckoutActivity.this, new Gson().toJson(((MFResult.Fail) result).getError()));
                        }

                        return Unit.INSTANCE;
                    });
                }
                //MFExecutePaymentRequest request = new MFExecutePaymentRequest(paymentMethod, Double.parseDouble(valueList.get(6)));



            }
        });

        radio_visa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paymentMethod = 1;
                    radio_netcard.setChecked(false);
                }
            }
        });

        radio_netcard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paymentMethod = 2;
                    radio_visa.setChecked(false);
                }
            }
        });

        tv_selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpBottomSheet();
            }
        });
    }

    private void setPayment() {
        MFExecutePaymentRequest request = new MFExecutePaymentRequest(paymentMethod, Double.parseDouble(valueList.get(6)));
        MFSDK.INSTANCE.executePayment(this, request, MFAPILanguage.EN,
                (String invoiceId, MFResult<MFGetPaymentStatusResponse> result) -> {
                    if (result instanceof MFResult.Success) {
                        Log.d("TAG", "Response: " + new Gson().toJson(((MFResult.Success<MFGetPaymentStatusResponse>) result).getResponse()));


                        String user_id = SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, "");

                        if (paymentMethod == 2) {
                            payment_method = "Master / Visa Card";
                        } else {
                            payment_method = "KNET";
                        }
                        paymentRef = ((MFResult.Success<MFGetPaymentStatusResponse>) result).getResponse().getInvoiceTransactions().get(0).getPaymentId();

                        Log.e("TAG", "setPayment: user_id" +user_id);
                        Log.e("TAG", "setPayment: meal_id" +Integer.parseInt(meal_id));
                        Log.e("TAG", "setPayment: selectedAddressId" + Integer.parseInt(selectedAddressId));
                        Log.e("TAG", "setPayment: payment_method" + payment_method);
                        Log.e("TAG", "setPayment: paymentRef" + paymentRef);
                        Log.e("TAG", "setPayment: unique" + unique);

                        checkOutViewModel.addFinalSub(user_id, Integer.parseInt(meal_id), 1, Integer.parseInt(selectedAddressId), payment_method, "", "en", paymentRef, unique, 0).observe(this, addFinalSubscriptionResponse -> {
                            if (addFinalSubscriptionResponse.getStatus()) {
                                Intent intent = new Intent(CheckoutActivity.this, OrderConfirmActivity.class);
                                intent.putExtra("keys", keyList);
                                intent.putExtra("values", valueList);
                                intent.putExtra("meal_id", meal_id);
                                intent.putExtra("unique", unique);
                                intent.putExtra("paymentMethod", String.valueOf(paymentMethod));
                                intent.putExtra("address", tvSelectedAddress.getText().toString());
                                startActivity(intent);
                            } else {
                                CommonUtils.showWarning(CheckoutActivity.this, addFinalSubscriptionResponse.getMessage());
                            }
                        });
                    } else if (result instanceof MFResult.Fail) {
                        CommonUtils.showWarning(CheckoutActivity.this, new Gson().toJson(((MFResult.Fail) result).getError()));
                    }

                    Log.d("TAG", "invoiceId: " + invoiceId);

                    return Unit.INSTANCE;
                });
    }

    private void setUpBottomSheet() {
        BottomSheetDialog bottom = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        View view = this.getLayoutInflater().inflate(R.layout.select_delivery_address, null);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView close = view.findViewById(R.id.ivCloseAddressDialog);
        MaterialButton selectBtn = view.findViewById(R.id.SelectAddressDialogBtn);
        RecyclerView rvDeliveryAddress = view.findViewById(R.id.rvDeliveryAddress);

        tvTitle.setText("Select Delivery Address");

        rvDeliveryAddress.setLayoutManager(new LinearLayoutManager(this));
        rvDeliveryAddress.setAdapter(new AddressAdapter(this, addressList));

        close.setOnClickListener(it -> bottom.dismiss());
        selectBtn.setOnClickListener(v -> {
            if (selectedAddressId.isEmpty()) {
                CommonUtils.showWarning(this, "Select Address");
            } else {
                checkOutViewModel.setDefaultAddress(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), selectedAddressId, "en").observe(this, setDefaultAddressResponse -> {
                    if (setDefaultAddressResponse.getStatus()) {
                        initData();
                    } else {
                        CommonUtils.showWarning(this, setDefaultAddressResponse.getMessage());
                    }
                });
                bottom.dismiss();
            }
        });

        bottom.setContentView(view);
        bottom.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    @Override
    public void setValue(Datum datum) {
        selectedAddressId = String.valueOf(datum.getId());
    }

    @Override
    public void delete(Datum datum) {
        checkOutViewModel.deleteAddress(datum.getId(), "en").observe(this, deleteAddressResponse -> {
            if (deleteAddressResponse.getStatus()) {
                CommonUtils.showWarning(this, deleteAddressResponse.getMessage());
            } else {
                CommonUtils.showWarning(this, deleteAddressResponse.getMessage());
            }
        });
    }
}