package com.ashtechlabs.teleporter.ui.signup;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
import com.ashtechlabs.teleporter.ui.signup.registrationvendor.RegisterController;
import com.ashtechlabs.teleporter.ui.signup.registrationvendor.RegisterControllerCallback;
import com.ashtechlabs.teleporter.ui.signup.registrationvendor.RegisterVendorService;
import com.ashtechlabs.teleporter.util.SingleChoiceActivitytDialog;
import com.ashtechlabs.teleporter.util.SingleChoiceItemDialogListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RegisterVendorActivity extends BaseActivity implements RegisterControllerCallback,OnDatePickListener,
        SingleChoiceItemDialogListener, View.OnClickListener {


    EditText editCompanyName;
    AutoCompleteTextView editCompnayAddress;
    EditText editContactNum, editEmail;
    EditText editContactDetail;
    EditText editPOC;
    EditText editDesignation;
    EditText editBankDetail;
    EditText editWebsite;
    TextView txtInsuranceExpiredate;
    ImageView img_trade_license;
    ImageView img_insurance;
    ImageView img_profile;
    String img_license_path = "";
    String img_insurance_path = "";
    String img_profile_path = "";
    Bitmap bmp_license;
    Bitmap bmp_insurance;
    Bitmap bmp_profile;
    RelativeLayout spr_partner;
    TextView partner;

    RegisterController registerController;
    String path1, path2, path3;

    ArrayList<String> arrayvendor = new ArrayList<String>();
    String delivarySelect;
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
        setContentView(R.layout.activity_partner);


        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Registration");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        if (shouldAskPermissions()) {
            askPermissions();
        }
        initUI();
        //fillInformationFromPreference();
        registerController = new RegisterVendorService(this);
    }

    private void initUI() {

        arrayvendor.clear();

        arrayvendor.add("Freight forwarders");
        arrayvendor.add("Movers and Packers");
        arrayvendor.add("3PL");
        arrayvendor.add("Courrier Service");
        arrayvendor.add("Parcel");
        arrayvendor.add("Freelancer");
        arrayvendor.add("Others");



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
//                Intent intent = new Intent(RegisterVendorActivity.this, ChatWindowActivity.class);
//                intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, "6202801");
//                startActivity(intent);
//
//            }
//        });

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (bmp_license == null) {
                    Toast.makeText(RegisterVendorActivity.this, "Please take a photo of Trade License Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (bmp_insurance == null) {
                    Toast.makeText(RegisterVendorActivity.this, "Please take a photo of Insurance License Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (bmp_profile == null) {
                    Toast.makeText(RegisterVendorActivity.this, "Please take a photo for Profile", Toast.LENGTH_LONG).show();
                    return;
                }

                String vendorType = delivarySelect;
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
                if(vendorType == null)
                {
                    Toast.makeText(RegisterVendorActivity.this, "Please Select Vendor Type!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (companyName.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Company Name!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (companyAddress.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Company Address!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (contactNum.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Contact Number!", Toast.LENGTH_LONG).show();
                    return;
                }if (email.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Contact Email!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (insuranceExpDate.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Insurance Expire Date!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (contactDetail.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Contact Detail!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (poc.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input POC!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (designation.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Designation!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (bankDetail.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Bank Detail!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (website.equals("")) {
                    Toast.makeText(RegisterVendorActivity.this, "Please Input Website Detail!", Toast.LENGTH_LONG).show();
                    return;
                }
                //submitRegisterInformation();

                Log.e("TAG", "onClick: "+vendorType );
                registerController.onRegisterController(vendorType, companyName, companyAddress, contactNum,email, insuranceExpDate, contactDetail, poc, designation, bankDetail, website, path1, path2, path3);

            }
        });

        editCompanyName = (EditText) findViewById(R.id.editCompanyName);
        editCompnayAddress = (AutoCompleteTextView) findViewById(R.id.editCompnayAddress);
        editCompnayAddress.setAdapter(new GooglePlacesAdapter(this, R.layout.list_item));
        editContactNum = (EditText) findViewById(R.id.editContactNum);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editContactDetail = (EditText) findViewById(R.id.editContactDetail);
        editPOC = (EditText) findViewById(R.id.editPOC);
        editDesignation = (EditText) findViewById(R.id.editDesignation);
        editBankDetail = (EditText) findViewById(R.id.editBankDetail);
        editWebsite = (EditText) findViewById(R.id.editWebsite);
        img_profile = (ImageView) findViewById(R.id.img_trade_profile);

        txtInsuranceExpiredate = (TextView) findViewById(R.id.txtInsuranceExpiredate);
        findViewById(R.id.txtInsuranceExpiredateImg).setOnClickListener(this);
        txtInsuranceExpiredate.setOnClickListener(this);
        img_trade_license = (ImageView) findViewById(R.id.img_trade_license);
        img_insurance = (ImageView) findViewById(R.id.img_insurance);
        spr_partner = (RelativeLayout) findViewById(R.id.spr_partner);
        partner = (TextView) findViewById(R.id.partner);

        partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SingleChoiceActivitytDialog commodityListFragment = new SingleChoiceActivitytDialog();
                Bundle argsCommodity = new Bundle();
                argsCommodity.putString("title", "Vendor Services");
                argsCommodity.putStringArrayList("list_array", arrayvendor);
                if(TextUtils.isEmpty(partner.getText().toString())){
                    argsCommodity.putInt("selected_position", 0);
                }else{
                    argsCommodity.putInt("selected_position", arrayvendor.indexOf(partner.getText().toString()));
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
        img_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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

    private void fillInformationFromPreference() {


        editCompanyName.setText(GlobalPreferManager.getString("vendor_strCompanyName", ""));
        editCompnayAddress.setText(GlobalPreferManager.getString("vendor_strCompnayAddress", ""));
        editContactNum.setText(GlobalPreferManager.getString("vendor_strContactNum", ""));
        editContactDetail.setText(GlobalPreferManager.getString("vendor_strContactDetail", ""));
        editPOC.setText(GlobalPreferManager.getString("vendor_strPOC", ""));
        editDesignation.setText(GlobalPreferManager.getString("vendor_strDesignation", ""));
        editBankDetail.setText(GlobalPreferManager.getString("vendor_strBankDetail", ""));
        editWebsite.setText(GlobalPreferManager.getString("vendor_strWebsite", ""));
        txtInsuranceExpiredate.setText(GlobalPreferManager.getString("vendor_str_insurance_exp", ""));

        img_license_path = GlobalPreferManager.getString("vendor_img_license_path", "");
        img_insurance_path = GlobalPreferManager.getString("vendor_img_insurance_path", "");

        if (!img_license_path.equals("")) {

            Bitmap photo = BitmapFactory.decodeFile(img_license_path);
            if (photo != null) img_trade_license.setImageBitmap(photo);
        }

        if (!img_insurance_path.equals("")) {

            Bitmap photo = BitmapFactory.decodeFile(img_insurance_path);
            if (photo != null) img_insurance.setImageBitmap(photo);
        }


    }

    private void submitRegisterInformation() {

        String strCompanyName = editCompanyName.getText().toString();
        String strCompnayAddress = editCompnayAddress.getText().toString();
        String strContactNum = editContactNum.getText().toString();
        String strContactDetail = editContactDetail.getText().toString();
        String strPOC = editPOC.getText().toString();
        String strDesignation = editDesignation.getText().toString();
        String strBankDetail = editBankDetail.getText().toString();
        String strWebsite = editWebsite.getText().toString();
        String str_insurance_exp = txtInsuranceExpiredate.getText().toString();

        GlobalPreferManager.setString("vendor_strCompanyName", strCompanyName);
        GlobalPreferManager.setString("vendor_strCompnayAddress", strCompnayAddress);
        GlobalPreferManager.setString("vendor_strContactNum", strContactNum);
        GlobalPreferManager.setString("vendor_strContactDetail", strContactDetail);
        GlobalPreferManager.setString("vendor_strPOC", strPOC);
        GlobalPreferManager.setString("vendor_strDesignation", strDesignation);
        GlobalPreferManager.setString("vendor_strBankDetail", strBankDetail);
        GlobalPreferManager.setString("vendor_strWebsite", strWebsite);
        GlobalPreferManager.setString("vendor_str_insurance_exp", str_insurance_exp);
        GlobalPreferManager.setString("vendor_img_license_path", img_license_path);
        GlobalPreferManager.setString("vendor_img_insurance_path", img_insurance_path);


        int completed_num = 0;
        if (!strCompanyName.equals("")) completed_num++;
        if (!strCompnayAddress.equals("")) completed_num++;
        if (!strContactNum.equals("")) completed_num++;
        if (!strContactDetail.equals("")) completed_num++;
        if (!strPOC.equals("")) completed_num++;
        if (!strDesignation.equals("")) completed_num++;
        if (!strBankDetail.equals("")) completed_num++;
        if (!strWebsite.equals("")) completed_num++;
        if (!str_insurance_exp.equals("")) completed_num++;
        if (!img_license_path.equals("")) completed_num++;
        if (!img_insurance_path.equals("")) completed_num++;

        int complete_percent = (int) ((completed_num / 12.0) * 100);
        GlobalPreferManager.setInt("vendor_percent", complete_percent);
    }

    private void showUploadImagePopup(final int request) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        Bitmap photo = null;
        photo = (Bitmap) data.getExtras().get("data");

        if (requestCode == 1) {
            bmp_license = photo;
            img_license_path = saveToSd(photo, "img_license_vendor.jpg", "1");
            img_trade_license.setImageBitmap(photo);
        }

        if (requestCode == 2) {
            bmp_insurance = photo;
            img_insurance_path = saveToSd(photo, "img_insurance_vendor.jpg", "2");
            img_insurance.setImageBitmap(photo);
        }
        if (requestCode == 3) {
            bmp_profile = photo;
            img_profile_path = saveToSd(photo, "img_profile_vendor.jpg", "3");
            img_profile.setImageBitmap(photo);
        }
    }

    @Override
    public void onGetRegisterDetails(JsonObject customer) {
        String regId = customer.get("registerID").getAsString();
        Intent intent = new Intent(RegisterVendorActivity.this, CreateAccountActivity.class);
        intent.putExtra("mode", "2");
        intent.putExtra("regId", regId);
        intent.putExtra("contact_number", editContactNum.getText().toString());
        intent.putExtra("contact_email", editEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFailed(String message) {

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
                return path3;
            }
        }

        return "";
    }


    @Override
    public void setOnSelectItem(String item, int position, String tag) {
        delivarySelect = String.valueOf(position);
        partner.setText(item);
    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    public void dismissLoadingIndicator() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtInsuranceExpiredate || v.getId() == R.id.txtInsuranceExpiredateImg) {
            DialogFragment datePickerDialogFragment = new DatePickerActivityDialog();
            datePickerDialogFragment.show(getSupportFragmentManager(), "date_picker_dialog");
        }
    }

    @Override
    public void setDate(String date, String tag) {
        txtInsuranceExpiredate.setText(date);
    }
}
