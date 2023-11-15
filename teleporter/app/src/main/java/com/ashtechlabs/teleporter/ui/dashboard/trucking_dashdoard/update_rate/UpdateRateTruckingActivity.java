package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.update_rate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapter;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapterNew;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.VehicleTypeActivity;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTrucking;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.DatePickerActivityDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.ashtechlabs.teleporter.util.SingleChoiceActivitytDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateRateTruckingActivity extends BaseActivity implements SingleChoiceItemDialogListener, OnDatePickListener,
        IUpdateRateTruckingActivityControllerCallback, View.OnClickListener {


    private static final int PLACE_PICKER_REQUEST_FROM = 100;
    private static final int PLACE_PICKER_REQUEST_TO = 200;

    String fromLocation;
    String toLocation;
    String minAmount, id;
    int selected_currency;
    ArrayList<String> arrayCurrency;
    String days, hours, duration, rateValidity, insuranceMinAmt, insurancePercentage;
    String token = "";
    String vechicleType = "-1";
    String vehicleSubType = "-1";
    String currency;
    private IUpdateRateTruckingActivityController mController;
    private TextView tvFromLocation;
    private TextView tvToLocation;
    private EditText editAmount;
    private EditText editDays;
    private EditText editHours;
    private EditText editInsPercentage, editInsMinAmt;
    private Button btnUpdate;
    private TextView tvCurrency;
    private ImageView ivCurrency;
    private TextView tvRateValidity, tvVechicleType;
    private PriceTrucking priceDriver;
    private int rateCardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rate_trucking);
        setUpToolbar();

        Places.createClient(this);

        tvFromLocation = (TextView) findViewById(R.id.tvFromLocation);
        tvFromLocation.setOnClickListener(this);
        tvToLocation = (TextView) findViewById(R.id.tvToLocation);
        tvToLocation.setOnClickListener(this);

        tvVechicleType = (TextView) findViewById(R.id.tvVechicleType);
        tvVechicleType.setOnClickListener(this);
        tvRateValidity = (TextView) findViewById(R.id.tvRateValidity);
        tvRateValidity.setOnClickListener(this);
        editAmount = (EditText) findViewById(R.id.editAmount);
        editInsPercentage = (EditText) findViewById(R.id.editInsPercentage);
        editInsMinAmt = (EditText) findViewById(R.id.editInsMinAmt);
        editDays = (EditText) findViewById(R.id.editDays);
        editHours = (EditText) findViewById(R.id.editHours);
        arrayCurrency = new ArrayList(Arrays.asList(this.getResources().getStringArray(R.array.array_currency)));

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        ivCurrency = (ImageView) findViewById(R.id.ivCurrency);
        tvCurrency.setOnClickListener(this);
        ivCurrency.setOnClickListener(this);

        mController = new UpdateRateTruckingActivityController(this);
        token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, "");

        Bundle extras = getIntent().getExtras();
        priceDriver = extras.getParcelable("price_trucking");
        rateCardType = extras.getInt("rate_card_type");

        if (rateCardType == 0) {


            tvFromLocation.setText(priceDriver.getFromLocation());
            tvToLocation.setText(priceDriver.getToLocation());
            duration = priceDriver.getDuration();
            selected_currency = Integer.parseInt(priceDriver.getCurrency());
            id = priceDriver.getId();
            vechicleType = priceDriver.getVehicleType();
            vehicleSubType = priceDriver.getSubVehicleType();
            if (Integer.parseInt(priceDriver.getDuration()) > 24) {
                int day = Integer.parseInt(priceDriver.getDuration());
                int days = day / 24;
                int hours = day % 24;
                editDays.setText(String.valueOf(days));
                editHours.setText(String.valueOf(hours));
            } else {
                editHours.setText(priceDriver.getDuration());
            }

            tvCurrency.setText(arrayCurrency.get(selected_currency));
            editAmount.setText(priceDriver.getMinAmount());

            if (TextUtils.isEmpty(priceDriver.getVehicleType()))
                vechicleType = "-1";

            if (TextUtils.isEmpty(priceDriver.getSubVehicleType()))
                vehicleSubType = "-1";

            tvVechicleType.setText(CommonUtils.getVechicleType(priceDriver.getVehicleType(), priceDriver.getSubVehicleType()));

            tvRateValidity.setText(priceDriver.getRateValidity());

            editInsPercentage.setText(priceDriver.getInsPercent());
            editInsMinAmt.setText(priceDriver.getInsuranceMinAmt());


        } else {

            btnUpdate.setText("ADD NEW RATE");
            id = extras.getString("id");
            tvFromLocation.setText(extras.getString("from_location"));
            tvToLocation.setText(extras.getString("to_location"));

        }

        /*editFromLocation.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
        editToLocation.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
*/
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapterNew(this, placesClient, autocompleteSessionToken));
//        editToLocation.setAdapter(new PlaceAutocompleteAdapterNew(this, placesClient, autocompleteSessionToken));

      /*  btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                uploadRateCard();
            }
        });*/
        // processOrder();
    }

    private void uploadRateCard() {

        fromLocation = tvFromLocation.getText().toString();
        toLocation = tvToLocation.getText().toString();
        minAmount = editAmount.getText().toString();
        days = editDays.getText().toString();
        hours = editHours.getText().toString();
        rateValidity = tvRateValidity.getText().toString();
        insuranceMinAmt = editInsMinAmt.getText().toString();
        insurancePercentage = editInsPercentage.getText().toString();
        rateValidity = tvRateValidity.getText().toString();


        currency = tvCurrency.getText().toString();
        if (fromLocation.equals("")) {
            Toast.makeText(this, "Please input From Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (toLocation.equals("")) {
            Toast.makeText(this, "Please input To Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (tvVechicleType.getText().equals("")) {
            Toast.makeText(this, "Please select Vehicle!", Toast.LENGTH_LONG).show();
            return;
        }

        if (minAmount.equals("")) {
            Toast.makeText(this, "Please input Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (insurancePercentage.equals("")) {
            Toast.makeText(this, "Please input Insurance percentage!", Toast.LENGTH_LONG).show();
            return;
        }

        if (insuranceMinAmt.equals("")) {
            Toast.makeText(this, "Please input Insurance min amount!", Toast.LENGTH_LONG).show();
            return;
        }
        if (rateValidity.equals("")) {
            Toast.makeText(this, "Please input rate card validity!", Toast.LENGTH_LONG).show();
            return;
        }

        if (days.equals("")) {
            Toast.makeText(this, "Please input the days!", Toast.LENGTH_LONG).show();
            return;
        }

        if (hours.equals("")) {
            Toast.makeText(this, "Please input the hours!", Toast.LENGTH_LONG).show();
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

        String duration = String.valueOf(Integer.valueOf(days) * 24 + Integer.valueOf(hours));

        mController.getUpdateRateCard(rateCardType, token, Integer.parseInt(vechicleType), Integer.parseInt(vehicleSubType), fromLocation, toLocation, minAmount, rateValidity, insurancePercentage, insuranceMinAmt, duration, selected_currency, id);
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvFromLocation:
                callPlaceIntent(PLACE_PICKER_REQUEST_FROM);
                break;

            case R.id.tvToLocation:
                callPlaceIntent(PLACE_PICKER_REQUEST_TO);
                break;

            case R.id.btnUpdate:
                uploadRateCard();

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
            case R.id.tvVechicleType:

                Intent intent = new Intent(this, VehicleTypeActivity.class);
                startActivityForResult(intent, 501);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            if (requestCode == 501) {

                vechicleType = data.getStringExtra(Constants.KEY_VEHICLE_TYPE);
                vehicleSubType = data.getStringExtra(Constants.KEY_VEHICLE_SUB_TYPE);

                if ((TextUtils.isEmpty(vechicleType)))
                    vechicleType = "-1";

                if ((TextUtils.isEmpty(vehicleSubType)))
                    vehicleSubType = "-1";

                tvVechicleType.setText(data.getStringExtra(Constants.KEY_VEHICLE_TYPE_NAME));

                //CommonUtils.showToast(getActivity(), "" + vehicleType + "-" + vehicleTypeName + " || " + vehicleSubType + "-" + vehicleSubTypeName);
            }

         else {

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
            }
        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
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
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }

    @Override
    public void onUpdateRateSuccess(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateRateFailed(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

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
