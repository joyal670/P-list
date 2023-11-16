package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.add;

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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.OnVehicleTypeFragmentInteractionListener;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.type.VehicleSelectActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.DatePickerFragmentDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;


public class AddFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, OnDatePickListener, IAddFragmentControllerCallback {


    private TextView tvVehicleType, tvUploadInsurance, tvExpiryInsurance;
    private EditText etNumber;
    private ImageView ivUploadInsurance;
    private Button btAdd;
    private IAddFragmentController iAddFragmentController;

    private String insurancePath;
    private int vehicleType = -1;
    private String vehicleTypeName = "";
    private int vehicleSubType = 1;
    private String vehicleSubTypeName = "";

    private OnVehicleTypeFragmentInteractionListener mListener;

    public AddFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance() {
        return new AddFragment();
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

        if (getArguments() != null) {
            vehicleType = getArguments().getInt(Constants.KEY_VEHICLE_TYPE);
            vehicleTypeName = getArguments().getString(Constants.KEY_VEHICLE_TYPE_NAME);
            vehicleSubType = getArguments().getInt(Constants.KEY_VEHICLE_SUB_TYPE);
            vehicleSubTypeName = getArguments().getString(Constants.KEY_VEHICLE_SUB_TYPE_NAME);
        }
        iAddFragmentController = new AddFragmentController(this);

        if (shouldAskPermissions()) {
            askPermissions();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_type_add, container, false);
        initView(view);
        bindView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.showFab(false);

    }

    private void bindView() {

    }

    private void initView(View view) {

        tvVehicleType = (TextView) view.findViewById(R.id.tvVehicleType);
        tvVehicleType.setOnClickListener(this);
        tvUploadInsurance = (TextView) view.findViewById(R.id.tvUploadInsurance);
        tvUploadInsurance.setOnClickListener(this);
        tvExpiryInsurance = (TextView) view.findViewById(R.id.tvExpiryInsurance);
        tvExpiryInsurance.setOnClickListener(this);
        etNumber = (EditText) view.findViewById(R.id.etNumber);
        ivUploadInsurance = (ImageView) view.findViewById(R.id.ivUploadInsurance);
        ivUploadInsurance.setOnClickListener(this);
        btAdd = (Button) view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVehicleTypeFragmentInteractionListener) {
            mListener = (OnVehicleTypeFragmentInteractionListener) context;
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
            case R.id.btAdd:
                if (validateData()) {
                    //call add warehouse api
                    iAddFragmentController.addVehicle(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), vehicleType, vehicleSubType, etNumber.getText().toString(),
                            insurancePath, tvExpiryInsurance.getText().toString());
                }
                break;
            case R.id.tvExpiryInsurance:
                DialogFragment insurancePicker = new DatePickerFragmentDialog();
                insurancePicker.setTargetFragment(this, 201);
                insurancePicker.show(getActivity().getSupportFragmentManager(), "expiry_insurance");
                break;
            case R.id.ivUploadInsurance:
                showUploadImagePopup(1);
                break;
            case R.id.tvVehicleType:
                Intent intent = new Intent(getActivity(), VehicleSelectActivity.class);
                startActivityForResult(intent, 501);
                break;


        }
    }


    private void showUploadImagePopup(final int request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Take photo").setItems(
                R.array.upload_array_list,
                new DialogInterface.OnClickListener() {
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

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 1) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            if (photo == null)
                return;

            insurancePath = saveToSd(photo, "img_insurance_truck.jpg");
            ivUploadInsurance.setImageBitmap(photo);
        }

        if (requestCode == 501) {

            vehicleType = data.getIntExtra(Constants.KEY_VEHICLE_TYPE, -1);
            vehicleTypeName = data.getStringExtra(Constants.KEY_VEHICLE_TYPE_NAME);
            vehicleSubType = data.getIntExtra(Constants.KEY_VEHICLE_SUB_TYPE, -1);
            vehicleSubTypeName = data.getStringExtra(Constants.KEY_VEHICLE_SUB_TYPE_NAME);

            tvVehicleType.setText(vehicleTypeName + "-" + vehicleSubTypeName);

            CommonUtils.showToast(getActivity(), "" + vehicleType + "-" + vehicleTypeName + " || " + vehicleSubType + "-" + vehicleSubTypeName);
        }
    }

    private boolean validateData() {
        if (!checkTextViewNotEmpty(tvVehicleType)) {
            CommonUtils.showToast(getActivity(), "Select a vehicle type");
            return false;
        } else if (!checkEditTextNotEmpty(etNumber)) {
            CommonUtils.showToast(getActivity(), "Enter a vehicle number");
            return false;
        } else if (!checkTextNotEmpty(insurancePath)) {
            CommonUtils.showToast(getActivity(), "Upload insurance");
            return false;
        } else if (!checkTextViewNotEmpty(tvExpiryInsurance)) {
            CommonUtils.showToast(getActivity(), "Enter Insurance expiry date");
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
    public void vehicleAdded(String message) {
        CommonUtils.showToast(getActivity(), message);
    }

    @Override
    public void showErrorMessage(String message) {
        CommonUtils.showToast(getActivity(), message);
    }

    @Override
    public void showLoadingDialog(boolean show) {
        mListener.showProgressIndicator(show);
    }
}
