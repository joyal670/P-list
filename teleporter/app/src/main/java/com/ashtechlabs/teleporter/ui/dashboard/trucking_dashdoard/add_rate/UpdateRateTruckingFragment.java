package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.add_rate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapter;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapterNew;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.OnTDashFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.VehicleTypeActivity;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.DatePickerFragmentDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.ashtechlabs.teleporter.util.SingleChoiceFragmentDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.places.Places;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IROID_ANDROID1 on 07-Feb-17.
 */

public class UpdateRateTruckingFragment extends Fragment implements UpdateRateTruckingControllerCallback,
        SingleChoiceItemDialogListener, GoogleApiClient.OnConnectionFailedListener, OnDatePickListener, View.OnClickListener {

    private static final int PLACE_PICKER_REQUEST_FROM = 100;
    private static final int PLACE_PICKER_REQUEST_TO = 200;
//    public GoogleApiClient mGoogleApiClient;
//    private AutocompleteSessionToken autocompleteSessionToken;
    private PlacesClient placesClient;
    int selected_currency;
    ArrayList<String> arrayCurrency;
//    private AutoCompleteTextView editFromLocation;
//    private AutoCompleteTextView editToLocation;

    private TextView tvFromLocation;
    private TextView tvToLocation;
    private EditText editAmount;
    private EditText editPerKgAmount;
    private EditText editDays;
    private EditText editHours;
    private EditText etInsurancePercentage, etMinInsurance;
    private Button btnSubmit;
    private Button btnReset;
    private TextView tvCurrency, tvVechicleType, tvRateValidity;
    private ImageView ivCurrency;
    private IUpdateRateTruckingController IUpdateRateTruckingController;
    private String vehicleType = "-1";
    private String vehicleSubType = "-1";

    private String driverSelect;

    private OnTDashFragmentInteractionListener mListener;

    public UpdateRateTruckingFragment() {
        // Required empty public constructor
    }


    public static UpdateRateTruckingFragment newInstance() {
        return new UpdateRateTruckingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayCurrency = new ArrayList(Arrays.asList(getActivity().getResources().getStringArray(R.array.array_currency)));

//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), 200 /* clientId */, this)
//                .addApi(Places.GEO_DATA_API)
//                .build();
 //       autocompleteSessionToken= AutocompleteSessionToken.newInstance();
        placesClient= Places.createClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.trucking_update_rate, container, false);
        initViews(view);
        IUpdateRateTruckingController = new UpdateRateTruckingController(this);
        return view;
    }

    private void initViews(View view) {

        tvFromLocation = (TextView) view.findViewById(R.id.tvFromLocation);
        tvFromLocation.setOnClickListener(this);
        tvToLocation = (TextView) view.findViewById(R.id.tvToLocation);
        tvToLocation.setOnClickListener(this);
//        radio_delivery = (RadioButton) view.findViewById(R.id.radio_delivery);
//        radio_mover = (RadioButton) view.findViewById(R.id.radio_mover);
        editAmount = (EditText) view.findViewById(R.id.editAmount);
        editPerKgAmount = (EditText) view.findViewById(R.id.editPerKgAmount);
        etInsurancePercentage = (EditText) view.findViewById(R.id.etInsurancePercentage);
        etMinInsurance = (EditText) view.findViewById(R.id.etMinInsurance);
        editDays = (EditText) view.findViewById(R.id.editDays);
        editHours = (EditText) view.findViewById(R.id.editHours);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        tvVechicleType = (TextView) view.findViewById(R.id.tvVechicleType);
        tvCurrency = (TextView) view.findViewById(R.id.tvCurrency);
        tvRateValidity = (TextView) view.findViewById(R.id.tvRateValidity);
        tvRateValidity.setOnClickListener(this);
        ivCurrency = (ImageView) view.findViewById(R.id.ivCurrency);
        tvCurrency.setOnClickListener(this);
        ivCurrency.setOnClickListener(this);


        tvVechicleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VehicleTypeActivity.class);
                startActivityForResult(intent, 501);
            }
        });



       /* editFromLocation.setAdapter(new GooglePlacesAdapter(getActivity(), R.layout.list_item));
        editToLocation.setAdapter(new GooglePlacesAdapter(getActivity(), R.layout.list_item));*/
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, null, null));
//        editToLocation.setAdapter(new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, null, null));
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapterNew(getContext(), placesClient, autocompleteSessionToken));
//        editToLocation.setAdapter(new PlaceAutocompleteAdapterNew(getContext(), placesClient, autocompleteSessionToken));

        // processOrder();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 501) {

                vehicleType = data.getStringExtra(Constants.KEY_VEHICLE_TYPE);
                vehicleSubType = data.getStringExtra(Constants.KEY_VEHICLE_SUB_TYPE);

                Log.e("TAG", "vehicleType "+vehicleType );
                Log.e("TAG", "vehicleSubType "+vehicleSubType );

//            if ((TextUtils.isEmpty(vehicleType)))
//                vehicleType = "-1";
//
//            if ((TextUtils.isEmpty(vehicleSubType)))
//                vehicleSubType = "1";

                tvVechicleType.setText(data.getStringExtra(Constants.KEY_VEHICLE_TYPE_NAME));

                //CommonUtils.showToast(getActivity(), "" + vehicleType + "-" + vehicleTypeName + " || " + vehicleSubType + "-" + vehicleSubTypeName);
            }else{

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


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(
                    getActivity(), "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            Log.i("ERROR", status.getStatusMessage());
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTDashFragmentInteractionListener) {
            mListener = (OnTDashFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTDashFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void uploadRateCard() {

        String fromLocation = tvFromLocation.getText().toString();
        String toLocation = tvToLocation.getText().toString();
        String minAmount = editAmount.getText().toString();
        String rateValidity = tvRateValidity.getText().toString();
        String insurancePercentage = etInsurancePercentage.getText().toString();
        String insuranceMinAmt = etMinInsurance.getText().toString();
        String days = editDays.getText().toString();
        String hours = editHours.getText().toString();
        String currency = tvCurrency.getText().toString();

        if (fromLocation.equals("")) {
            Toast.makeText(getActivity(), "Please input From Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (toLocation.equals("")) {
            Toast.makeText(getActivity(), "Please input To Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (minAmount.equals("")) {
            Toast.makeText(getActivity(), "Please input Delivery Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(minAmount) == 0) {
            Toast.makeText(getActivity(), "Please input a valid Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (days.equals("")) {
            Toast.makeText(getActivity(), "Please input the days!", Toast.LENGTH_LONG).show();
            return;
        }

        if (hours.equals("")) {
            Toast.makeText(getActivity(), "Please input the hours!", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(days) == 0 && Integer.parseInt(hours) == 0) {
            Toast.makeText(getActivity(), "Please input a transit day or hour", Toast.LENGTH_LONG).show();
            return;
        }

        if (insurancePercentage.equals("")) {
            Toast.makeText(getActivity(), "Please input the insurance Percentage!", Toast.LENGTH_LONG).show();
            return;
        }

        if (insuranceMinAmt.equals("")) {
            Toast.makeText(getActivity(), "Please input Minimum amount of Insurance!", Toast.LENGTH_LONG).show();
            return;
        }

        if (currency.equals("")) {
            Toast.makeText(getActivity(), "Please select the currency!", Toast.LENGTH_LONG).show();
            return;
        }


        String duration = String.valueOf(Integer.valueOf(days) * 24 + Integer.valueOf(hours));

        Log.e("TAG", "uploadRateCard: "+duration );

        Log.e("###SERvice", vehicleSubType);
        Log.e("###SERvice", vehicleType);

        Log.e("TAG", "uploadRateCard: "+ selected_currency);

         IUpdateRateTruckingController.onUpdateTruckingRateCard(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), Integer.parseInt(vehicleType), Integer.parseInt(vehicleSubType), fromLocation, toLocation, minAmount, rateValidity, insurancePercentage, insuranceMinAmt, duration, selected_currency);

    }

    private void formatData() {
        tvFromLocation.setText("");
        tvToLocation.setText("");
        editAmount.setText("");
        tvVechicleType.setText("");
        tvRateValidity.setText("");
        tvCurrency.setText("");
        editDays.setText("");
        editHours.setText("");
        etInsurancePercentage.setText("");
        etMinInsurance.setText("");
    }

    @Override
    public void onGetUpdateDriver(String customer) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(customer);
            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
            formatData();
        } catch (JSONException e) {
            e.printStackTrace();
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
        mListener.showLoadingIndicator();
    }

    @Override
    public void dismissLoadingIndicator() {
        mListener.dismissLoadingIndicator();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

                SingleChoiceFragmentDialog currencyListFragment = new SingleChoiceFragmentDialog();
                Bundle argsCurrency = new Bundle();
                argsCurrency.putString("title", "Currency");
                argsCurrency.putStringArrayList("list_array", arrayCurrency);
                if (TextUtils.isEmpty(tvCurrency.getText().toString())) {
                    argsCurrency.putInt("selected_position", 0);
                } else {
                    argsCurrency.putInt("selected_position", arrayCurrency.indexOf(tvCurrency.getText().toString()));
                }
                currencyListFragment.setArguments(argsCurrency);
                currencyListFragment.setTargetFragment(this, 1);
                currencyListFragment.show(getActivity().getSupportFragmentManager(), "currency_dialog");

                break;

            case R.id.tvCurrency:
                SingleChoiceFragmentDialog currencyList = new SingleChoiceFragmentDialog();
                Bundle args2 = new Bundle();
                args2.putString("title", "Currency");
                args2.putStringArrayList("list_array", arrayCurrency);
                if (TextUtils.isEmpty(tvCurrency.getText().toString())) {
                    args2.putInt("selected_position", 0);
                } else {
                    args2.putInt("selected_position", arrayCurrency.indexOf(tvCurrency.getText().toString()));
                }
                currencyList.setArguments(args2);
                currencyList.setTargetFragment(this, 1);
                currencyList.show(getActivity().getSupportFragmentManager(), "currency_dialog");

                break;
            case R.id.tvRateValidity:
                DialogFragment toDatePicker = new DatePickerFragmentDialog();
                toDatePicker.setTargetFragment(this, 201);
                toDatePicker.show(getActivity().getSupportFragmentManager(), "date_picker");
                break;
            case R.id.btnReset:
                formatData();
                break;
            case R.id.btnSubmit:
                uploadRateCard();
                break;
        }
    }

    private void callPlaceIntent(int requestCode) {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setDate(String date, String tag) {
        tvRateValidity.setText(date);
    }
}
