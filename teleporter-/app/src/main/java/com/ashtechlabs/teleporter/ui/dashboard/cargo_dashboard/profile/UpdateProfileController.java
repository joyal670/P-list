package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.profile;

import android.text.TextUtils;

import com.google.gson.JsonObject;
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

public class UpdateProfileController  implements IUpdateProfileController {

    UpdateProfileControllerCallback mCallback;
    Call<JsonObject> call;

    public UpdateProfileController(UpdateProfileControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateProfileController(String token, String partner, String companyname, String companyAddress, String contactNum,String email,
                                          String insuranceExpDate, String contactDetail, String poc, String designation,
                                          String bankDetail, String website, String img_insurance_path, String img_license_path, String img_profile_path) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        File lic = new File(img_license_path);
        File ins = new File(img_insurance_path);
        File pro = new File(img_profile_path);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RetroClient.createRequestBody(token));
        params.put("vendorType", RetroClient.createRequestBody(partner));
        params.put("companyName", RetroClient.createRequestBody(companyname));
        params.put("registratedAddress", RetroClient.createRequestBody(companyAddress));
        params.put("contactNumber", RetroClient.createRequestBody(contactNum));
        params.put("ContactEmail", RetroClient.createRequestBody(email));
        params.put("expireInsuranceDate", RetroClient.createRequestBody(insuranceExpDate));
        params.put("contactDetail", RetroClient.createRequestBody(contactDetail));
        params.put("poc", RetroClient.createRequestBody(poc));
        params.put("designation", RetroClient.createRequestBody(designation));
        params.put("bankAccount", RetroClient.createRequestBody(bankDetail));
        params.put("websiteUrl", RetroClient.createRequestBody(website));

        MultipartBody.Part insuranceNumber = null;
        if (!TextUtils.isEmpty(img_insurance_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ins);
            insuranceNumber = MultipartBody.Part.createFormData("insuranceNumber", ins.getName(), requestBody);
        }

        MultipartBody.Part tradeLicenseNumber = null;
        if (!TextUtils.isEmpty(img_license_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
            tradeLicenseNumber = MultipartBody.Part.createFormData("tradeLicenseNumber", lic.getName(), requestBody);
        }

        MultipartBody.Part profileimage = null;
        if (!TextUtils.isEmpty(img_profile_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), pro);
            profileimage = MultipartBody.Part.createFormData("profileimage", pro.getName(), requestBody);
        }


        call = api.updateVendorProfile(params, insuranceNumber, tradeLicenseNumber, profileimage);

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
