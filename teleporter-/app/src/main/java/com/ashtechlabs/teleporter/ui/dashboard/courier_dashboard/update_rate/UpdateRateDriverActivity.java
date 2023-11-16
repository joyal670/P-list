package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.update_rate;

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
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceCourier;
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

public class UpdateRateDriverActivity extends BaseActivity implements SingleChoiceItemDialogListener, IUpdateRateDriverActivityControllerCallback, View.OnClickListener, OnDatePickListener {

    private static final int PLACE_PICKER_REQUEST_FROM = 100;
    private static final int PLACE_PICKER_REQUEST_TO = 200;

    String fromLocation;
    String toLocation;
    String minAmount, id;
    int selected_currency;
    ArrayList<String> arrayCurrency, array_delivery;
    String perKGAmount, days, hours, duration;
    String token = "";
    int deliveryType = 0;
    String currency;
    private IUpdateRateDriverActivityController mController;
    private TextView tvFromLocation;
    private TextView tvToLocation;
    private EditText editMinAmount;
    private EditText editPerKgAmount;
    private EditText editDays;
    private EditText editHours;
    private Button btnUpdate;
    private TextView tvCurrency;
    private ImageView ivCurrency;
    private TextView tvDeliverySelect, tvRateValidity;
    private PriceCourier priceDriver;
    private ArrayList<String> arraydelivary = new ArrayList<>();
    private String rateValidity, priceDriverDuration;
    private int rateCardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rate_driver);
        setUpToolbar();

        Places.createClient(this);

        tvFromLocation = (TextView) findViewById(R.id.tvFromLocation);
        tvFromLocation.setOnClickListener(this);
        tvToLocation = (TextView) findViewById(R.id.tvToLocation);
        tvToLocation.setOnClickListener(this);

        editMinAmount = (EditText) findViewById(R.id.editMinAmount);
        editPerKgAmount = (EditText) findViewById(R.id.editPerKgAmount);
        editDays = (EditText) findViewById(R.id.editDays);
        editHours = (EditText) findViewById(R.id.editHours);
        arrayCurrency = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_currency)));
        array_delivery = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_delivery)));

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        tvDeliverySelect = (TextView) findViewById(R.id.sprDeliveryType);
        tvDeliverySelect.setOnClickListener(this);
        tvRateValidity = (TextView) findViewById(R.id.tvRateValidity);
        tvRateValidity.setOnClickListener(this);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        ivCurrency = (ImageView) findViewById(R.id.ivCurrency);
        tvCurrency.setOnClickListener(this);
        ivCurrency.setOnClickListener(this);
        mController = new UpdateRateDriverActivityController(this);
        token = GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, "");

        arraydelivary.clear();

        arraydelivary.add("Docs");
        arraydelivary.add("Parcel");
        arraydelivary.add("Food");
        arraydelivary.add("Grocery");
        arraydelivary.add("Plants & Pets");
        arraydelivary.add("Fragile");
        arraydelivary.add("Others");

        Bundle extras = getIntent().getExtras();
        priceDriver = extras.getParcelable("price_driver");
        rateCardType = extras.getInt("rate_card_type");
        priceDriverDuration = extras.getString("priceDriverDuration");


        if (rateCardType == 0) {

            duration = priceDriver.getDuration();
            selected_currency = priceDriver.getCurrency();
            if (TextUtils.isEmpty(priceDriver.getDeliveryType())) {
                deliveryType = 0;
            } else {
                deliveryType = Integer.parseInt(priceDriver.getDeliveryType());
            }

            id = priceDriver.getId();


            tvFromLocation.setText(priceDriver.getFromLocation());
            tvToLocation.setText(priceDriver.getToLocation());
            tvDeliverySelect.setText(arraydelivary.get(deliveryType));

            //tvDeliverySelect.setText(arraydelivary.get(Integer.parseInt(deliveryType)));
            tvRateValidity.setText(priceDriver.getRateValidity());
            Log.d("MESSAGE", priceDriver.getRateValidity() + "....>>>");

            Log.d("WSWSWS", priceDriverDuration);
            if (Integer.parseInt(priceDriverDuration) > 24) {
                int day = Integer.parseInt(priceDriverDuration);
                int days = day / 24;
                int hours = day % 24;
                editDays.setText(String.valueOf(days));
                editHours.setText(String.valueOf(hours));
            } else {
                editHours.setText(priceDriver.getDuration());
                editDays.setText("0");
            }

            tvCurrency.setText(arrayCurrency.get(selected_currency));


//        radio_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//                // TODO Auto-generated method stub
//                if (arg1) {
//                    deliverySpinner.setVisibility(View.VISIBLE);
//                    moverSpinner.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        radio_mover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//                // TODO Auto-generated method stub
//                if (arg1) {
//                    moverSpinner.setVisibility(View.VISIBLE);
//                    deliverySpinner.setVisibility(View.GONE);
//                }
//            }
//        });


//            if (priceDriver.getDeliveryType() != null) {
//                deliverySpinner.setVisibility(View.VISIBLE);
//                moverSpinner.setVisibility(View.GONE);
//                mode = priceDriver.getDeliveryType();
//
//            } else if (priceDriver.getVehicleType() != null) {
//                deliverySpinner.setVisibility(View.GONE);
//                moverSpinner.setVisibility(View.VISIBLE);
//                mode = priceDriver.getVehicleType();
//            }

            editMinAmount.setText(priceDriver.getMinAmount());
            editPerKgAmount.setText(priceDriver.getPerKGAmount());

        } else {

            btnUpdate.setText("ADD NEW RATE");
            id = extras.getString("id");
            deliveryType = extras.getInt("item_type",0);
            tvDeliverySelect.setText(arraydelivary.get(deliveryType));
            tvFromLocation.setText(extras.getString("from_location"));
            tvToLocation.setText(extras.getString("to_location"));

//            if (extras.getInt("service_type") ==0 ) {
//                deliverySpinner.setVisibility(View.VISIBLE);
//                moverSpinner.setVisibility(View.GONE);
//                mode = String.valueOf(extras.getInt("delivery_type"));
//                serviceType = "delivery";
//
//            } else if (extras.getInt("service_type") == 1) {
//                deliverySpinner.setVisibility(View.GONE);
//                moverSpinner.setVisibility(View.VISIBLE);
//                mode = String.valueOf(extras.getInt("vehicle_type"));
//                serviceType = "mover";
//            }

        }

        /*editFromLocation.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
        editToLocation.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
*/
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, null));
//        editToLocation.setAdapter(new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, null));
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
        minAmount = editMinAmount.getText().toString();
        perKGAmount = editPerKgAmount.getText().toString();
        rateValidity = tvRateValidity.getText().toString();
        days = editDays.getText().toString();
        hours = editHours.getText().toString();

        currency = tvCurrency.getText().toString();
        if (fromLocation.equals("")) {
            Toast.makeText(this, "Please input From Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (toLocation.equals("")) {
            Toast.makeText(this, "Please input To Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (tvDeliverySelect.getText().toString().equals("")) {
            Toast.makeText(this, "Please select a Delivery type!", Toast.LENGTH_LONG).show();
            return;
        }

        if (minAmount.equals("")) {
            Toast.makeText(this, "Please input Min Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (rateValidity.equals("")) {
            Toast.makeText(this, "Please input validity!", Toast.LENGTH_LONG).show();
            return;
        }

        if (perKGAmount.equals("")) {
            Toast.makeText(this, "Please input Per Kg Amount!", Toast.LENGTH_LONG).show();
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

        duration = String.valueOf(Integer.valueOf(days) * 24 + Integer.valueOf(hours));

        //CommonUtils.showToast(this, "" + deliveryType);
         mController.getUpdateRateCard(rateCardType, token, deliveryType, fromLocation, toLocation, minAmount, perKGAmount, rateValidity, duration, selected_currency, id);

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
        if (tag.equals("delivery_dialog")) {
            deliveryType = position;
            tvDeliverySelect.setText(item);
        } else if (tag.equals("currency_dialog")) {
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
                if (TextUtils.isEmpty(tvCurrency.getText().toString())) {
                    argsCurrency.putInt("selected_position", 0);
                } else {
                    argsCurrency.putInt("selected_position", arrayCurrency.indexOf(tvCurrency.getText().toString()));
                }
                currencyListFragment.setArguments(argsCurrency);
                currencyListFragment.show(this.getSupportFragmentManager(), "currency_dialog");

                break;

            case R.id.tvCurrency:
                SingleChoiceActivitytDialog currencyList = new SingleChoiceActivitytDialog();
                Bundle args2 = new Bundle();
                args2.putString("title", "Currency");
                args2.putStringArrayList("list_array", arrayCurrency);
                if (TextUtils.isEmpty(tvCurrency.getText().toString())) {
                    args2.putInt("selected_position", 0);
                } else {
                    args2.putInt("selected_position", arrayCurrency.indexOf(tvCurrency.getText().toString()));
                }
                currencyList.setArguments(args2);
                currencyList.show(this.getSupportFragmentManager(), "currency_dialog");

                break;

            case R.id.tvRateValidity:
                DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
                datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_dialog");
                break;
            case R.id.sprDeliveryType:
                SingleChoiceActivitytDialog commodityListFragment = new SingleChoiceActivitytDialog();
                Bundle argsCommodity = new Bundle();
                argsCommodity.putString("title", "Courier");
                argsCommodity.putStringArrayList("list_array", arraydelivary);
                if (TextUtils.isEmpty(tvDeliverySelect.getText().toString())) {
                    argsCommodity.putInt("selected_position", 0);
                } else {
                    argsCommodity.putInt("selected_position", array_delivery.indexOf(tvDeliverySelect.getText().toString()));
                }
                commodityListFragment.setArguments(argsCommodity);
                commodityListFragment.show(getSupportFragmentManager(), "delivery_dialog");
                break;
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
