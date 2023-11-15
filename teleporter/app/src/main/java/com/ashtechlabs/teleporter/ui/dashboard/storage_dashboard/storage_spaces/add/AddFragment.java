package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.add;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.OnStorageSpaceFragmentInteractionListener;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.DatePickerFragmentDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, OnDatePickListener, IAddFragmentControllerCallback {

    private static final int PLACE_PICKER_REQUEST = 100;

    private TextView tvAddWarehouseImage, tvUploadInsurance, tvExpiryInsurance, tvUploadTradeLicence, tvExpiryTradeLicence;
    private EditText etName, etCapacity, etSpaceAvailable;
    //private AutoCompleteTextView actLocation;
    private TextView tvLocation;
    private ImageView ivUploadInsurance, ivUploadWarehouseImage, ivUploadTradeLicence;
    private Button btAdd;
//    private GoogleApiClient mGoogleApiClient;
    private AutocompleteSessionToken autocompleteSessionToken;
    private PlacesClient placesClient;
    private IAddFragmentController iAddFragmentController;

    private String warehousePath, insurancePath, tradeLicencePath;
    private int selectedItem;

    private ArrayList<String> arrayCommodity;

    private OnStorageSpaceFragmentInteractionListener mListener;

    public AddFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayCommodity = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.array_commodity_copy)));
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), 500 /* clientId */, this)
//                .addApi(Places.GEO_DATA_API)
//                .build();
        //autocompleteSessionToken= AutocompleteSessionToken.newInstance();
        placesClient= Places.createClient(getContext());

        iAddFragmentController = new AddFragmentController(this);

        if (shouldAskPermissions()) {
            askPermissions();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.showFab(false);
        mListener.setToolbarTitle("Add Warehouse");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_storage_space_add, container, false);
        initView(view);
       // bindView();
        return view;
    }

//    private void bindView() {
////        actLocation.setAdapter(new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, null, null));
//        actLocation.setAdapter(new PlaceAutocompleteAdapterNew(getContext(), placesClient, autocompleteSessionToken));
//
//    }

    private void initView(View view) {
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        tvLocation.setOnClickListener(this);

        tvAddWarehouseImage = (TextView) view.findViewById(R.id.tvAddWarehouseImage);
        tvAddWarehouseImage.setOnClickListener(this);
        tvUploadInsurance = (TextView) view.findViewById(R.id.tvUploadInsurance);
        tvUploadInsurance.setOnClickListener(this);
        tvExpiryInsurance = (TextView) view.findViewById(R.id.tvExpiryInsurance);
        tvExpiryInsurance.setOnClickListener(this);
        tvUploadTradeLicence = (TextView) view.findViewById(R.id.tvUploadTradeLicence);
        tvUploadTradeLicence.setOnClickListener(this);
        tvExpiryTradeLicence = (TextView) view.findViewById(R.id.tvExpiryTradeLicence);
        tvExpiryTradeLicence.setOnClickListener(this);
        etName = (EditText) view.findViewById(R.id.etName);
        etCapacity = (EditText) view.findViewById(R.id.etCapacity);
        etSpaceAvailable = (EditText) view.findViewById(R.id.etSpaceAvailable);
        ivUploadInsurance = (ImageView) view.findViewById(R.id.ivUploadInsurance);
        ivUploadInsurance.setOnClickListener(this);
        ivUploadWarehouseImage = (ImageView) view.findViewById(R.id.ivUploadWarehouseImage);
        ivUploadWarehouseImage.setOnClickListener(this);
        ivUploadTradeLicence = (ImageView) view.findViewById(R.id.ivUploadTradeLicence);
        ivUploadTradeLicence.setOnClickListener(this);
        btAdd = (Button) view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStorageSpaceFragmentInteractionListener) {
            mListener = (OnStorageSpaceFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVehicleTypeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvLocation:
                callPlaceIntent();
                break;

            case R.id.btAdd:
                if (validateData()) {
                    //call add warehouse api
                    iAddFragmentController.addWareHouse(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""),
                           etCapacity.getText().toString(), etSpaceAvailable.getText().toString(), tvLocation.getText().toString(), etName.getText().toString(),
                            tvExpiryInsurance.getText().toString(), tvExpiryTradeLicence.getText().toString(),
                             warehousePath, tradeLicencePath, insurancePath);
               }
                break;
            case R.id.tvExpiryInsurance:
                DialogFragment insurancePicker = new DatePickerFragmentDialog();
                insurancePicker.setTargetFragment(this, 201);
                insurancePicker.show(getActivity().getSupportFragmentManager(), "expiry_insurance");
                break;
            case R.id.tvExpiryTradeLicence:
                DialogFragment tradeLicencePicker = new DatePickerFragmentDialog();
                tradeLicencePicker.setTargetFragment(this, 202);
                tradeLicencePicker.show(getActivity().getSupportFragmentManager(), "expiry_trade_licence");
                break;
            case R.id.ivUploadWarehouseImage:
                showUploadImagePopup(0);
                break;
            case R.id.ivUploadTradeLicence:
                showUploadImagePopup(1);
                break;
            case R.id.ivUploadInsurance:
                showUploadImagePopup(2);
                break;

        }
    }

    private void showUploadImagePopup(final int request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Take photo").setItems(
                R.array.upload_array_list,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent cameraIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent,
                                request);

                    }
                });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PLACE_PICKER_REQUEST) {

                Place place = Autocomplete.getPlaceFromIntent(data);

                Log.e("NAME", place.getName());
                Log.e("ADDRESS", place.getAddress());

                tvLocation.setText(place.getAddress());
            }else {

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                if (photo == null)
                    return;

                if (requestCode == 0) {
                    warehousePath = saveToSd(photo, "img_warehouse.jpg");
                    ivUploadWarehouseImage.setImageBitmap(photo);
                }

                if (requestCode == 1) {
                    tradeLicencePath = saveToSd(photo, "img_license_warehouse.jpg");
                    ivUploadTradeLicence.setImageBitmap(photo);
                }

                if (requestCode == 2) {
                    insurancePath = saveToSd(photo, "img_insurance_warehouse.jpg");
                    ivUploadInsurance.setImageBitmap(photo);
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

    private void callPlaceIntent() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    private boolean validateData() {

        if (!checkEditTextNotEmpty(etName)) {
            CommonUtils.showToast(getActivity(), "Enter a Name");
            return false;
        }
//        else if (!checkAutoCompleteTextViewNotEmpty(actLocation)) {
//            CommonUtils.showToast(getActivity(), "Select warehouse location");
//            return false;
//        }
        else if (!checkTextViewNotEmpty(tvLocation)) {
            CommonUtils.showToast(getActivity(), "Select warehouse location");
            return false;
        } else if (!checkTextNotEmpty(warehousePath)) {
            CommonUtils.showToast(getActivity(), "Upload warehouse image");
            return false;
        }  else if (!checkEditTextNotEmpty(etCapacity)) {
            CommonUtils.showToast(getActivity(), "Enter warehouse capacity");
            return false;
        } else if (!checkEditTextNotEmpty(etSpaceAvailable)) {
            CommonUtils.showToast(getActivity(), "Enter space available");
            return false;
        }else if (!checkTextNotEmpty(insurancePath)) {
            CommonUtils.showToast(getActivity(), "Upload insurance");
            return false;
        }else if (!checkTextViewNotEmpty(tvExpiryInsurance)) {
            CommonUtils.showToast(getActivity(), "Enter insurance expiry date");
            return false;
        } else if (!checkTextNotEmpty(tradeLicencePath)) {
            CommonUtils.showToast(getActivity(), "Upload trade licence");
            return false;
        }  else if (!checkTextViewNotEmpty(tvExpiryTradeLicence)) {
            CommonUtils.showToast(getActivity(), "Enter trade licence expiry date");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTextViewNotEmpty(TextView textView) {
        if (textView.getText().toString().trim().matches("")) {
            return false;
        } else {
            return true;
        }
    }
    private boolean checkAutoCompleteTextViewNotEmpty(AutoCompleteTextView textView) {
        if (textView.getText().toString().trim().matches("")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkEditTextNotEmpty(EditText textView) {
        if (textView.getText().toString().trim().matches("")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTextNotEmpty(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setDate(String date, String tag) {

        if (tag.equals("expiry_insurance")) {
            tvExpiryInsurance.setText(date);
        }
        if (tag.equals("expiry_trade_licence")) {
            tvExpiryTradeLicence.setText(date);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String saveToSd(Bitmap bm, String filename) {

        if (bm != null) {
            File sd = Environment.getExternalStorageDirectory();
            Log.d("pathsd", " " + sd.getAbsolutePath().toString());
            File dest = new File(sd, filename);
            Log.d("pathdest", " " + dest.getAbsolutePath().toString());
            try {
                FileOutputStream out = new FileOutputStream(dest);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Environment.getExternalStorageDirectory().toString() + File.separator + filename;
        }
        return "";
    }




    @Override
    public void warehouseAdded(String message) {
        CommonUtils.showToast(getActivity(), message);
    }

    @Override
    public void showLoadingDialog(boolean show) {
        mListener.showProgressIndicator(show);
    }
}
