package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.add_rate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapter;
import com.ashtechlabs.teleporter.adapters.PlaceAutocompleteAdapterNew;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.OnFragmentInteractionListener;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.DatePickerFragmentDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.ashtechlabs.teleporter.util.SingleChoiceFragmentDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class UpdateRateVendorsFragment extends Fragment implements UpdateRateVendorsControllerCallback, SingleChoiceItemDialogListener,
        GoogleApiClient.OnConnectionFailedListener, OnDatePickListener,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int PLACE_PICKER_REQUEST_FROM = 100;
    private static final int PLACE_PICKER_REQUEST_TO = 200;
//    private GoogleApiClient mGoogleApiClient;
//    private AutoCompleteTextView editFromLocation;
//    private AutoCompleteTextView editToLocation;
    private TextView tvFromLocation;
    private TextView tvToLocation;
    //RelativeLayout sprServiceType;
    private EditText editMinAmount, editPerKgAmount, etInsurancePercentage, etMinInsuranceAmt;
    private EditText editDays;
    private EditText editHours;
    private int selected_currency;
    private int additional_info = -1;
    private int perishable_det = 0;
    private RadioButton rbDoorToDoor, rbPortToPort, rbDoorToPort, rbPortToDoor;
    private ArrayList<String> arrayCurrency;
    private Button btnSubmit, btnReset;
    private String serviceType = "0";
    private IUpdateRateVendorsController IUpdateRateVendorsController;
    private TextView tvCurrency, tvRateValidity;
    private ImageView ivCurrency;
    private CheckBox cbHazardous, cbPerishable, cbGeneral;
    private RadioButton rbCool, rbCold, rbFrozen;
    private LinearLayout layRadiopersiable;
    private OnFragmentInteractionListener mListener;
    //private RadioButton rb_single, rb_regular;
    //private int shipmentOption = 0;
    private boolean isGeneralAdded, isHazardousAdded, isPerishable;
   // private AutocompleteSessionToken autocompleteSessionToken;
    private PlacesClient placesClient;

    public UpdateRateVendorsFragment() {
        // Required empty public constructor
    }


    public static UpdateRateVendorsFragment newInstance() {
        UpdateRateVendorsFragment fragment = new UpdateRateVendorsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayCurrency = new ArrayList(Arrays.asList(getActivity().getResources().getStringArray(R.array.array_currency)));


//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), 200 /* clientId */, this)
//                .addApi(Places.GEO_DATA_API)
//                .build();

//        autocompleteSessionToken= AutocompleteSessionToken.newInstance();
        placesClient= Places.createClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partner_upload_rate, container, false);
        initViews(view);
        IUpdateRateVendorsController = new UpdateRateVendorsController(this);
        return view;
    }

//    private void processOrder() {
//        if (CargoDashBoardActivity.orderJson != null) {
//            try {
//                JSONObject jObj = new JSONObject(CargoDashBoardActivity.orderJson);
//
////                sprServiceType.setSelection(Integer.valueOf(jObj.getString("CommodityType")));
//                editFromLocation.setText(jObj.getString("FromLocation"));
//                editToLocation.setText(jObj.getString("ToLocation"));
//
//                CargoDashBoardActivity.orderJson = null;
//
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//        }
//    }

    private void uploadRateCard() {
        String fromLocation = tvFromLocation.getText().toString();
        String toLocation = tvToLocation.getText().toString();
        String minAmount = editMinAmount.getText().toString();
        String perKGAmount = editPerKgAmount.getText().toString();
        String days = editDays.getText().toString();
        String hours = editHours.getText().toString();
        String currency = tvCurrency.getText().toString();
        String rateValidity = tvRateValidity.getText().toString();
        String insurancePercentage = etInsurancePercentage.getText().toString();
        String minInsuranceAmt = etMinInsuranceAmt.getText().toString();


        if (fromLocation.equals("")) {
            Toast.makeText(getActivity(), "Please input From Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (toLocation.equals("")) {
            Toast.makeText(getActivity(), "Please input To Location!", Toast.LENGTH_LONG).show();
            return;
        }

        if (minAmount.equals("")) {
            Toast.makeText(getActivity(), "Please input Min Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(minAmount) == 0) {
            Toast.makeText(getActivity(), "Please input valid Min Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (perKGAmount.equals("")) {
            Toast.makeText(getActivity(), "Please input Per Kg Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(perKGAmount) == 0) {
            Toast.makeText(getActivity(), "Please input valid Per Kg Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (rateValidity.equals("")) {
            Toast.makeText(getActivity(), "Please input validity of rate card!", Toast.LENGTH_LONG).show();
            return;
        }

        if (days.equals("")) {
            Toast.makeText(getActivity(), "Please input the days!", Toast.LENGTH_LONG).show();
            return;
        }


        if (hours.equals("")) {
            Toast.makeText(getActivity(), "Please input hours!", Toast.LENGTH_LONG).show();
            return;
        }


        if (Integer.parseInt(days) == 0 && Integer.parseInt(hours) == 0) {
            Toast.makeText(getActivity(), "Please input a transit day or hour", Toast.LENGTH_LONG).show();
            return;
        }

        if (currency.equals("")) {
            Toast.makeText(getActivity(), "Please input the currency!", Toast.LENGTH_LONG).show();
            return;
        }

        if (cbGeneral.isChecked()) {
            additional_info = 0;
            isGeneralAdded = true;
        } else if (cbHazardous.isChecked()) {
            additional_info = 1;
            isHazardousAdded = true;
        } else if (cbPerishable.isChecked()) {
            additional_info = 2;
            isPerishable = true;
        } else {
            CommonUtils.showToast(getActivity(), "Select Cargo type");
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
                CommonUtils.showToast(getActivity(), "Select Perishable type");
                perishable_det = 0;
                return;
            }
        }

//        if(rb_single.isChecked()){
//            shipmentOption=0;
//        }else{
//            shipmentOption=1;
//        }

        String duration = String.valueOf(Integer.valueOf(days) * 24 + Integer.valueOf(hours));
        IUpdateRateVendorsController.onUpdateVendor(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""),
                serviceType, fromLocation, toLocation, additional_info, perishable_det, minAmount, perKGAmount, rateValidity, insurancePercentage, minInsuranceAmt, duration, selected_currency);

    }

    private void clearData(boolean clearLocation) {
        if (clearLocation) {
            tvFromLocation.setText("");
            tvToLocation.setText("");
        }
        editMinAmount.setText("");
        editPerKgAmount.setText("");
        tvCurrency.setText("");
        tvRateValidity.setText("");
        etMinInsuranceAmt.setText("");
        etInsurancePercentage.setText("");
        editDays.setText("");
        editHours.setText("");
    }

    private void initViews(View view) {
        //air = (ImageView) view.findViewById(R.id.air);
        rbDoorToDoor = (RadioButton) view.findViewById(R.id.rbDoorToDoor);
        rbDoorToDoor.setOnCheckedChangeListener(this);
        //cargo = (ImageView) view.findViewById(R.id.cargo);
        rbPortToPort = (RadioButton) view.findViewById(R.id.rbPortToPort);
        rbPortToPort.setOnCheckedChangeListener(this);
        //truck = (ImageView) view.findViewById(R.id.truck);
        rbDoorToPort = (RadioButton) view.findViewById(R.id.rbDoorToPort);
        rbDoorToPort.setOnCheckedChangeListener(this);
        //trolley = (ImageView) view.findViewById(R.id.trolley);
        rbPortToDoor = (RadioButton) view.findViewById(R.id.rbPortToDoor);
        rbPortToDoor.setOnCheckedChangeListener(this);

//        rb_single = (RadioButton) view.findViewById(R.id.rb_single);
//        rb_regular = (RadioButton) view.findViewById(R.id.rb_regular);

//        editFromLocation = (AutoCompleteTextView) view.findViewById(R.id.editFromLocation);
//        editToLocation = (AutoCompleteTextView) view.findViewById(R.id.editToLocation);
        tvFromLocation = (TextView) view.findViewById(R.id.tvFromLocation);
        tvFromLocation.setOnClickListener(this);
        tvToLocation = (TextView) view.findViewById(R.id.tvToLocation);
        tvToLocation.setOnClickListener(this);
        //sprServiceType = (RelativeLayout) view.findViewById(R.id.sprServiceType);
        //ServiceType = (TextView) view.findViewById(R.id.ServiceType);


        editMinAmount = (EditText) view.findViewById(R.id.editMinAmount);
        editPerKgAmount = (EditText) view.findViewById(R.id.editPerKgAmount);

        etInsurancePercentage = (EditText) view.findViewById(R.id.etInsurancePercentage);
        etMinInsuranceAmt = (EditText) view.findViewById(R.id.etMinInsuranceAmt);
        tvRateValidity = (TextView) view.findViewById(R.id.tvRateValidity);
        tvRateValidity.setOnClickListener(this);

        tvCurrency = (TextView) view.findViewById(R.id.tvCurrency);
        ivCurrency = (ImageView) view.findViewById(R.id.ivCurrency);
        tvCurrency.setOnClickListener(this);
        ivCurrency.setOnClickListener(this);
        editDays = (EditText) view.findViewById(R.id.editDays);
        editHours = (EditText) view.findViewById(R.id.editHours);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        rbCool = (RadioButton) view.findViewById(R.id.rbCool);
        rbCool.setOnCheckedChangeListener(this);
        rbCold = (RadioButton) view.findViewById(R.id.rbCold);
        rbCold.setOnCheckedChangeListener(this);
        rbFrozen = (RadioButton) view.findViewById(R.id.rbFrozen);
        rbFrozen.setOnCheckedChangeListener(this);

        layRadiopersiable = (LinearLayout) view.findViewById(R.id.layRadiopersiable);
        cbHazardous = (CheckBox) view.findViewById(R.id.cbHazardous);
        cbGeneral = (CheckBox) view.findViewById(R.id.cbGeneral);
        cbPerishable = (CheckBox) view.findViewById(R.id.cbPerishable);
        cbGeneral.setOnCheckedChangeListener(this);
        cbPerishable.setOnCheckedChangeListener(this);
        cbHazardous.setOnCheckedChangeListener(this);


//        editToLocation.setAdapter(new PlaceAutocompleteAdapterNew(getContext(), placesClient, autocompleteSessionToken));
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapterNew(getContext(), placesClient, autocompleteSessionToken));
//        editToLocation.setAdapter(new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, null, null));
//        editFromLocation.setAdapter(new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, null, null));
        //processOrder();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

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
                    getActivity(), "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            Log.i("ERROR", status.getStatusMessage());
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public void onGetUpdateVendor(String customer) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(customer);
            Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (!isGeneralAdded) {
            showAlert("Do you need to update rate card for General", cbGeneral);
        } else if (!isHazardousAdded) {
            showAlert("Do you need to update rate card for Hazardous", cbHazardous);
        } else if (!isPerishable) {
            showAlert("Do you need to update rate card for Perishable", cbPerishable);
        } else {
            clearData(true);
        }
    }

    private void showAlert(String message, final CheckBox checkbox) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        checkbox.setChecked(true);
                        clearData(false);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        clearData(true);
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("");
        alert.show();
    }


    @Override
    public void showMessage(String message) {
        CommonUtils.showToast(getActivity(), message);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setOnSelectItem(String item, int position, String tag) {
        if (tag.equals("currency_dialog")) {
            tvCurrency.setText(item);
            selected_currency = position;
        }
    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (show) {
            mListener.showLoadingIndicator();
        } else {
            mListener.dismissLoadingIndicator();
        }
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
                clearData(true);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cbGeneral) {
            if (isChecked) {
                cbPerishable.setChecked(false);
                cbHazardous.setChecked(false);
                layRadiopersiable.setVisibility(View.GONE);
                additional_info = 0;
            }

        } else if (buttonView.getId() == R.id.cbHazardous) {
            if (isChecked) {
                cbGeneral.setChecked(false);
                cbPerishable.setChecked(false);
                layRadiopersiable.setVisibility(View.GONE);
                additional_info = 1;
            }
        } else if (buttonView.getId() == R.id.cbPerishable) {
            if (isChecked) {
                cbGeneral.setChecked(false);
                cbHazardous.setChecked(false);
                layRadiopersiable.setVisibility(View.VISIBLE);
                additional_info = 2;
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

    @Override
    public void setDate(String date, String tag) {
        tvRateValidity.setText(date);
    }
}

