package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.custom_views.SlideDateTimeListener;
import com.ashtechlabs.teleporter.custom_views.SlideDateTimePicker;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfileDetail;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.profile.WareHouseProfileActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.ashtechlabs.teleporter.util.SingleChoiceActivitytDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */
public class DriverProfileActivity extends BaseActivity implements
        DriverGetProfileControllerCallBack, UpdateProfileControllerCallback, OnDatePickListener, SingleChoiceItemDialogListener {
    public ArrayList<DriverProfileDetail> mProfileDetails;
    AutoCompleteTextView editCity;
    EditText editName;
    EditText editCompanyAddress, editPhone, editEmail;
    EditText editZipCode;
    ImageView img_license;
    TextView txtLicenseExpiredate;
    EditText edit_vehicle_num;
    ImageView img_insurance;
    ImageView img_profile;
    TextView txtInsuranceExpireDate;
    RelativeLayout spr_vehicleType;
    TextView vehicleType;
    String img_license_path = "";
    String img_insurance_path = "";
    String img_profile_path = "";
    IDriverGetProfileController IDriverGetProfileController;
    IUpdateDriverProfileController IUpdateDriverProfileController;
    ArrayList<String> arraydriver = new ArrayList<>();
    //String[] arrayDriver;
    String vehicleID;

    SimpleDateFormat  dateFormatGmt = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private SlideDateTimeListener slideDateTimeListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            // Do something with the date. This Date object contains
            // the date and time that the user has selected.
            String strDate = dateFormatGmt.format(date);
            txtLicenseExpiredate.setText(strDate);
        }

        @Override
        public void onDateTimeCancel() {
            // Overriding onDateTimeCancel() is optional.
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverprofile);
        setUpToolbar();
        initUI();

        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        //arrayDriver = getResources().getStringArray(R.array.array_driver_service);
        IUpdateDriverProfileController = new UpdateProfileController(this);
        IDriverGetProfileController = new DriverGetProfileController(this);
        IDriverGetProfileController.onGetProfileDetails(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));

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

                String strVehicle = vehicleID;
                String strCity = editCity.getText().toString();
                String strName = editName.getText().toString();
                String strCompanyAddress = editCompanyAddress.getText().toString();
                String email = editEmail.getText().toString();
                String phone = editPhone.getText().toString();
                String strZipCode = editZipCode.getText().toString();
                String str_vehicle_num = edit_vehicle_num.getText().toString();
                String str_license_exp = txtLicenseExpiredate.getText().toString();
                String str_insurance_exp = txtInsuranceExpireDate.getText().toString();


                if (strVehicle.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select the vehicle type", Toast.LENGTH_LONG).show();
                } else if (strCity.equals("")) {
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

                } else if (str_vehicle_num.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the vehicle number", Toast.LENGTH_LONG).show();

                } else if (str_license_exp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input the license expiry date", Toast.LENGTH_LONG).show();

                } else if (str_insurance_exp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input the insurance expiry date", Toast.LENGTH_LONG).show();

                } else {
                    IUpdateDriverProfileController.onUpdateProfileController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""), strVehicle, strCity, strCompanyAddress,phone,email, strZipCode, str_license_exp, str_vehicle_num, str_insurance_exp, strName, img_license_path, img_insurance_path, img_profile_path);
                }
            }
        });

        editCity = (AutoCompleteTextView) findViewById(R.id.editCity);
        editName = (EditText) findViewById(R.id.editName);
        editCompanyAddress = (EditText) findViewById(R.id.editCompanyAddress);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editZipCode = (EditText) findViewById(R.id.editZipCode);
        edit_vehicle_num = (EditText) findViewById(R.id.edit_vehicle_num);
        img_license = (ImageView) findViewById(R.id.img_license);
        img_insurance = (ImageView) findViewById(R.id.img_insurance);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        txtLicenseExpiredate = (TextView) findViewById(R.id.txtLicenseExpiredate);
        txtInsuranceExpireDate = (TextView) findViewById(R.id.txtInsuranceExpireDate);
        spr_vehicleType = (RelativeLayout) findViewById(R.id.spr_vehicle_type);
        vehicleType = (TextView) findViewById(R.id.vehicle_type);


        arraydriver.clear();
        arraydriver.add("Van");
        arraydriver.add("10ft Truck");
        arraydriver.add("14ft Truck");
        arraydriver.add("24ft Truck");

        vehicleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleChoiceActivitytDialog commodityListFragment = new SingleChoiceActivitytDialog();
                Bundle argsCommodity = new Bundle();
                argsCommodity.putString("title", "Driver Services");
                argsCommodity.putStringArrayList("list_array", arraydriver);
                if (TextUtils.isEmpty(vehicleType.getText().toString())) {
                    argsCommodity.putInt("selected_position", 0);
                } else {
                    argsCommodity.putInt("selected_position", arraydriver.indexOf(vehicleType.getText().toString()));
                }
                commodityListFragment.setArguments(argsCommodity);
                commodityListFragment.show(getSupportFragmentManager(), "driver_dialog");
            }
        });

        img_license.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showUploadImagePopup(1);
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUploadImagePopup(3);

            }
        });
        img_insurance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showUploadImagePopup(2);
            }
        });

        txtLicenseExpiredate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
//                Bundle bundle = new Bundle();
//                if(TextUtils.isEmpty(txtLicenseExpiredate.getText().toString())) {
//                    bundle.putString("selected_date", "");
//                }else{
//                    bundle.putString("selected_date", txtLicenseExpiredate.getText().toString());
//                }
//                datePickerDialogFragment.setArguments(bundle);
//                datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_licence");
                if (txtLicenseExpiredate.getText().equals("")) {
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(slideDateTimeListener)
                            .setInitialDate(new Date())
                            .setMinDate(new Date())
                            .build()
                            .show();
                } else {
                    Date date = new Date();
                    try {
                        date = dateFormatGmt.parse(txtLicenseExpiredate.getText().toString());
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(slideDateTimeListener)
                            .setInitialDate(date)
                            .setMinDate(new Date())
                            .build()
                            .show();

                }
            }
        });

        txtInsuranceExpireDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
//                Bundle bundle = new Bundle();
//                if(TextUtils.isEmpty(txtInsuranceExpireDate.getText().toString())) {
//                    bundle.putString("selected_date", "");
//                }else{
//                    bundle.putString("selected_date", txtInsuranceExpireDate.getText().toString());
//                }
//                datePickerDialogFragment.setArguments(bundle);
//                datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_insurance");
                if (txtInsuranceExpireDate.getText().equals("")) {
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(slideDateTimeListener)
                            .setInitialDate(new Date())
                            .setMinDate(new Date())
                            .build()
                            .show();
                } else {
                    Date date = new Date();
                    try {
                        date = dateFormatGmt.parse(txtInsuranceExpireDate.getText().toString());
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(slideDateTimeListener)
                            .setInitialDate(date)
                            .setMinDate(new Date())
                            .build()
                            .show();

                }
            }
        });


    }


    private void showUploadImagePopup(final int request) {
        if (checkPermission())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick Image").setItems(
                    R.array.upload_array_list,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    Intent cameraIntent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent,
                                            request);
                                    break;

                                default:
                                    break;
                            }

                        }
                    });
            builder.create().show();
        }
        else
        {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(DriverProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(DriverProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(DriverProfileActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(DriverProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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

        if (requestCode == 1) {
            img_license_path = saveToSd(photo, "img_license_driver.jpg");
            img_license.setImageBitmap(photo);
        }

        if (requestCode == 2) {
            img_insurance_path = saveToSd(photo, "img_insurance_driver.jpg");
            img_insurance.setImageBitmap(photo);
        }
        if (requestCode == 3) {
            img_profile_path = saveToSd(photo, "img_profile_driver.jpg");
            img_profile.setImageBitmap(photo);
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
    public void onGetDriverProfileDetails(DriverProfile profile) {

        mProfileDetails = profile.getProfile();
        vehicleType.setText(arraydriver.get(Integer.valueOf(mProfileDetails.get(0).getVehicleType())));
        vehicleID = mProfileDetails.get(0).getVehicleType();
        editCity.setText(mProfileDetails.get(0).getCityOfWork());
        editName.setText(mProfileDetails.get(0).getName());
        editCompanyAddress.setText(mProfileDetails.get(0).getCompanyAddress());
        editPhone.setText(mProfileDetails.get(0).getContactNumber());
        editEmail.setText(mProfileDetails.get(0).getContactEmail());
        editZipCode.setText(mProfileDetails.get(0).getZipCode());
        txtLicenseExpiredate.setText(dateFormatGmt.format(new Date(mProfileDetails.get(0).getExpireLicenseDate())));
        txtInsuranceExpireDate.setText(dateFormatGmt.format(new Date(mProfileDetails.get(0).getExpireInsuranceDate())));
        edit_vehicle_num.setText(mProfileDetails.get(0).getVehicleNumber());

        if (!mProfileDetails.get(0).getProfileimage().equals(""))
            Picasso.with(this)
                    .load(Constants.IMAGE_PATH  + mProfileDetails.get(0).getProfileimage())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(img_profile);

        if (!mProfileDetails.get(0).getLicensePlate().equals(""))
            Picasso.with(this)
                    .load(Constants.IMAGE_PATH + mProfileDetails.get(0).getLicensePlate())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(img_license);

        if (!mProfileDetails.get(0).getVehicleInsurance().equals(""))
            Picasso.with(this)
                    .load(Constants.IMAGE_PATH  + mProfileDetails.get(0).getVehicleInsurance())
                    .error(R.drawable.ic_camera)
                    .placeholder(R.drawable.ic_camera)
                    .into(img_insurance);

    }

    @Override
    public void onGetDriverProfileDetailsFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }

    @Override
    public void onGetProfileDetails(JsonObject customer) {
        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_COURIER, editName.getText().toString());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_COURIER, editPhone.getText().toString());
        CommonUtils.showToast(getApplicationContext(), Constants.MESSAGE_PROFILE_UPDATE);
    }

    @Override
    public void onGetProfileDetailsFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }

    @Override
    public void setOnSelectItem(String item, int position, String tag) {
        vehicleType.setText(item);
        vehicleID = String.valueOf(position);
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
    public void setDate(String date, String tag) {
        if (tag.equals("date_picker_insurance")) {
            txtInsuranceExpireDate.setText(date);
        } else {
            txtLicenseExpiredate.setText(date);
        }
    }
}
