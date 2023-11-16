package com.protenium.irohub.ui.main.dashboard.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.protenium.irohub.R;
import com.protenium.irohub.base.BaseActivity;
import com.protenium.irohub.model.getGovernate.Datum;
import com.protenium.irohub.shared_pref.SharedPrefs;
import com.protenium.irohub.ui.main.dashboard.home.adapter.AreaAdapter;
import com.protenium.irohub.ui.main.dashboard.home.adapter.GovernoteAdapter;
import com.protenium.irohub.ui.main.dashboard.home.viewmodel.AddressViewModel;
import com.protenium.irohub.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDeliveryAddressActivity extends BaseActivity implements GovernoteAdapter.SelectedValue, AreaAdapter.SelectedValue {

    private final ArrayList<com.protenium.irohub.model.getArea.Datum> areasList = new ArrayList<>();
    @BindView(R.id.btnSubmitAddress)
    MaterialButton btnSubmitAddress;
    @BindView(R.id.add_delivery_spinner)
    EditText add_delivery_spinner;
    @BindView(R.id.etArea)
    EditText etArea;
    @BindView(R.id.etBlock)
    EditText etBlock;
    @BindView(R.id.et_street)
    EditText et_street;
    @BindView(R.id.et_avenue)
    EditText et_avenue;
    @BindView(R.id.et_building)
    EditText et_building;
    @BindView(R.id.et_floor)
    EditText et_floor;
    @BindView(R.id.et_appartment)
    EditText et_appartment;
    @BindView(R.id.switchDefaultAddress)
    SwitchMaterial switchDefaultAddress;


    List<Datum> dataList = new ArrayList<>();
    private AddressViewModel addressViewModel;
    private String selectedGovernateId = "";
    private String selectedAreaId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        addressViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        getGovernate();
        setOnclick();

    }

    private void getGovernate() {
        addressViewModel.getGonvernate("en").observe(this, getGovernateResponse -> {
            if (getGovernateResponse.getStatus()) {
                dataList.addAll(getGovernateResponse.getData());
            } else {
                CommonUtils.showWarning(AddDeliveryAddressActivity.this, getGovernateResponse.getMessage());
            }
        });
    }

    private void setOnclick() {
        btnSubmitAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedGovernateId.isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Governorate");
                } else if (selectedAreaId.isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Area");
                } else if (etBlock.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Block");
                } else if (et_street.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Street");
                } else if (et_avenue.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Avenue");
                } else if (et_building.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Building");
                } else if (et_floor.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Floor");
                } else if (et_appartment.getText().toString().isEmpty()) {
                    CommonUtils.showWarning(AddDeliveryAddressActivity.this, "Select Apartment");
                } else {
                    int def = 0;
                    if (switchDefaultAddress.isChecked()) {
                        def = 1;
                    }

                    addressViewModel.addAddress(SharedPrefs.getString(SharedPrefs.Keys.CUSTOMER_ID, ""), Integer.parseInt(selectedGovernateId), Integer.parseInt(selectedAreaId), etBlock.getText().toString(), et_avenue.getText().toString(), et_street.getText().toString(), et_building.getText().toString(), et_floor.getText().toString(), et_appartment.getText().toString(), def, "en").observe(AddDeliveryAddressActivity.this, addAddressResponse -> {
                        if (addAddressResponse.getStatus()) {
                            Toast.makeText(AddDeliveryAddressActivity.this, ""+ addAddressResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            AddDeliveryAddressActivity.super.onBackPressed();
                        } else {
                            CommonUtils.showWarning(AddDeliveryAddressActivity.this, addAddressResponse.getMessage());
                        }
                    });
                }


            }
        });

        add_delivery_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGovernateBottomSheet();
            }
        });

        etArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGovernateId.isEmpty()) {
                    etArea.setEnabled(false);
                    etArea.setClickable(false);
                } else {
                    etArea.setEnabled(true);
                    etArea.setClickable(true);
                    setUpAreaBottomSheet();
                }
            }
        });

    }

    private void setUpAreaBottomSheet() {
        BottomSheetDialog bottom = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        View view = this.getLayoutInflater().inflate(R.layout.select_delivery_address, null);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView close = view.findViewById(R.id.ivCloseAddressDialog);
        MaterialButton selectBtn = view.findViewById(R.id.SelectAddressDialogBtn);
        RecyclerView rvDeliveryAddress = view.findViewById(R.id.rvDeliveryAddress);

        tvTitle.setText("Area");

        rvDeliveryAddress.setLayoutManager(new LinearLayoutManager(this));
        rvDeliveryAddress.setAdapter(new AreaAdapter(this, areasList));

        close.setOnClickListener(it -> bottom.dismiss());
        selectBtn.setOnClickListener(v -> {
            if (selectedAreaId.isEmpty()) {
                CommonUtils.showWarning(this, "Select any options");
            } else {
                bottom.dismiss();
            }
        });

        bottom.setContentView(view);
        bottom.show();
    }

    private void setUpGovernateBottomSheet() {
        BottomSheetDialog bottom = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        View view = this.getLayoutInflater().inflate(R.layout.select_delivery_address, null);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView close = view.findViewById(R.id.ivCloseAddressDialog);
        MaterialButton selectBtn = view.findViewById(R.id.SelectAddressDialogBtn);
        RecyclerView rvDeliveryAddress = view.findViewById(R.id.rvDeliveryAddress);

        tvTitle.setText("Governorate");

        rvDeliveryAddress.setLayoutManager(new LinearLayoutManager(this));
        rvDeliveryAddress.setAdapter(new GovernoteAdapter(this, dataList));

        close.setOnClickListener(it -> bottom.dismiss());
        selectBtn.setOnClickListener(v -> {
            if (selectedGovernateId.isEmpty()) {
                CommonUtils.showWarning(this, "Select any options");
            } else {
                bottom.dismiss();
                addressViewModel.getArea(selectedGovernateId, "en").observe(this, getAreaResponse -> {
                    if (getAreaResponse.getStatus()) {
                        areasList.clear();
                        areasList.addAll(getAreaResponse.getData());
                    } else {
                        CommonUtils.showWarning(AddDeliveryAddressActivity.this, getAreaResponse.getMessage());
                    }
                });
            }
        });

        bottom.setContentView(view);
        bottom.show();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_add_delivery_address;
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
    public void setValue(Datum datum) {
        selectedGovernateId = datum.getId().toString();
        add_delivery_spinner.setText(datum.getName());
    }

    @Override
    public void setValue(com.protenium.irohub.model.getArea.Datum datum) {
        selectedAreaId = datum.getId().toString();
        etArea.setText(datum.getName());
    }
}