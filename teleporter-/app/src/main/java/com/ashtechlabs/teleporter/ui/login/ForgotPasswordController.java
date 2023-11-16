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
 * Created by IROID_ANDROID1 on 28-Feb-17.
 */

public class ForgotPasswordController extends BaseActivity implements IForgotPasswordController {

    ForgotPasswordControllerCallback mCallback;
    int modes = GlobalUtils.MODE_COURIER;
    Call<JsonObject> call;

    public ForgotPasswordController(ForgotPasswordControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onForgotPasswordController(String mobilenum, int mode) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        call = api.forgotPassword(mobilenum, mode);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    Log.e("RESPONSE >> ", jsonObject.toString());
                    String code = jsonObject.get(Constants.TAG_CODE).getAsString();
                    if (code.equals(Constants.SUCCESS)) {

                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetForgotPasswordDetails(response.body());

                    } else {

                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetForgotPasswordFailed(jsonObject.get(Constants.TAG_MESSAGE).getAsString());
                    }

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetForgotPasswordFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetForgotPasswordFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
