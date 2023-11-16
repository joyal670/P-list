package com.ashtechlabs.teleporter.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.createaccount.CreateAccountController;
import com.ashtechlabs.teleporter.ui.createaccount.CreateAccountControllerCallback;
import com.ashtechlabs.teleporter.ui.createaccount.CreateAccountService;

public class CreateAccountActivity extends BaseActivity implements CreateAccountControllerCallback {

    int mode;
    EditText editMobileNum;
    EditText editPassword;
    EditText editConfirmPassword;
    CreateAccountController createAccountController;
    EditText editEmail;
    String mobileNum;
    String password, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        Intent intent = getIntent();
        String type = intent.getStringExtra("mode");
        phone = intent.getStringExtra("contact_number");
        email = intent.getStringExtra("contact_email");
        mode = Integer.parseInt(type);
        createAccountController = new CreateAccountService(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Registration");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        initUI();
    }

    private void initUI() {

        editMobileNum = (EditText) findViewById(R.id.editMobileNum);
        editMobileNum.setText(phone);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editEmail.setText(email);

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
////                Intent intent = new Intent(CreateAccountActivity.this, ChatWindowActivity.class);
////                intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, "6202801");
////                startActivity(intent);
//
//            }
//        });
//
//        switch (mode) {
//            case GlobalUtils.MODE_COURIER:
//                findViewById(R.id.layout_title).setBackgroundResource(R.drawable.img_title_driver);
//                //findViewById(R.id.btnCreateAccount).setBackgroundResource(R.drawable.btn_driver);
//                break;
//            case GlobalUtils.MODE_STORAGE:
//                findViewById(R.id.layout_title).setBackgroundResource(R.drawable.img_title_warehouse);
//                //findViewById(R.id.btnCreateAccount).setBackgroundResource(R.drawable.btn_warehouse);
//                break;
//
//            case GlobalUtils.MODE_CARGO:
//                findViewById(R.id.layout_title).setBackgroundResource(R.drawable.img_title_vendor);
//                //findViewById(R.id.btnCreateAccount).setBackgroundResource(R.drawable.btn_vendor);
//
//                break;
//
//        }

        findViewById(R.id.btnCreateAccount).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                createAccount();
            }
        });

    }


    private void createAccount() {

        mobileNum = editMobileNum.getText().toString();
        password = editPassword.getText().toString();
        String confirmPass = editConfirmPassword.getText().toString();
        String email = editEmail.getText().toString();

        if (mobileNum.equals("")) {
            Toast.makeText(this, "Please input Mobile Number!", Toast.LENGTH_LONG).show();
            return;
        }

        if (email.equals("")) {
            Toast.makeText(this, "Please Input Email!", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.equals("")) {
            Toast.makeText(this, "Please input Password!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!confirmPass.equals(password)) {
            Toast.makeText(this, "Confirm Password does not match with Password!", Toast.LENGTH_LONG).show();
            return;
        }


        showLoadingDialog(true);
        createAccountController.onRegisterController(mobileNum, password, getIntent().getStringExtra("regId"), mode, email);


    }


    @Override
    public void onGetCreateDetails(JsonObject customer) {
//        signUp(mobileNum, password);
        showLoadingDialog(false);
        Intent intent = new Intent(CreateAccountActivity.this, Dashboard_Main.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGetCreateDetailsFailed(String msg) {
        showLoadingDialog(false);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateAccountActivity.this, Dashboard_Main.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
        finish();
    }

    public void showLoadingDialog(boolean isShow) {
        if (isShow) showProgress();
        else dismissProgress();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
