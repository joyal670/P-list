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
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.GooglePlacesAdapter;
import com.ashtechlabs.teleporter.ui.CreateAccountActivity;
import com.ashtechlabs.teleporter.ui.signup.registrationwarehouse.RegisterController;
import com.ashtechlabs.teleporter.ui.signup.registrationwarehouse.RegisterControllerCallback;
import com.ashtechlabs.teleporter.ui.signup.registrationwarehouse.RegisterWareHouseService;
import com.ashtechlabs.teleporter.util.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;

public class RegisterWarehouseActivity extends BaseActivity implements RegisterControllerCallback {

    ImageView ivUploadImage;
    TextView tvUploadImage;
    EditText editCompanyName;
    AutoCompleteTextView editCompanyAddress;
    EditText editContactNum, editEmail;
    String img_warehouse_path = "";
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
        setContentView(R.layout.activity_warehouse);
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
        //fillInformationFromPreference();
        registerController = new RegisterWareHouseService(this);
    }

    private void initUI() {


        ivUploadImage = (ImageView) findViewById(R.id.ivUploadImage);
        editCompanyName = (EditText) findViewById(R.id.editCompanyName);
        editCompanyAddress = (AutoCompleteTextView) findViewById(R.id.editCompanyAddress);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editCompanyAddress.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
        editContactNum = (EditText) findViewById(R.id.editContactNum);


        ivUploadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showUploadImagePopup(0);
            }
        });


        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String companyName = editCompanyName.getText().toString();
                String address = editCompanyAddress.getText().toString();
                String email = editEmail.getText().toString();
                String contactNum = editContactNum.getText().toString();

                if (companyName.equals("")) {
                    Toast.makeText(RegisterWarehouseActivity.this, "Please input Company Name!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (img_warehouse_path.equals("")) {
                    Toast.makeText(RegisterWarehouseActivity.this, "Please upload profile image!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (address.equals("")) {
                    Toast.makeText(RegisterWarehouseActivity.this, "Please input Registrated Address!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (contactNum.equals("")) {
                    Toast.makeText(RegisterWarehouseActivity.this, "Please input Contact Number!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.equals("")) {
                    Toast.makeText(RegisterWarehouseActivity.this, "Please input Contact Email!", Toast.LENGTH_LONG).show();
                    return;
                }

                registerController.onRegisterController(companyName, address, contactNum, email, img_warehouse_path);
//                new registerWarehouse().execute(storageType, companyName, address, contactNum, insuranceExpDate);
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

        Bitmap photo = (Bitmap) data.getExtras().get("data");

        if (requestCode == 0) {
            img_warehouse_path = saveToSd(photo, "img_warehouse_pic.jpg");
            ivUploadImage.setImageBitmap(photo);
        }

    }

    @Override
    public void onGetRegisterDetails(JsonObject customer) {
        String regId = customer.get("registrationID").getAsString();
        Intent intent = new Intent(this, CreateAccountActivity.class);
        intent.putExtra("mode", "3");
        intent.putExtra("regId", regId);
        intent.putExtra("contact_number", editContactNum.getText().toString());
        intent.putExtra("contact_email", editEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
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
}
