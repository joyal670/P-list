package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_rate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapter;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapterNew;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.DatePickerActivityDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.ashtechlabs.teleporter.util.SingleChoiceActivitytDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateRateVendorActivity extends BaseActivity implements SingleChoiceItemDialogListener, CompoundButton.OnCheckedChangeListener,
        IUpdateRateVendorActivityControllerCallback, View.OnClickListener, OnDatePickListener {

    private static final int PLACE_PICKER_REQUEST_FROM = 100;
    private static final int PLACE_PICKER_REQUEST_TO = 200;

    String token;
    String addinsurance, minInsuranceAmt;
    int selected_currency;
    int type = -1;
    ArrayList<String> arrayCurrency;
    PriceVendor priceVendor;
    String fromLocations, toLocations, rateValidity, minAmounts, perKGAmounts, days, hours, durations, id;
    private IUpdateRateVendorActivityController mController;
    private TextView tvFromLocation;
    private TextView tvToLocation;
    //RelativeLayout sprServiceType;
    private EditText editMinAmount, editPerKgAmount, etInsurancePercentage, etMinInsuranceAmt;
    private EditText editDays;
    private EditText editHours;
    private int additional_info = -1;
    private int perishable_det = 0;
    private RadioButton rbDoorToDoor, rbPortToPort, rbDoorToPort, rbPortToDoor;
    private Button btnUpdate;
    private String serviceType = "0";
    private TextView tvCurrency, tvRateValidity;
    private ImageView ivCurrency;
    private CheckBox cbHazardous, cbPerishable, cbGeneral;
    private LinearLayout layRadiopersiable;
    private RadioButton rbCool, rbCold, rbFrozen;
    private int rateCardType;
    private int serviceMode;
   // private int shipmentOption = 0;
   // private RadioGroup rg_shiping;
   // private RadioButton rb_single, rb_regular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rate_vendor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpToolbar();

        Places.createClient(this);

        arrayCurrency = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_currency)));

        mController = new UpdateRateVendorActivityController(this);
        token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, "");

        //air = (ImageView) view.findViewById(R.id.air);
        rbDoorToDoor = (RadioButton) findViewById(R.id.rbDoorToDoor);
        rbDoorToDoor.setOnCheckedChangeListener(this);
        //cargo = (ImageView) view.findViewById(R.id.cargo);
        rbPortToPort = (RadioButton) findViewById(R.id.rbPortToPort);
        rbPortToPort.setOnCheckedChangeListener(this);
        //truck = (ImageView) view.findViewById(R.id.truck);
        rbDoorToPort = (RadioButton) findViewById(R.id.rbDoorToPort);
        rbDoorToPort.setOnCheckedChangeListener(this);
        //trolley = (ImageView) view.findViewById(R.id.trolley);
        rbPortToDoor = (RadioButton) findViewById(R.id.rbPortToDoor);
        rbPortToDoor.setOnCheckedChangeListener(this);


        tvFromLocation = (TextView) findViewById(R.id.tvFromLocation);
        tvFromLocation.setOnClickListener(this);
        tvToLocation = (TextView) findViewById(R.id.tvToLocation);
        tvToLocation.setOnClickListener(this);
        //sprServiceType = (RelativeLayout) view.findViewById(R.id.sprServiceType);
        //ServiceType = (TextView) view.findViewById(R.id.ServiceType);


        editMinAmount = (EditText) findViewById(R.id.editMinAmount);
        editPerKgAmount = (EditText) findViewById(R.id.editPerKgAmount);

        etInsurancePercentage = (EditText) findViewById(R.id.etInsurancePercentage);
        etMinInsuranceAmt = (EditText) findViewById(R.id.etMinInsuranceAmt);
        cbHazardous = (CheckBox) findViewById(R.id.cbHazardous);
        tvRateValidity = (TextView) findViewById(R.id.tvRateValidity);
        tvRateValidity.setOnClickListener(this);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        ivCurrency = (ImageView) findViewById(R.id.ivCurrency);
        tvCurrency.setOnClickListener(this);
        ivCurrency.setOnClickListener(this);
        editDays = (EditText) findViewById(R.id.editDays);
        editHours = (EditText) findViewById(R.id.editHours);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        rbCool = (RadioButton) findViewById(R.id.rbCool);
        rbCool.setOnCheckedChangeListener(this);
        rbCold = (RadioButton) findViewById(R.id.rbCold);
        rbCold.setOnCheckedChangeListener(this);
        rbFrozen = (RadioButton) findViewById(R.id.rbFrozen);
        rbFrozen.setOnCheckedChangeListener(this);

//        rg_shiping = (RadioGroup) findViewById(R.id.rg_shiping);
//        rb_single = (RadioButton) findViewById(R.id.rb_single);
//        rb_regular = (RadioButton) findViewById(R.id.rb_regular);

        layRadiopersiable = (LinearLayout) findViewById(R.id.layRadiopersiable);
        cbHazardous = (CheckBox) findViewById(R.id.cbHazardous);
        cbGeneral = (CheckBox) findViewById(R.id.cbGeneral);
        cbPerishable = (CheckBox) findViewById(R.id.cbPerishable);
        cbGeneral.setOnCheckedChangeListener(this);
        cbPerishable.setOnCheckedChangeListener(this);
        cbHazardous.setOnCheckedChangeListener(this);


        Bundle extras = getIntent().getExtras();
        priceVendor = extras.getParcelable("priceVendor");
        rateCardType = extras.getInt("rate_card_type");

        if (rateCardType == 0) {
            if (priceVendor.getTransport().equals("0")) {
                rbDoorToDoor.setChecked(true);
                type = 0;
            } else if (priceVendor.getTransport().equals("1")) {
                rbPortToPort.setChecked(true);
                type = 1;
            } else if (priceVendor.getTransport().equals("2")) {
                rbDoorToPort.setChecked(true);
                type = 2;
            } else if (priceVendor.getTransport().equals("3")) {
                rbPortToDoor.setChecked(true);
                type = 3;
            }

//            if (priceVendor.getShipment() == 0) {
//                rb_single.setChecked(true);
//                shipmentOption = 0;
//            } else {
//                rb_regular.setChecked(true);
//                shipmentOption = 1;
//            }

            tvFromLocation.setText(priceVendor.getFromLocation());
            tvToLocation.setText(priceVendor.getToLocation());
            editMinAmount.setText(priceVendor.getMinAmount());
            editPerKgAmount.setText(priceVendor.getPerKGAmount());
            id = priceVendor.getId();
            type = Integer.parseInt(priceVendor.getTransport());
            durations = priceVendor.getDuration();
            etInsurancePercentage.setText(priceVendor.getAddInsurance());
            etMinInsuranceAmt.setText(priceVendor.getInsuranceMinAmt());
            tvRateValidity.setText(priceVendor.getRateValidity());

            additional_info = Integer.parseInt(priceVendor.getAdditionalInfo());
            if (additional_info == 0) {
                cbGeneral.setChecked(true);
            } else if (additional_info == 1) {
                cbHazardous.setChecked(true);
            } else if (additional_info == 2) {
                cbPerishable.setChecked(true);
            }

            if (additional_info == 2) {
                layRadiopersiable.setVisibility(View.VISIBLE);
                perishable_det = Integer.parseInt(priceVendor.getPerishableDet());
                if (perishable_det == 1) {
                    rbCool.setChecked(true);
                } else if (perishable_det == 2) {
                    rbCold.setChecked(true);
                } else if (perishable_det == 3) {
                    rbFrozen.setChecked(true);
                }

            }
            // Log.d("################",priceVendor.getHazardous()+"jj");

            if (Integer.parseInt(priceVendor.getDuration()) > 24) {
                int day = Integer.parseInt(priceVendor.getDuration());
                int days = day / 24;
                int hours = day % 24;
                editDays.setText(String.valueOf(days));
                editHours.setText(String.valueOf(hours));
            } else {
                editHours.setText(priceVendor.getDuration());
                editHours.setText("0");
            }

            selected_currency = Integer.parseInt(priceVendor.getCurrency());
            tvCurrency.setText(arrayCurrency.get(selected_currency));


        } else {

            btnUpdate.setText("ADD NEW RATE");
            id = extras.getString("id");
            serviceMode = extras.getInt("service_mode", 0);
            if (serviceMode == 0) {
                rbDoorToDoor.setChecked(true);
                type = 0;
            } else if (serviceMode == 1) {
                rbPortToPort.setChecked(true);
                type = 1;
            } else if (serviceMode == 2) {
                rbDoorToPort.setChecked(true);
                type = 2;
            } else if (serviceMode == 3) {
                rbPortToDoor.setChecked(true);
                type = 3;
            }


            additional_info = extras.getInt("additional_info", 0);
            //shipmentOption = extras.getInt("shipment", 0);
            if (additional_info == 0) {
                cbGeneral.setChecked(true);
            } else if (additional_info == 1) {
                cbHazardous.setChecked(true);
            } else if (additional_info == 2) {
                cbPerishable.setChecked(true);
            }

            if (additional_info == 2) {
                layRadiopersiable.setVisibility(View.VISIBLE);
                perishable_det = extras.getInt("perishable_det", 0);
                if (perishable_det == 1) {
                    rbCool.setChecked(true);
                } else if (perishable_det == 2) {
                    rbCold.setChecked(true);
                } else if (perishable_det == 3) {
                    rbFrozen.setChecked(true);
                }
            }

//            if (shipmentOption == 0) {
//                rb_single.setChecked(true);
//            } else {
//                rb_regular.setChecked(true);
//            }

            tvFromLocation.setText(extras.getString("from_location"));
            tvToLocation.setText(extras.getString("to_location"));

        }


       /* editFromLocation.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
        editToLocation.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));*/
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, null));
//        editToLocation.setAdapter(new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, null));
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapterNew(this, placesClient, autocompleteSessionToken));
//        editToLocation.setAdapter(new PlaceAutocompleteAdapterNew(this, placesClient, autocompleteSessionToken));
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                uploadRateCard();
            }
        });

    }


    private void uploadRateCard() {

        fromLocations = tvFromLocation.getText().toString();
        toLocations = tvToLocation.getText().toString();
        minAmounts = editMinAmount.getText().toString();
        perKGAmounts = editPerKgAmount.getText().toString();
        days = editDays.getText().toString();
        hours = editHours.getText().toString();
        String currency = tvCurrency.getText().toString();
        addinsurance = etInsurancePercentage.getText().toString();
        minInsuranceAmt = etMinInsuranceAmt.getText().toString();
        rateValidity = tvRateValidity.getText().toString();


        if (type == -1) {
            Toast.makeText(this, "Please select mode of transportation!", Toast.LENGTH_LONG).show();
            return;
        }

        if (fromLocations.equals("")) {
            Toast.makeText(this, "Please input From Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (toLocations.equals("")) {
            Toast.makeText(this, "Please input To Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (addinsurance.equals("")) {
            Toast.makeText(this, "Please enter Insurance Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (minAmounts.equals("")) {
            Toast.makeText(this, "Please input Min Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (perKGAmounts.equals("")) {
            Toast.makeText(this, "Please input Per Kg Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (days.equals("")) {
            Toast.makeText(this, "Please input Days!", Toast.LENGTH_LONG).show();
            return;
        }

        if (hours.equals("")) {
            Toast.makeText(this, "Please input hours!", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(days) == 0 && Integer.parseInt(hours) == 0) {
            Toast.makeText(this, "Please input a transit day or hour", Toast.LENGTH_LONG).show();
            return;
        }

        if (currency.equals("")) {
            Toast.makeText(this, "Please input the currency!", Toast.LENGTH_LONG).show();
            return;
        }

        if (cbGeneral.isChecked()) {
            additional_info = 0;
            perishable_det = 0;
        } else if (cbHazardous.isChecked()) {
            additional_info = 1;
            perishable_det = 0;
        } else if (cbPerishable.isChecked()) {
            additional_info = 2;
        } else {
            CommonUtils.showToast(this, "Select Cargo type");
            additional_info = -1;
            return;
        }

        if (cbPerishable.isChecked()) {
            if (rbCool.isChecked()) {
                perishable_det = 1;
            } else if (rbCold.isChecked()) {
                perishable_det = 2;
            } else if (rbFrozen.isChecked()) {
                perishable_det = 3;
            } else {
                CommonUtils.showToast(this, "Select Perishable type");
                perishable_det = 0;
                return;
            }
        }

//        if (rb_single.isChecked()) {
//            shipmentOption = 0;
//        } else {
//            shipmentOption = 1;
//        }

        durations = String.valueOf(Integer.valueOf(days) * 24 + Integer.valueOf(hours));
        mController.getUpdateVendorRateCard(rateCardType, token, id, serviceType, fromLocations, toLocations, additional_info, perishable_det, minAmounts, perKGAmounts, rateValidity, addinsurance, minInsuranceAmt, durations, selected_currency);

    }

    private void formatData() {
        tvFromLocation.setText("");
        tvToLocation.setText("");
        editMinAmount.setText("");
        editPerKgAmount.setText("");
        editDays.setText("");
        editHours.setText("");
    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Update Rate");
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
    public void setOnSelectItem(String item, int position, String tag) {

        if (tag.equals("currency_dialog")) {
            tvCurrency.setText(item);
            selected_currency = position;
        }
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
    public void onUpdateVendorRateSuccess(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
//        if (!cbHazardous.isChecked()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRateVendorActivity.this);
//            builder.setMessage("Do you need to update ratecard for Hazardous ?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            cbHazardous.setChecked(true);
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            //Setting the title manually
//            alert.setTitle("");
//            alert.show();
//        }
    }

    @Override
    public void onUpdateVendorRateFailed(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvFromLocation:
                callPlaceIntent(PLACE_PICKER_REQUEST_FROM);
                break;

            case R.id.tvToLocation:
                callPlaceIntent(PLACE_PICKER_REQUEST_TO);
                break;

            case R.id.ivCurrency:

                SingleChoiceActivitytDialog currencyListFragment = new SingleChoiceActivitytDialog();
                Bundle argsCurrency = new Bundle();
                argsCurrency.putString("title", "Currency");
                argsCurrency.putStringArrayList("list_array", arrayCurrency);
                currencyListFragment.setArguments(argsCurrency);
                currencyListFragment.show(this.getSupportFragmentManager(), "currency_dialog");

                break;

            case R.id.tvCurrency:
                SingleChoiceActivitytDialog currencyList = new SingleChoiceActivitytDialog();
                Bundle args2 = new Bundle();
                args2.putString("title", "Currency");
                args2.putStringArrayList("list_array", arrayCurrency);
                currencyList.setArguments(args2);
                currencyList.show(this.getSupportFragmentManager(), "currency_dialog");

                break;

            case R.id.tvRateValidity:
                DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
                datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_dialog");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);

            if (requestCode == PLACE_PICKER_REQUEST_FROM) {

                Log.e("NAME", place.getName());
                Log.e("ADDRESS", place.getAddress());

                tvFromLocation.setText(place.getAddress());
            }
            if (requestCode == PLACE_PICKER_REQUEST_TO) {

                Log.e("NAME", place.getName());
                Log.e("ADDRESS", place.getAddress());

                tvToLocation.setText(place.getAddress());
            }

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(
                    this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            Log.i("ERROR", status.getStatusMessage());
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cbGeneral) {
            if (isChecked) {
                cbPerishable.setChecked(false);
                cbHazardous.setChecked(false);
                layRadiopersiable.setVisibility(View.GONE);
                perishable_det = 0;
            }

        } else if (buttonView.getId() == R.id.cbPerishable) {
            if (isChecked) {
                cbGeneral.setChecked(false);
                cbHazardous.setChecked(false);
                layRadiopersiable.setVisibility(View.VISIBLE);
            }
        } else if (buttonView.getId() == R.id.cbHazardous) {
            if (isChecked) {
                cbGeneral.setChecked(false);
                cbPerishable.setChecked(false);
                layRadiopersiable.setVisibility(View.GONE);
                perishable_det = 0;
            }
        } else if (buttonView.getId() == R.id.rbDoorToDoor) {
            if (isChecked) {
                serviceType = "0";
            }
        } else if (buttonView.getId() == R.id.rbPortToPort) {
            if (isChecked) {
                serviceType = "1";
            }
        } else if (buttonView.getId() == R.id.rbDoorToPort) {
            if (isChecked) {
                serviceType = "2";
            }
        } else if (buttonView.getId() == R.id.rbPortToDoor) {
            if (isChecked) {
                serviceType = "3";
            }
        } else if (buttonView.getId() == R.id.rbCool) {
            if (isChecked) {
                perishable_det = 1;
            }
        } else if (buttonView.getId() == R.id.rbCold) {
            if (isChecked) {
                perishable_det = 2;
            }
        } else if (buttonView.getId() == R.id.rbFrozen) {
            if (isChecked) {
                perishable_det = 3;
            }
        }
    }

    private void callPlaceIntent(int requestCode) {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setDate(String date, String tag) {
        tvRateValidity.setText(date);
    }
}
