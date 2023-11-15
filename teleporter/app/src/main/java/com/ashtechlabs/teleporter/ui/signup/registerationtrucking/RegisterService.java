package com.ashtechlabs.teleporter.ui.signup.registerationtrucking;

import android.text.TextUtils;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;

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

public class RegisterService extends BaseActivity implements RegisterController {

    RegisterControllerCallback mCallback;
    Call<JsonObject> call;

    public RegisterService(RegisterControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onRegisterController(String city,String name, String address,String phone,String email, String zipcode,String profileImg, String licenceImg, String licenseexpiredense) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        File lic = new File(licenceImg);
        File pro = new File(profileImg);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("cityOfWork", RetroClient.createRequestBody(city));
        params.put("companyAddress", RetroClient.createRequestBody(address));
        params.put("ContactNumber", RetroClient.createRequestBody(phone));
        params.put("ContactEmail", RetroClient.createRequestBody(email));
        params.put("zipCode", RetroClient.createRequestBody(zipcode));
        params.put("expireLicenseDate", RetroClient.createRequestBody(licenseexpiredense));
        params.put("name", RetroClient.createRequestBody(name));


        MultipartBody.Part licensePlate = null;
        if (!TextUtils.isEmpty(licenceImg)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
            licensePlate = MultipartBody.Part.createFormData("licensePlate", lic.getName(), requestBody);
        }


        MultipartBody.Part profileimage = null;
        if (!TextUtils.isEmpty(profileImg)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), pro);
            profileimage = MultipartBody.Part.createFormData("profileimage", pro.getName(), requestBody);
        }


        call = api.registerTrucking(params, licensePlate, profileimage);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    String code = response.body().get("code").getAsString();
                    if(code.equals("success")){
                        mCallback.onGetRegisterDetails(response.body().get("registrationID").getAsString());
                    }else{
                        mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
                    }
                } else {
                    mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
                }
                mCallback.dismissLoadingIndicator();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
