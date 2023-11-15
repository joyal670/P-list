package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.profile;

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
import com.ashtechlabs.teleporter.adapters.GooglePlacesAdapter;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfile;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfileDetail;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.StorageSpacesActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile.TruckingProfileActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */
public class WareHouseProfileActivity extends BaseActivity implements WareHouseGetProfileControllerCallBack,
        UpdateProfileControllerCallback, View.OnClickListener {

    EditText editCompanyName;
    TextView tvAddWareHouse;
    ScrollView container;
    AutoCompleteTextView editCompanyAddress;
    EditText editContactNum, editEmail;
    IWarehouseGetProfileController IWarehouseGetProfileController;
    IUpdateWarehouseProfileController IUpdateWarehouseProfileController;
    private TextView tvUploadImage;
    private ImageView ivUploadImage;
    private String imagePath = "";

    private ArrayList<WareHouseProfileDetail> profil = new ArrayList<WareHouseProfileDetail>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        setupToolbar();

        initViews();

        IWarehouseGetProfileController = new WareHouseGetProfileController(this);
        IUpdateWarehouseProfileController = new UpdateWareHouseProfileController(this);
        IWarehouseGetProfileController.onGetProfileDetails();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Account");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initViews() {

        container = (ScrollView) findViewById(R.id.container);
        tvAddWareHouse = (TextView) findViewById(R.id.tvAddWareHouse);
        tvAddWareHouse.setOnClickListener(this);
        editCompanyName = (EditText) findViewById(R.id.editCompanyName);
        editCompanyAddress = (AutoCompleteTextView) findViewById(R.id.editCompanyAddress);
        editCompanyAddress.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
        editContactNum = (EditText) findViewById(R.id.editContactNum);
        editEmail = (EditText) findViewById(R.id.editEmail);
        tvUploadImage = (TextView) findViewById(R.id.tvUploadImage);
        ivUploadImage = (ImageView) findViewById(R.id.ivUploadImage);
        ivUploadImage.setOnClickListener(this);


        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String companyName = editCompanyName.getText().toString();
                String address = editCompanyAddress.getText().toString();
                String contactNum = editContactNum.getText().toString();
                String email = editEmail.getText().toString();


                if (companyName.equals("")) {
                    Toast.makeText(WareHouseProfileActivity.this, "Please input Company Name!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (address.equals("")) {
                    Toast.makeText(WareHouseProfileActivity.this, "Please input Registered Address!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (contactNum.equals("")) {
                    Toast.makeText(WareHouseProfileActivity.this, "Please input Contact Number!", Toast.LENGTH_LONG).show();
                    return;
                }

                IUpdateWarehouseProfileController.onUpdateProfileController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""), companyName, address, contactNum,email, imagePath);
            }
        });

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

    private void showUploadImagePopup(final int request) {
        if (checkPermission())
        {
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
        else
        {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(WareHouseProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(WareHouseProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(WareHouseProfileActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(WareHouseProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;


        Bitmap photo = (Bitmap) data.getExtras().get("data");


        if (requestCode == 1) {
            imagePath = saveToSd(photo, "img_profile_warehouse.jpg");
            ivUploadImage.setImageBitmap(photo);
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
            return Environment.getExternalStorageDirectory().toString() + File.separator + filename;
        }


        return "";
    }

    @Override
    public void onGetStorageProfileDetails(WareHouseProfile profile) {

        profil = profile.getProfile();

        editCompanyName.setText(profil.get(0).getCompanyName());
        editCompanyAddress.setText(profil.get(0).getRegistratedAddress());
        editContactNum.setText(profil.get(0).getContactNumber());
        editEmail.setText(profil.get(0).getContactEmail());

        Picasso.with(this)
                .load(Constants.IMAGE_PATH + profil.get(0).getPicOfWarehouse())
                .error(R.drawable.ic_camera)
                .placeholder(R.drawable.ic_camera)
                .into(ivUploadImage);

        container.setVisibility(View.VISIBLE);

    }

    @Override
    public void onGetStorageProfileDetailsFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }

    @Override
    public void onGetProfileDetails(JsonObject customer) {
        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_STORAGE, editCompanyName.getText().toString());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_STORAGE, editContactNum.getText().toString());
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
        if (v.getId() == R.id.tvAddWareHouse) {
            Intent intent = new Intent(this, StorageSpacesActivity.class);
            intent.putExtra(Constants.KEY_WHICH_ACTIVITY, "profile");
            startActivityForResult(intent, 2000);
        }
        if (v.getId() == R.id.ivUploadImage) {
            showUploadImagePopup(1);
        }
    }
}
