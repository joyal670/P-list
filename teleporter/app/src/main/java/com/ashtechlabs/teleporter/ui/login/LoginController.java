package com.ashtechlabs.teleporter.ui.login;

import android.util.Log;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class LoginController extends BaseActivity implements ILoginController {

    LoginControllerCallback mCallback;
    Call<JsonObject> call;

    public LoginController(LoginControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onLoginController(String mobile,String username, String email, String password, String regid, int mode, String socialid, String type) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();


        switch (mode) {
            case GlobalUtils.MODE_COURIER:
                call = api.loginCourier(mobile,username, email, password, regid, socialid, type);
                break;
            case GlobalUtils.MODE_TRUCKING:
                call = api.loginTrucking(mobile,username, email, password, regid, socialid, type);
                break;
            case GlobalUtils.MODE_STORAGE:
                call = api.loginWarehouse(mobile,username, email,password, regid, socialid, type);
                break;
            case GlobalUtils.MODE_CARGO:
                call = api.loginVendor(mobile,username, email,password, regid, socialid, type);
                break;
        }


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    Log.e("RESPONSE >> ", jsonObject.toString());
                    String code = jsonObject.get(Constants.TAG_CODE).getAsString();
                    if (code.equals(Constants.SUCCESS)) {

                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetLoginDetails(response.body());

                    } else {

                        mCallback.dismissLoadingIndicator();
                        mCallback.onLoginFailed(jsonObject.get(Constants.TAG_MESSAGE).getAsString());
                    }

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onLoginFailed(Constants.MESSAGE_SERVER_ERROR);
//                    Toast.makeText(HomeDriverActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onLoginFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }

}
