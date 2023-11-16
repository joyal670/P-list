package com.ashtechlabs.teleporter.ui.createaccount;

import android.util.Log;
import android.widget.Toast;

import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class CreateAccountService extends BaseActivity implements CreateAccountController {

    CreateAccountControllerCallback mCallback;
    int modes = GlobalUtils.MODE_COURIER;
    Call<JsonObject> call;

    public CreateAccountService(CreateAccountControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onRegisterController(String mobile, String pass, String regid, int mode, String email) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        switch (mode) {
            case GlobalUtils.MODE_COURIER:
                call = api.createDriverAccount(mobile, pass, regid, email);
                break;
            case GlobalUtils.MODE_TRUCKING:
                call = api.createTruckingAccount(mobile, pass, regid, email);
                break;
            case GlobalUtils.MODE_STORAGE:
                call = api.createWarehouseAccount(mobile, pass, regid, email);
                break;
            case GlobalUtils.MODE_CARGO:
                call = api.createVendorAccount(mobile, pass, regid, email);
                break;
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    Log.e("RESPONSE >> ", jsonObject.toString());
                    String code = jsonObject.get(Constants.TAG_CODE).getAsString();
                    if (code.equals(Constants.SUCCESS)) {
                        mCallback.showLoadingDialog(false);
                        mCallback.onGetCreateDetails(response.body());
                    }
                    else {
                        mCallback.showLoadingDialog(false);
                        mCallback.onGetCreateDetailsFailed(jsonObject.get(Constants.TAG_MESSAGE).getAsString());
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.showLoadingDialog(false);
            }
        });
    }
}
