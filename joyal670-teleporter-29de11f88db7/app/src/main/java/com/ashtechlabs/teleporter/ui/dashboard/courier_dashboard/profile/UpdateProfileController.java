package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.profile;

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

public class UpdateProfileController extends BaseActivity implements IUpdateDriverProfileController {

    UpdateProfileControllerCallback mCallback;
    Call<JsonObject> call;

    public UpdateProfileController(UpdateProfileControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateProfileController(String token, String vehicletype, String city, String address,String phone,String email, String zipcode, String licenseexpired, String vehicleno, String insuranceexpiry, String title, String img_license_path, String img_insurance_path, String img_profile_path) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        File lic = new File(img_license_path);
        File ins = new File(img_insurance_path);
        File pro = new File(img_profile_path);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RetroClient.createRequestBody(token));
        params.put("vehicleType", RetroClient.createRequestBody(vehicletype));
        params.put("cityOfWork", RetroClient.createRequestBody(city));
        params.put("companyAddress", RetroClient.createRequestBody(address));
        params.put("ContactNumber", RetroClient.createRequestBody(phone));
        params.put("ContactEmail", RetroClient.createRequestBody(email));
        params.put("zipCode", RetroClient.createRequestBody(zipcode));
        params.put("expireLicenseDate", RetroClient.createRequestBody(licenseexpired));
        params.put("vehicleNumber", RetroClient.createRequestBody(vehicleno));
        params.put("expireInsuranceDate", RetroClient.createRequestBody(insuranceexpiry));
        params.put("name", RetroClient.createRequestBody(title));


        MultipartBody.Part licensePlate = null;
        if (!TextUtils.isEmpty(img_license_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
            licensePlate = MultipartBody.Part.createFormData("licensePlate", lic.getName(), requestBody);
        }

        MultipartBody.Part vehicleInsurance = null;
        if (!TextUtils.isEmpty(img_insurance_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ins);
            vehicleInsurance = MultipartBody.Part.createFormData("vehicleInsurance", ins.getName(), requestBody);
        }

        MultipartBody.Part profileimage = null;
        if (!TextUtils.isEmpty(img_profile_path)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), pro);
            profileimage = MultipartBody.Part.createFormData("profileimage", pro.getName(), requestBody);
        }

        call = api.updateDriverProfile(params, licensePlate, vehicleInsurance, profileimage);

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
