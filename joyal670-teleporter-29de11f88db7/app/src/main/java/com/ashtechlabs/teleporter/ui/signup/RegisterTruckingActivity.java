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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.GooglePlacesAdapter;
import com.ashtechlabs.teleporter.ui.CreateAccountActivity;
import com.ashtechlabs.teleporter.ui.signup.registerationtrucking.RegisterController;
import com.ashtechlabs.teleporter.ui.signup.registerationtrucking.RegisterControllerCallback;
import com.ashtechlabs.teleporter.ui.signup.registerationtrucking.RegisterService;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.DatePickerActivityDialog;
import com.ashtechlabs.teleporter.util.OnDatePickListener;

import java.io.File;
import java.io.FileOutputStream;

public class RegisterTruckingActivity extends BaseActivity implements RegisterControllerCallback,
        View.OnClickListener, OnDatePickListener {

    AutoCompleteTextView editCity;
    EditText editName;
    EditText editCompanyAddress, editPhone, editEmail;
    EditText editZipCode;
    ImageView img_license;
    TextView txtLicenseExpiredate;
    ImageView img_profile;
    String img_license_path = "";
    String img_profile_path = "";
    RegisterController registerController;


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
        setContentView(R.layout.activity_trucking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Registration");
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


        editCity = (AutoCompleteTextView) findViewById(R.id.editCity);
        editCity.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));

        editName = (EditText) findViewById(R.id.editName);
        editCompanyAddress = (EditText) findViewById(R.id.editCompanyAddress);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editZipCode = (EditText) findViewById(R.id.editZipCode);
        img_license = (ImageView) findViewById(R.id.img_license);
        img_license.setOnClickListener(this);
        txtLicenseExpiredate = (TextView) findViewById(R.id.txtLicenseExpiredate);
        txtLicenseExpiredate.setOnClickListener(this);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_profile.setOnClickListener(this);


        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = editName.getText().toString();
                String city = editCity.getText().toString();
                String companyAddress = editCompanyAddress.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                String zipCode = editZipCode.getText().toString();
                String licenseExpireDate = txtLicenseExpiredate.getText().toString();

                if (city.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please input City", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please input Name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (companyAddress.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please input Registrated Address", Toast.LENGTH_LONG).show();
                    return;
                }

                if (phone.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please input Contact Number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please input Contact Email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (zipCode.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please input ZipCode", Toast.LENGTH_LONG).show();
                    return;
                }

                if (img_profile_path.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please Upload your profile Image", Toast.LENGTH_LONG).show();
                    return;
                }

                if (img_license_path.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please Upload your Licence", Toast.LENGTH_LONG).show();
                    return;
                }


                if (licenseExpireDate.equals("")) {
                    Toast.makeText(RegisterTruckingActivity.this, "Please input License Expire Date", Toast.LENGTH_LONG).show();
                    return;
                }


                registerController.onRegisterController(city, name, companyAddress, phone, email, zipCode, img_profile_path, img_license_path, licenseExpireDate);

            }
        });
    }


    private void showUploadImagePopup(final int request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image").setItems(
                R.array.upload_array_list,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Intent cameraIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent,
                                request);


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
                bm.compress(CompressFormat.JPEG, 100, out);
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
    public void onGetRegisterDetails(String registrationId) {
        Intent intent = new Intent(RegisterTruckingActivity.this, CreateAccountActivity.class);
        intent.putExtra("mode", "1");
        intent.putExtra("regId", registrationId);
        intent.putExtra("contact_number", editPhone.getText().toString());
        intent.putExtra("contact_email", editEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
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
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtLicenseExpiredate) {
            DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
            datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_dialog");
        }
        if (v.getId() == R.id.img_profile) {
            showUploadImagePopup(0);
        }
        if (v.getId() == R.id.img_license) {
            showUploadImagePopup(1);
        }

    }

    @Override
    public void setDate(String date, String tag) {
        txtLicenseExpiredate.setText(date);
    }
}
