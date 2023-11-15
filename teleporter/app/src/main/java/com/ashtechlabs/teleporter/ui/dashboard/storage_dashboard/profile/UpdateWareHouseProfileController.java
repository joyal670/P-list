package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.profile;

import android.text.TextUtils;

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

public class UpdateWareHouseProfileController implements IUpdateWarehouseProfileController {

    UpdateProfileControllerCallback mCallback;


    public UpdateWareHouseProfileController(UpdateProfileControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateProfileController(String token, String companyName, String address, String contactNum, String email, String imagePath) {


        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        File lic = new File(imagePath);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RetroClient.createRequestBody(token));
        params.put("companyName", RetroClient.createRequestBody(companyName));
        params.put("registratedAddress", RetroClient.createRequestBody(address));
        params.put("contactNumber", RetroClient.createRequestBody(contactNum));
        params.put("ContactEmail", RetroClient.createRequestBody(email));

        MultipartBody.Part profileImage = null;
        if (!TextUtils.isEmpty(imagePath)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
            profileImage = MultipartBody.Part.createFormData("picOfWarehouse", lic.getName(), requestBody);
        }


        Call<JsonObject> call = api.updateWarehouseProfile(params, profileImage);

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
