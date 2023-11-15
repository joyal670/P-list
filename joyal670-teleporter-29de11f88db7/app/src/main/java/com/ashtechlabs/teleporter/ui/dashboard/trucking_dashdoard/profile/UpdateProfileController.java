package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile;

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

public class UpdateProfileController extends BaseActivity implements IUpdateTruckingProfileController {

    UpdateProfileControllerCallback mCallback;
    Call<JsonObject> call;

    public UpdateProfileController(UpdateProfileControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateProfileController(String token, String city,String name, String address,String phone, String email, String zipcode, String img_profile_path, String img_license_path, String licenseexpired) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        File lic = new File(img_license_path);
        File pro = new File(img_profile_path);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RetroClient.createRequestBody(token));
        params.put("cityOfWork", RetroClient.createRequestBody(city));
        params.put("companyAddress", RetroClient.createRequestBody(address));
        params.put("ContactNumber", RetroClient.createRequestBody(phone));
        params.put("ContactEmail", RetroClient.createRequestBody(email));
        params.put("zipCode", RetroClient.createRequestBody(zipcode));
        params.put("expireLicenseDate", RetroClient.createRequestBody(licenseexpired));
        params.put("name", RetroClient.createRequestBody(name));


        MultipartBody.Part licensePlate = null;
        if (!TextUtils.isEmpty(img_license_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
            licensePlate = MultipartBody.Part.createFormData("licensePlate", lic.getName(), requestBody);
        }


        MultipartBody.Part profileimage = null;
        if (!TextUtils.isEmpty(img_profile_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), pro);
            profileimage = MultipartBody.Part.createFormData("profileimage", pro.getName(), requestBody);
        }

        call = api.updateTruckingProfile(params, licensePlate, profileimage);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetProfileDetails(response.body());

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
