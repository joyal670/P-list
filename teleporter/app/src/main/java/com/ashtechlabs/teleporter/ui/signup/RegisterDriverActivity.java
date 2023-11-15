package com.ashtechlabs.teleporter.ui.signup;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;
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

import com.ashtechlabs.teleporter.util.DatePickerActivityDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;
import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.GooglePlacesAdapter;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.custom_views.SlideDateTimeListener;
import com.ashtechlabs.teleporter.ui.CreateAccountActivity;
import com.ashtechlabs.teleporter.ui.signup.registrationdriver.RegisterController;
import com.ashtechlabs.teleporter.ui.signup.registrationdriver.RegisterControllerCallback;
import com.ashtechlabs.teleporter.ui.signup.registrationdriver.RegisterService;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.SingleChoiceActivitytDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RegisterDriverActivity extends BaseActivity implements RegisterControllerCallback,
        SingleChoiceItemDialogListener, View.OnClickListener, OnDatePickListener {

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
    String img_license_path = "";
    String img_insurance_path = "";
    String img_profile_path = "";
    Bitmap bmp_license;
    Bitmap bmp_insurance;
    Bitmap bmp_profile;
    RelativeLayout spr_vehicle_type;
    TextView vehicle_type;
    RegisterController registerController;
    String path1, path2, path3;

    ArrayList<String> arraydriver = new ArrayList<String>();
    String delivarySelect = "";
    private SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd/MM/yyyy", Locale.US);


    //13256238963 qwertyuiop
    private SlideDateTimeListener listenerLicense = new SlideDateTimeListener() {

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
    private SlideDateTimeListener listenerInsurance = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            // Do something with the date. This Date object contains
            // the date and time that the user has selected.

            String strDate = dateFormatGmt.format(date);
            txtInsuranceExpireDate.setText(strDate);
        }

        @Override
        public void onDateTimeCancel() {
            // Overriding onDateTimeCancel() is optional.
        }
    };


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
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Registration");

        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        initUI();
        if (shouldAskPermissions()) {
            askPermissions();
        }
        registerController = new RegisterService(this);
        //fillInformationFromPreference();
    }

    private void initUI() {

        arraydriver.clear();

        arraydriver.add("Van");
        arraydriver.add("10ft Truck");
        arraydriver.add("14ft Truck");
        arraydriver.add("24ft Truck");

        editCity = (AutoCompleteTextView) findViewById(R.id.editCity);
        editCity.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));

        editName = (EditText) findViewById(R.id.editName);
        editCompanyAddress = (EditText) findViewById(R.id.editCompanyAddress);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editZipCode = (EditText) findViewById(R.id.editZipCode);
        edit_vehicle_num = (EditText) findViewById(R.id.edit_vehicle_num);
        img_license = (ImageView) findViewById(R.id.img_license);
        img_insurance = (ImageView) findViewById(R.id.img_insurance);
        txtLicenseExpiredate = (TextView) findViewById(R.id.txtLicenseExpiredate);
        findViewById(R.id.txtLicenseExpiredateImg).setOnClickListener(this);
        txtLicenseExpiredate.setOnClickListener(this);
        txtInsuranceExpireDate = (TextView) findViewById(R.id.txtInsuranceExpireDate);
        findViewById(R.id.txtInsuranceExpireDateImg).setOnClickListener(this);
        txtInsuranceExpireDate.setOnClickListener(this);
        spr_vehicle_type = (RelativeLayout) findViewById(R.id.spr_vehicle_type);
        vehicle_type = (TextView) findViewById(R.id.vehicle_type);
        img_profile = (ImageView) findViewById(R.id.img_profile);


        vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleChoiceActivitytDialog commodityListFragment = new SingleChoiceActivitytDialog();
                Bundle argsCommodity = new Bundle();
                argsCommodity.putString("title", "Driver Services");
                argsCommodity.putStringArrayList("list_array", arraydriver);
                if(TextUtils.isEmpty(vehicle_type.getText().toString())){
                    argsCommodity.putInt("selected_position", 0);
                }else{
                    argsCommodity.putInt("selected_position", arraydriver.indexOf(vehicle_type.getText().toString()));
                }
                commodityListFragment.setArguments(argsCommodity);
                commodityListFragment.show(getSupportFragmentManager(), "driver_dialog");
            }
        });

//        checkAndRequestPermissions();
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

  /*      txtLicenseExpiredate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (txtLicenseExpiredate.getText().equals("")) {
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(listenerLicense)
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
                            .setListener(listenerLicense)
                            .setInitialDate(date)
                            .setMinDate(new Date())
                            .build()
                            .show();

                }
            }
        });*/

 /*       txtInsuranceExpireDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (txtInsuranceExpireDate.getText().equals("")) {
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(listenerInsurance)
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
                            .setListener(listenerInsurance)
                            .setInitialDate(date)
                            .setMinDate(new Date())
                            .build()
                            .show();

                }
            }
        });*/

//        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                finish();
//            }
//        });
//
//        findViewById(R.id.btnChat).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(RegisterDriverActivity.this, ChatWindowActivity.class);
//                intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, "6202801");
//                startActivity(intent);
//
//            }
//        });

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

				/*
                submitRegisterInformation();
				Intent intent=new Intent(RegisterDriverActivity.this, CreateAccountActivity.class);
				intent.putExtra("mode", GlobalUtils.MODE_COURIER);
				startActivity(intent);
				finish();*/

                if (bmp_license == null) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input License Image", Toast.LENGTH_LONG).show();
                    return;
                }

                if (bmp_insurance == null) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input Insurance Image", Toast.LENGTH_LONG).show();
                    return;
                }
                if (bmp_profile == null) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input ProfileActivity Image", Toast.LENGTH_LONG).show();
                    return;
                }


                String name = editName.getText().toString();
                String city = editCity.getText().toString();
                String companyAddress = editCompanyAddress.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                String zipCode = editZipCode.getText().toString();
                String licenseExpireDate = txtLicenseExpiredate.getText().toString();
                String insuranceExpireDate = txtInsuranceExpireDate.getText().toString();
                String vehicleNum = edit_vehicle_num.getText().toString();
                String strVehicle = delivarySelect;

                if (strVehicle.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please select the vehicle type", Toast.LENGTH_LONG).show();
                    return;
                }
                if (city.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input City", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input Name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (companyAddress.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input Registrated Address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phone.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input Phone Number", Toast.LENGTH_LONG).show();
                    return;
                }if (email.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input Email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (zipCode.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input ZipCode", Toast.LENGTH_LONG).show();
                    return;
                }

                if (licenseExpireDate.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input License Expire Date", Toast.LENGTH_LONG).show();
                    return;
                }

                if (insuranceExpireDate.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input Insurance Expire Date", Toast.LENGTH_LONG).show();
                    return;
                }

                if (vehicleNum.equals("")) {
                    Toast.makeText(RegisterDriverActivity.this, "Please input Vehicle Number", Toast.LENGTH_LONG).show();
                    return;
                }

                //submitRegisterInformation();
                registerController.onRegisterController(delivarySelect, city, companyAddress, phone ,email,zipCode, licenseExpireDate, vehicleNum, insuranceExpireDate, name, path1, path2, path3);

//                new registerDriver().execute(String.valueOf(spr_vehicle_type.getSelectedItemPosition()), editCity.getText().toString(), editCompanyAddress.getText().toString(), editZipCode.getText().toString(), txtLicenseExpiredate.getText().toString(), edit_vehicle_num.getText().toString(), txtInsuranceExpireDate.getText().toString());
            }
        });
    }


    private void submitRegisterInformation() {


        String strCity = editCity.getText().toString();
        String strName = editName.getText().toString();
        String strCompanyAddress = editCompanyAddress.getText().toString();
        String strZipCode = editZipCode.getText().toString();
        String str_vehicle_num = edit_vehicle_num.getText().toString();
        String str_license_exp = txtLicenseExpiredate.getText().toString();
        String str_insurance_exp = txtInsuranceExpireDate.getText().toString();

        GlobalPreferManager.setString("driver_strCity", strCity);
        GlobalPreferManager.setString("driver_strName", strName);
        GlobalPreferManager.setString("driver_strCompanyAddress", strCompanyAddress);
        GlobalPreferManager.setString("driver_strZipCode", strZipCode);
        GlobalPreferManager.setString("driver_str_vehicle_num", str_vehicle_num);
        GlobalPreferManager.setString("driver_img_license_path", img_license_path);
        GlobalPreferManager.setString("driver_img_insurance_path", img_insurance_path);
        GlobalPreferManager.setString("driver_str_license_exp", str_license_exp);
        GlobalPreferManager.setString("driver_str_insurance_exp", str_insurance_exp);

        int completed_num = 0;
        if (!strCity.equals("")) completed_num++;
        if (!strName.equals("")) completed_num++;
        if (!strCompanyAddress.equals("")) completed_num++;
        if (!strZipCode.equals("")) completed_num++;
        if (!str_vehicle_num.equals("")) completed_num++;
        if (!str_insurance_exp.equals("")) completed_num++;
        if (!str_license_exp.equals("")) completed_num++;
        if (!img_license_path.equals("")) completed_num++;
        if (!img_insurance_path.equals("")) completed_num++;

        int complete_percent = (int) ((completed_num / 9.0) * 100);
        GlobalPreferManager.setInt("driver_percent", complete_percent);
    }

    private void showUploadImagePopup(final int request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image").setItems(
                R.array.upload_array_list,
                new DialogInterface.OnClickListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Bitmap photo = null;
        photo = (Bitmap) data.getExtras().get("data");

        if (requestCode == 1) {
            bmp_license = photo;
            img_license_path = saveToSd(photo, "img_license_driver.jpg", "1");
            img_license.setImageBitmap(photo);
        }

        if (requestCode == 2) {
            bmp_insurance = photo;
            img_insurance_path = saveToSd(photo, "img_insurance_driver.jpg", "2");
            img_insurance.setImageBitmap(photo);

        }
        if (requestCode == 3) {
            bmp_profile = photo;
            img_profile_path = saveToSd(photo, "img_profile_driver.jpg", "3");
            img_profile.setImageBitmap(photo);

        }
    }

    private String saveToSd(Bitmap bm, String filename, String type) {
        if (type.equals("1")) {
            if (bm != null) {
                File sd = Environment.getExternalStorageDirectory();
                Log.d("pathsd", " " + sd.getAbsolutePath().toString());
                File dest = new File(sd, filename);
                Log.d("pathdest", " " + dest.getAbsolutePath().toString());
                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    bm.compress(CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path1 = Environment.getExternalStorageDirectory().toString() + File.separator + filename;
                Log.d("path1", " " + path1);

                return path1;
            }
        } else if (type.equals("2")) {
            if (bm != null) {
                File sd = Environment.getExternalStorageDirectory();
                Log.d("pathsd", " " + sd.getAbsolutePath().toString());
                File dest = new File(sd, filename);
                Log.d("pathdest", " " + dest.getAbsolutePath().toString());
                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    bm.compress(CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path2 = Environment.getExternalStorageDirectory().toString() + File.separator + filename;
                Log.d("path2", " " + path2);
                return path2;
            }
        } else if (type.equals("3")) {
            if (bm != null) {
                File sd = Environment.getExternalStorageDirectory();
                Log.d("pathsd", " " + sd.getAbsolutePath().toString());
                File dest = new File(sd, filename);
                Log.d("pathdest", " " + dest.getAbsolutePath().toString());
                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    bm.compress(CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path3 = Environment.getExternalStorageDirectory().toString() + File.separator + filename;
                Log.d("path3", " " + path3);
                return path3;
            }
        }

        return "";
    }

    @Override
    public void onGetRegisterDetails(JsonObject result) {
        String regId = result.get("value").getAsString();
        Intent intent = new Intent(RegisterDriverActivity.this, CreateAccountActivity.class);
        intent.putExtra("mode", "0");
        intent.putExtra("regId", regId);
        intent.putExtra("contact_number", editPhone.getText().toString());
        intent.putExtra("contact_email", editEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFailed(String message) {
        CommonUtils.showToast(getApplicationContext(),message);
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
        delivarySelect = String.valueOf(position);
        vehicle_type.setText(item);
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
        if (v.getId() == R.id.txtLicenseExpiredate || v.getId() == R.id.txtLicenseExpiredateImg) {
            DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
            datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_licence");
        }
        if (v.getId() == R.id.txtInsuranceExpireDate || v.getId() == R.id.txtInsuranceExpireDateImg) {
            DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
            datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_insurance");
        }
    }

    @Override
    public void setDate(String date, String tag) {
        if(tag.equals("date_picker_licence")){
            txtLicenseExpiredate.setText(date);
        }else if(tag.equals("date_picker_insurance")){
            txtInsuranceExpireDate.setText(date);
        }
    }
}
