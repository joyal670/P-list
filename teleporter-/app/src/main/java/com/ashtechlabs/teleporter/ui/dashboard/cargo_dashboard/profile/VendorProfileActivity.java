package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.profile;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.profile.WareHouseProfileActivity;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.custom_views.SlideDateTimeListener;
import com.ashtechlabs.teleporter.custom_views.SlideDateTimePicker;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfile;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfileDetail;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.SingleChoiceActivitytDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;

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
public class VendorProfileActivity extends BaseActivity implements SingleChoiceItemDialogListener, GetProfileControllerCallBack, UpdateProfileControllerCallback {

    EditText editCompanyName;
    EditText editCompnayAddress;
    EditText editContactNum, editEmail;
    EditText editContactDetail;
    EditText editPOC;
    EditText editDesignation;
    EditText editBankDetail;
    EditText editWebsite;
    TextView txtPartner;
    TextView txtInsuranceExpiredate;
    ImageView img_trade_license;
    ImageView img_insurance;
    ImageView img_profile;
    ArrayList<String> arrayvendor = new ArrayList<String>();
    ArrayList<VendorProfileDetail> profiles = new ArrayList<VendorProfileDetail>();

    String img_license_path = "";
    String img_insurance_path = "";
    String img_profile_path = "";
    IGetProfileController iGetProfileController;
    IUpdateProfileController IUpdateProfileController;
    String itemPosition;
    private SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private SlideDateTimeListener listenerInsurance = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            // Do something with the date. This Date object contains
            // the date and time that the user has selected.

            String strDate = dateFormatGmt.format(date);
            txtInsuranceExpiredate.setText(strDate);
        }

        @Override
        public void onDateTimeCancel() {
            // Overriding onDateTimeCancel() is optional.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendorprofile);

        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        init();

        setupToolbar();

        iGetProfileController = new GetProfileController(this);
        IUpdateProfileController = new UpdateProfileController(this);
        iGetProfileController.onGetProfileDetails(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));
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

    private void init() {

        editCompanyName = (EditText) findViewById(R.id.editCompanyName);
        editCompnayAddress = (EditText) findViewById(R.id.editCompnayAddress);
        editContactNum = (EditText) findViewById(R.id.editContactNum);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editContactDetail = (EditText) findViewById(R.id.editContactDetail);
        editPOC = (EditText) findViewById(R.id.editPOC);
        editDesignation = (EditText) findViewById(R.id.editDesignation);
        editBankDetail = (EditText) findViewById(R.id.editBankDetail);
        editWebsite = (EditText) findViewById(R.id.editWebsite);
        txtPartner = (TextView) findViewById(R.id.partner);
        txtInsuranceExpiredate = (TextView) findViewById(R.id.txtInsuranceExpiredate);
        img_trade_license = (ImageView) findViewById(R.id.img_trade_license);
        img_insurance = (ImageView) findViewById(R.id.img_insurance);
        img_profile = (ImageView) findViewById(R.id.img_trade_profile);

        arrayvendor.clear();
        arrayvendor.add("Freight forwarders");
        arrayvendor.add("Movers and Packers");
        arrayvendor.add("3PL");
        arrayvendor.add("Courrier Service");
        arrayvendor.add("Parcel");
        arrayvendor.add("Freelancer");
        arrayvendor.add("Others");

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String partner = itemPosition;
                String companyName = editCompanyName.getText().toString();
                String companyAddress = editCompnayAddress.getText().toString();
                String contactNum = editContactNum.getText().toString();
                String email = editEmail.getText().toString();
                String insuranceExpDate = txtInsuranceExpiredate.getText().toString();
                String contactDetail = editContactDetail.getText().toString();
                String poc = editPOC.getText().toString();
                String designation = editDesignation.getText().toString();
                String bankDetail = editBankDetail.getText().toString();
                String website = editWebsite.getText().toString();

                if (companyName.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Company Name!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (companyAddress.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Company Address!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (contactNum.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Contact Number!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Email!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (insuranceExpDate.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Insurance Expire Date!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (contactDetail.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Contact Detail!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (poc.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input POC!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (designation.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Designation!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (bankDetail.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Bank Detail!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (website.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input Website Detail!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (partner.equals("")) {
                    Toast.makeText(VendorProfileActivity.this, "Please Input the service!", Toast.LENGTH_LONG).show();
                    return;
                }
                IUpdateProfileController.onUpdateProfileController(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO,""),partner, companyName, companyAddress, contactNum,
                        email, insuranceExpDate, contactDetail, poc, designation,
                        bankDetail, website, img_insurance_path, img_license_path, img_profile_path);


            }
        });

        txtPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleChoiceActivitytDialog commodityListFragment = new SingleChoiceActivitytDialog();
                Bundle argsCommodity = new Bundle();
                argsCommodity.putString("title", "Vendor Services");
                argsCommodity.putStringArrayList("list_array", arrayvendor);
                if(TextUtils.isEmpty(txtPartner.getText().toString())){
                    argsCommodity.putInt("selected_position", 0);
                }else{
                    argsCommodity.putInt("selected_position", arrayvendor.indexOf(txtPartner.getText().toString()));
                }
                commodityListFragment.setArguments(argsCommodity);
                commodityListFragment.show(getSupportFragmentManager(), "driver_dialog");
            }
        });

        img_trade_license.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showUploadImagePopup(1);
            }
        });

        img_insurance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showUploadImagePopup(2);
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUploadImagePopup(3);

            }
        });

        txtInsuranceExpiredate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (txtInsuranceExpiredate.getText().equals("")) {
                    new SlideDateTimePicker.Builder(getSupportFragmentManager())
                            .setListener(listenerInsurance)
                            .setInitialDate(new Date())
                            .setMinDate(new Date())
                            .build()
                            .show();
                } else {
                    Date date = new Date();
                    try {
                        date = dateFormatGmt.parse(txtInsuranceExpiredate.getText().toString());
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
        });
    }

    private void showUploadImagePopup(final int request) {
        if (checkPermission())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(VendorProfileActivity.this);
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
        int result = ContextCompat.checkSelfPermission(VendorProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(VendorProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(VendorProfileActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(VendorProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        Bitmap photo = null;
        photo = (Bitmap) data.getExtras().get("data");

        if (requestCode == 1) {
            img_license_path = saveToSd(photo, "img_license_vendor.jpg");
            img_trade_license.setImageBitmap(photo);
        }

        if (requestCode == 2) {
            img_insurance_path = saveToSd(photo, "img_insurance_vendor.jpg");
            img_insurance.setImageBitmap(photo);
        }

        if (requestCode == 3) {
            img_profile_path = saveToSd(photo, "img_profile_vendor.jpg");
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
        txtPartner.setText(item);
        itemPosition = String.valueOf(position);
    }

    @Override
    public void onGetVendorProfileDetails(VendorProfile profile) {
        profiles = profile.getProfile();
        editCompanyName.setText(profiles.get(0).getCompanyName());
        editCompnayAddress.setText(profiles.get(0).getRegistratedAddress());
        editContactNum.setText(profiles.get(0).getContactNumber());
        editEmail.setText(profiles.get(0).getContactEmail());
        editContactDetail.setText(profiles.get(0).getContactDetail());
        editPOC.setText(profiles.get(0).getPOC());
        editDesignation.setText(profiles.get(0).getDesignation());
        editBankDetail.setText(profiles.get(0).getBackAccount());
        editWebsite.setText(profiles.get(0).getWebsiteURL());
        txtPartner.setText(arrayvendor.get(Integer.parseInt(profiles.get(0).getVendorType())));
        itemPosition = profiles.get(0).getVendorType();
        txtInsuranceExpiredate.setText(profiles.get(0).getExpireInsuranceDate());

        Picasso.with(this)
                .load(Constants.IMAGE_PATH + profiles.get(0).getTradeLicenseNumber())
                .error(R.drawable.ic_camera)
                .placeholder(R.drawable.ic_camera)
                .into(img_trade_license);

        Picasso.with(this)
                .load(Constants.IMAGE_PATH  + profiles.get(0).getInsuranceNumber())
                .error(R.drawable.ic_camera)
                .placeholder(R.drawable.ic_camera)
                .into(img_insurance);
        Picasso.with(this)
                .load(Constants.IMAGE_PATH + profiles.get(0).getProfileimage())
                .error(R.drawable.ic_camera)
                .placeholder(R.drawable.ic_camera)
                .into(img_profile);

    }

    @Override
    public void onGetVendorProfileDetailsFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }

    @Override
    public void onGetProfileDetails(JsonObject customer) {

        GlobalPreferManager.setString(GlobalPreferManager.Keys.USERNAME_CARGO, editCompanyName.getText().toString());
        GlobalPreferManager.setString(GlobalPreferManager.Keys.PHONE_CARGO, editContactNum.getText().toString());

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
}