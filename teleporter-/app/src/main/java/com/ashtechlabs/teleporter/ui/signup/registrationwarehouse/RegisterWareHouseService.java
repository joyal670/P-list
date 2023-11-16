package com.ashtechlabs.teleporter.ui.signup.registrationwarehouse;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class RegisterWareHouseService extends BaseActivity implements RegisterController {

    RegisterControllerCallback mCallback;

    Call<JsonObject> call;

    public RegisterWareHouseService(RegisterControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onRegisterController(String companyName, String address, String contactNum,String email, String path1) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();


        File ware = new File(path1);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("companyName", RetroClient.createRequestBody(companyName));
        params.put("registratedAddress", RetroClient.createRequestBody(address));
        params.put("contactNumber", RetroClient.createRequestBody(contactNum));
        params.put("ContactEmail", RetroClient.createRequestBody(email));


        MultipartBody.Part tradeLicenseNumber = null;
        if (!TextUtils.isEmpty(path1)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ware);
            tradeLicenseNumber = MultipartBody.Part.createFormData("picOfWarehouse", ware.getName(), requestBody);
        }


        call = api.registerWarehouse(params, tradeLicenseNumber);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    mCallback.onGetRegisterDetails(response.body());
                    mCallback.dismissLoadingIndicator();
                } else {
                    mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
                    mCallback.dismissLoadingIndicator();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
                mCallback.dismissLoadingIndicator();
            }
        });
    }
}