package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.add_rate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.OnFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.StorageSpacesActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.DatePickerFragmentDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.ashtechlabs.teleporter.util.SingleChoiceFragmentDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.gms.location.places.Places;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class WarehouseUpdateRateFragment extends Fragment implements UpdateRateWareHouseControllerCallback,
        SingleChoiceItemDialogListener, GoogleApiClient.OnConnectionFailedListener, OnDatePickListener,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    int warehouseId = -1;
    String warehouseName = "";
    private GoogleApiClient mGoogleApiClient;
    private AutocompleteSessionToken autocompleteSessionToken;
    private PlacesClient placesClient;
    private TextView editLocation;
    private EditText editPerCBMAmount, etInsurancePercentage, etMinInsuranceAmt;
    private TextView txtDateFrom, txtDateTo, tvRateValidity;
    private EditText editTotalCBM;
    private Button btnSubmit;
    private Button btnReset;
    private int selected_currency;
    private ArrayList<String> arrayCurrency;
    private IUpdateRateWareHouseController updateDriverController;
    private RadioButton rbCool, rbCold, rbFrozen;
    private CheckBox cbGeneral, cbHazardous, cbPerishable;
    private LinearLayout layRadiopersiable;
    private int additional_info = -1;
    private int perishable_det = 0;
    private TextView tvCurrency;
    private ImageView ivCurrency;
    private OnFragmentInteractionListener mListener;
    private boolean isGeneralAdded, isHazardousAdded, isPerishable;

    public WarehouseUpdateRateFragment() {
        // Required empty public constructor
    }


    public static WarehouseUpdateRateFragment newInstance() {

        return new WarehouseUpdateRateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), 200 /* clientId */, this)
//                .addApi(Places.GEO_DATA_API)
//                .build();
        autocompleteSessionToken= AutocompleteSessionToken.newInstance();
        placesClient= Places.createClient(getContext());
        arrayCurrency = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.array_currency)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.warehouse_uploadrate, container, false);
        initViews(view);
        updateDriverController = new UpdateRateWareHouseController(this);


        return view;
    }


    private void initViews(View view) {

        txtDateFrom = (TextView) view.findViewById(R.id.txtDateFrom);
        txtDateFrom.setOnClickListener(this);
        txtDateTo = (TextView) view.findViewById(R.id.txtDateTo);
        txtDateTo.setOnClickListener(this);

        editLocation = (TextView) view.findViewById(R.id.editLocation);
        editLocation.setOnClickListener(this);
        editPerCBMAmount = (EditText) view.findViewById(R.id.editPerCBMAmount);

        etInsurancePercentage = (EditText) view.findViewById(R.id.etInsurancePercentage);
        etMinInsuranceAmt = (EditText) view.findViewById(R.id.etMinInsuranceAmt);


        editTotalCBM = (EditText) view.findViewById(R.id.editTotalCBM);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);


        tvCurrency = (TextView) view.findViewById(R.id.tvCurrency);
        ivCurrency = (ImageView) view.findViewById(R.id.ivCurrency);
        tvCurrency.setOnClickListener(this);
        ivCurrency.setOnClickListener(this);

        etInsurancePercentage = (EditText) view.findViewById(R.id.etInsurancePercentage);
        etMinInsuranceAmt = (EditText) view.findViewById(R.id.etMinInsuranceAmt);
        tvRateValidity = (TextView) view.findViewById(R.id.tvRateValidity);
        tvRateValidity.setOnClickListener(this);

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

        //editLocation.setAdapter(new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, null, null));

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                uploadRateCard();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                clearData(true);
            }
        });

        //processOrder();
    }

//    private void processOrder() {
//
//        if (WareHouseDashBoardActivity.orderJson != null) {
//            try {
//                JSONObject jObj = new JSONObject(WareHouseDashBoardActivity.orderJson);
////                sprStorageType.setSelection(Integer.valueOf(jObj.getString("CommodityType")));
//                txtDateFrom.setText(jObj.getString("FromDate"));
//                txtDateTo.setText(jObj.getString("ToDate"));
//                editLocation.setText(jObj.getString("Location"));
//
//                WareHouseDashBoardActivity.orderJson = null;
//
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//
//        }
//    }

    private void uploadRateCard() {

        String location = editLocation.getText().toString();
        String perCBMAmount = editPerCBMAmount.getText().toString();
        String dateFrom = txtDateFrom.getText().toString();
        String dateTo = txtDateTo.getText().toString();
        String totalCBMAvailable = editTotalCBM.getText().toString();
        String currency = tvCurrency.getText().toString();
        String rateValidity = tvRateValidity.getText().toString();
        String insurancePercentage = etInsurancePercentage.getText().toString();
        String minInsuranceAmt = etMinInsuranceAmt.getText().toString();

        if (location.equals("")) {
            Toast.makeText(getActivity(), "Please input a Location!", Toast.LENGTH_LONG).show();
            return;
        }


        if (dateFrom.equals("")) {
            Toast.makeText(getActivity(), "Please input Date From", Toast.LENGTH_LONG).show();
            return;
        }

        if (dateTo.equals("")) {
            Toast.makeText(getActivity(), "Please input Date To", Toast.LENGTH_LONG).show();
            return;
        }

        if (warehouseId == -1) {
            Toast.makeText(getActivity(), "Please select a Warehouse", Toast.LENGTH_LONG).show();
            return;
        }


        if (additional_info == -1) {
            Toast.makeText(getActivity(), "Please select a storage type!", Toast.LENGTH_LONG).show();
            return;
        }

        if (additional_info == 2 && perishable_det == 0) {
            Toast.makeText(getActivity(), "Please select a perishable type!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(insurancePercentage)) {
            Toast.makeText(getActivity(), "Please input Insurance Percentage!", Toast.LENGTH_LONG).show();
            return;
        }

        if (minInsuranceAmt.equals("")) {
            Toast.makeText(getActivity(), "Please input Minimum amount of insurance!", Toast.LENGTH_LONG).show();
            return;
        }


        if (perCBMAmount.equals("")) {
            Toast.makeText(getActivity(), "Please input Per CBM Amount!", Toast.LENGTH_LONG).show();
            return;
        }

        if (totalCBMAvailable.equals("")) {
            Toast.makeText(getActivity(), "Please input Total CBM", Toast.LENGTH_LONG).show();
            return;
        }

        if (rateValidity.equals("")) {
            Toast.makeText(getActivity(), "Please input the Validity!", Toast.LENGTH_LONG).show();
            return;
        }
        if (currency.equals("")) {
            Toast.makeText(getActivity(), "Please select the Currency!", Toast.LENGTH_LONG).show();
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
            CommonUtils.showToast(getActivity(), "Select Storage type");
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

        updateDriverController.onUpdateWareHouseDriver(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""), warehouseId, perCBMAmount, dateFrom, dateTo, additional_info, perishable_det, minInsuranceAmt, insurancePercentage, rateValidity, totalCBMAvailable, selected_currency);

    }

    private void clearData(boolean clearLocation) {

        if(clearLocation){
            editLocation.setText("");
        }
        txtDateFrom.setText("");
        txtDateTo.setText("");
        etInsurancePercentage.setText("");
        etMinInsuranceAmt.setText("");
        editTotalCBM.setText("");
        editPerCBMAmount.setText("");
        tvRateValidity.setText("");

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
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onGetUpdateDriver(String customer) {
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

            case R.id.txtDateFrom:
                DialogFragment fromDatePick = new DatePickerFragmentDialog();
                fromDatePick.setTargetFragment(this, 202);
                fromDatePick.show(getActivity().getSupportFragmentManager(), "from_date_picker");
                break;
            case R.id.txtDateTo:
                if (TextUtils.isEmpty(txtDateFrom.getText().toString())) {
                    CommonUtils.showToast(getActivity(), "Select From date");
                    return;
                }
                DialogFragment toDatePick = new DatePickerFragmentDialog();
                Bundle args3 = new Bundle();
                args3.putString("from_date", txtDateFrom.getText().toString());
                toDatePick.setArguments(args3);
                toDatePick.setTargetFragment(this, 203);
                toDatePick.show(getActivity().getSupportFragmentManager(), "to_date_picker");
                break;
            case R.id.editLocation:
                Intent intent = new Intent(getActivity(), StorageSpacesActivity.class);
                intent.putExtra(Constants.KEY_WHICH_ACTIVITY, "rate_card");
                startActivityForResult(intent, 501);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 501) {

            warehouseId = data.getIntExtra(Constants.KEY_WARE_HOUSE_ID, -1);
            warehouseName = data.getStringExtra(Constants.KEY_WARE_HOUSE_NAME);

            editLocation.setText(warehouseName);

        }
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
        if (tag.equals("date_picker")) {
            tvRateValidity.setText(date);
        } else if (tag.equals("from_date_picker")) {
            txtDateFrom.setText(date);
        } else {
            txtDateTo.setText(date);
        }
    }
}
