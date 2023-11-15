package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfile;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfileDetail;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.VehicleTypeActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.DatePickerActivityDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */
public class TruckingProfileActivity extends BaseActivity implements OnDatePickListener,
        TruckingGetProfileControllerCallBack, UpdateProfileControllerCallback, View.OnClickListener {

    public ArrayList<TruckingProfileDetail> mProfileDetails;
    AutoCompleteTextView editCity;
    ScrollView container;
    EditText editName;
    EditText editCompanyAddress, editPhone, editEmail;
    EditText editZipCode;
    ImageView img_license;
    TextView txtLicenseExpiredate, tvAddVehicleType;
    ImageView img_profile;
    String img_license_path = "";
    String img_profile_path = "";
    ITruckingGetProfileController ITruckingGetProfileController;
    IUpdateTruckingProfileController IUpdateTruckingProfileController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucking_profile);
        setUpToolbar();
        initUI();

        IUpdateTruckingProfileController = new UpdateProfileController(this);
        ITruckingGetProfileController = new TruckingGetProfileController(this);
        ITruckingGetProfileController.onGetProfileDetails(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));

    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Account");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initUI() {

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String strCity = editCity.getText().toString();
                String strName = editName.getText().toString();
                String strCompanyAddress = editCompanyAddress.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                String strZipCode = editZipCode.getText().toString();
                String str_license_exp = txtLicenseExpiredate.getText().toString();

                if (strCity.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the city", Toast.LENGTH_LONG).show();

                } else if (strName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the Name", Toast.LENGTH_LONG).show();

                } else if (strCompanyAddress.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the Address", Toast.LENGTH_LONG).show();

                }else if (phone.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the Phone Number", Toast.LENGTH_LONG).show();

                }else if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the Email", Toast.LENGTH_LONG).show();

                } else if (strZipCode.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the zipcode", Toast.LENGTH_LONG).show();

                } else if (img_profile_path.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Upload Company profile", Toast.LENGTH_LONG).show();

                } else if (img_license_path.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Upload Trade Licence", Toast.LENGTH_LONG).show();

                } else if (str_license_exp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input the license expiry date", Toast.LENGTH_LONG).show();

                } else {
                    IUpdateTruckingProfileController.onUpdateProfileController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""), strCity, strName, strCompanyAddress,phone, email, strZipCode, img_profile_path, img_license_path, str_license_exp);
                }
            }
        });

        container = (ScrollView) findViewById(R.id.container);
        editCity = (AutoCompleteTextView) findViewById(R.id.editCity);
        editName = (EditText) findViewById(R.id.editName);
        editCompanyAddress = (EditText) findViewById(R.id.editCompanyAddress);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editZipCode = (EditText) findViewById(R.id.editZipCode);
        img_license = (ImageView) findViewById(R.id.img_license);
        img_license.setOnClickListener(this);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_profile.setOnClickListener(this);
        tvAddVehicleType = (TextView) findViewById(R.id.tvAddVehicleType);
        tvAddVehicleType.setOnClickListener(this);
        txtLicenseExpiredate = (TextView) findViewById(R.id.txtLicenseExpiredate);
        txtLicenseExpiredate.setOnClickListener(this);


    }


    private void showUploadImagePopup(final int request) {
        if (checkPermission())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick Image").setItems(
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
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(TruckingProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(TruckingProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(TruckingProfileActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(TruckingProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        Bitmap photo = null;
        photo = (Bitmap) data.getExtras().get("data");

        if (requestCode == 0) {
            img_profile_path = saveToSd(photo, "img_profile_trucking.jpg");
            img_profile.setImageBitmap(photo);
        }

        if (requestCode == 1) {
            img_license_path = saveToSd(photo, "img_trade_licence_trucking.jpg");
            img_license.setImageBitmap(photo);
        }


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
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + filename;
            return path;
        }
        return "";
    }


    @Override
    public void onGetDriverProfileDetails(TruckingProfile profile) {

        mProfileDetails = profile.getProfile();
        editCity.setText(mProfileDetails.get(0).getCityOfWork());
        editName.setText(mProfileDetails.get(0).getName());
        editCompanyAddress.setText(mProfileDetails.get(0).getCompanyAddress());
        editZipCode.setText(mProfileDetails.get(0).getZipCode());
        editPhone.setText(mProfileDetails.get(0).getContactNumber());
        editEmail.setText(mProfileDetails.get(0).getContactEmail());
        txtLicenseExpiredate.setText(mProfileDetails.get(0).getExpireLicenseDate());


        if (!mProfileDetails.get(0).getProfileImage().equals(""))
            Picasso.with(this)
                    .load(Constants.IMAGE_PATH + mProfileDetails.get(0).getProfileImage())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(img_profile);

        if (!mProfileDetails.get(0).getLicensePlate().equals(""))
            Picasso.with(this)
                    .load(Constants.IMAGE_PATH + mProfileDetails.get(0).getLicensePlate())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(img_license);

        container.setVisibility(View.VISIBLE);

    }

    @Override
    public void onGetDriverProfileDetailsFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }

    @Override
    public void onGetProfileDetails(JsonObject customer) {
        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_TRUCKING, editName.getText().toString());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_TRUCKING, editPhone.getText().toString());
        CommonUtils.showToast(getApplicationContext(), Constants.MESSAGE_PROFILE_UPDATE);
    }

    @Override
    public void onGetProfileDetailsFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
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
    public void onClick(View v) {
        if (v.getId() == R.id.img_license) {
            showUploadImagePopup(1);
        }
        if (v.getId() == R.id.img_profile) {
            showUploadImagePopup(0);
        }
        if (v.getId() == R.id.txtLicenseExpiredate) {
            DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
            datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_dialog");
        }
        if (v.getId() == R.id.tvAddVehicleType) {
            Intent intent = new Intent(this, VehicleTypeActivity.class);
            startActivityForResult(intent, 2000);
        }

    }

    @Override
    public void setDate(String date, String tag) {
        txtLicenseExpiredate.setText(date);
    }
}
