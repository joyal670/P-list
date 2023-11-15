package com.ashtechlabs.teleporter.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.DriverDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.TruckingDashBoardActivity;
import com.ashtechlabs.teleporter.ui.signup.RegisterDriverActivity;
import com.ashtechlabs.teleporter.ui.signup.RegisterTruckingActivity;
import com.ashtechlabs.teleporter.ui.signup.RegisterVendorActivity;
import com.ashtechlabs.teleporter.ui.signup.RegisterWarehouseActivity;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;

public class LoginActivity extends BaseActivity implements LoginControllerCallback,
        ForgotPasswordControllerCallback, OnCompleteListener<InstanceIdResult> {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private int mode = GlobalUtils.MODE_COURIER;
    private EditText editMobileNum;
    private EditText editPassword;
    private ILoginController ILoginController;
    private IForgotPasswordController IForgotPasswordController;
    private String type;
    private TextView forgotPass;
    private String mobileNum;
    private String password;
    //    private AccessTokenTracker accessTokenTracker;
//    private ProfileTracker profileTracker;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        setUpToolbar();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this);


        Intent intent = getIntent();
        String userMode = intent.getStringExtra("mode");
        if (userMode != null) {
            mode = Integer.parseInt(userMode);
        }

        initUI();

        ILoginController = new LoginController(this);
        IForgotPasswordController = new ForgotPasswordController(this);


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.forgotpassword);
                Button dialogButtonOk = (Button) dialog.findViewById(R.id.send);
                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.cancel);
                final EditText dialogMobile = (EditText) dialog.findViewById(R.id.mobile);
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String mobileNumber = dialogMobile.getText().toString();
                        if (mobileNumber.length() != 10) {
                            Toast.makeText(getApplicationContext(), "Please enter a valid mobile number", Toast.LENGTH_LONG).show();
                        } else {
                            IForgotPasswordController.onForgotPasswordController(mobileNumber, mode);
                        }
                    }
                });
                dialog.show();
            }
        });

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Login");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initUI() {

        forgotPass = (TextView) findViewById(R.id.tvForgotPassword);

        editMobileNum = (EditText) findViewById(R.id.editMobileNum);
        editPassword = (EditText) findViewById(R.id.editPassword);


        findViewById(R.id.tvSignUp).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (mode) {
                    case GlobalUtils.MODE_COURIER:
                        Intent intent = new Intent(LoginActivity.this, RegisterDriverActivity.class);
                        startActivity(intent);
                        break;

                    case GlobalUtils.MODE_TRUCKING:
                        Intent intent3 = new Intent(LoginActivity.this, RegisterTruckingActivity.class);
                        startActivity(intent3);
                        break;

                    case GlobalUtils.MODE_STORAGE:
                        Intent intent1 = new Intent(LoginActivity.this, RegisterWarehouseActivity.class);
                        startActivity(intent1);
                        break;
                    case GlobalUtils.MODE_CARGO:
                        Intent intent2 = new Intent(LoginActivity.this, RegisterVendorActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });

        findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (null == token)
                    return;

                mobileNum = editMobileNum.getText().toString();
                password = editPassword.getText().toString();
                if (mobileNum.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input Mobile Number!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input Password!", Toast.LENGTH_LONG).show();
                    return;
                }

                switch (mode) {
                    case GlobalUtils.MODE_COURIER:
                        GlobalPreferManager.setInt(GlobalPreferManager.Keys.LOGIN_TYPE_COURIER, 0);
                        break;

                    case GlobalUtils.MODE_TRUCKING:
                        GlobalPreferManager.setInt(GlobalPreferManager.Keys.LOGIN_TYPE_TRUCKING, 0);
                        break;

                    case GlobalUtils.MODE_STORAGE:
                        GlobalPreferManager.setInt(GlobalPreferManager.Keys.LOGIN_TYPE_STORAGE, 0);
                        break;

                    case GlobalUtils.MODE_CARGO:
                        GlobalPreferManager.setInt(GlobalPreferManager.Keys.LOGIN_TYPE_CARGO, 0);
                        break;

                }

                ILoginController.onLoginController(mobileNum, "", "", password, token, mode, "", "0");

            }
        });
    }


    @Override
    public void onGetLoginDetails(JsonObject customer) {

        String code = customer.get("code").getAsString();

        if (code.equals("success")) {

            GlobalPreferManager.setString(GlobalPreferManager.Keys.USER_MODE, String.valueOf(mode));
            String token = customer.get("token").getAsString();

            switch (mode) {
                case GlobalUtils.MODE_COURIER:
                    GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_COURIER, token);
                    startActivity(new Intent(LoginActivity.this, DriverDashBoardActivity.class));
                    finish();
                    break;

                case GlobalUtils.MODE_TRUCKING:
                    GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_TRUCKING, token);
                    startActivity(new Intent(LoginActivity.this, TruckingDashBoardActivity.class));
                    finish();
                    break;

                case GlobalUtils.MODE_STORAGE:
                    GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_STORAGE, token);
                    startActivity(new Intent(LoginActivity.this, WareHouseDashBoardActivity.class));
                    finish();
                    break;

                case GlobalUtils.MODE_CARGO:
                    GlobalPreferManager.setString(GlobalPreferManager.Keys.TOKEN_CARGO, token);
                    startActivity(new Intent(LoginActivity.this, CargoDashBoardActivity.class));
                    finish();
                    break;

            }
        } else {
            Toast.makeText(getApplicationContext(), "Login Failed! Try again", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLoginFailed(String message) {
        CommonUtils.showToast(getApplicationContext(), message);
    }



    @Override
    public void onGetForgotPasswordDetails(JsonObject password) {
        CommonUtils.showToast(getApplicationContext(), "Password has been send to registered mail id");
    }

    @Override
    public void onGetForgotPasswordFailed(String message) {
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
    public void onComplete(@NonNull Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
            Log.w(TAG, "getInstanceId failed", task.getException());
            return;
        }
        // Get new Instance ID token
        token = task.getResult().getToken();

    }
}
