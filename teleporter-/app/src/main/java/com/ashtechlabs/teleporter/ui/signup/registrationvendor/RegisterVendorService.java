package com.ashtechlabs.teleporter.ui.signup.registrationvendor;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.GlobalUtils;

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

public class RegisterVendorService extends BaseActivity implements RegisterController {

    RegisterControllerCallback mCallback;
    int modes = GlobalUtils.MODE_COURIER;
    Call<JsonObject> call;

    public RegisterVendorService(RegisterControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onRegisterController(String vendorType, String companyName, String companyAddress, String contactNum,String email, String insuranceExpDate, String contactDetail, String poc, String designation,  String bankDetail, String website, String path1, String path2, String path3) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        File lic = new File(path1);
        File ins = new File(path2);
        File pro = new File(path3);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("vendorType", RetroClient.createRequestBody(vendorType));
        params.put("companyName", RetroClient.createRequestBody(companyName));
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
        if (!TextUtils.isEmpty(path1)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ins);
            insuranceNumber = MultipartBody.Part.createFormData("insuranceNumber", ins.getName(), requestBody);
        }

        MultipartBody.Part tradeLicenseNumber = null;
        if (!TextUtils.isEmpty(path2)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
            tradeLicenseNumber = MultipartBody.Part.createFormData("tradeLicenseNumber", lic.getName(), requestBody);
        }

        MultipartBody.Part profileimage = null;
        if (!TextUtils.isEmpty(path3)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), pro);
            profileimage = MultipartBody.Part.createFormData("profileimage", pro.getName(), requestBody);
        }
        call = api.registerVendor(params, insuranceNumber, tradeLicenseNumber, profileimage);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetRegisterDetails(response.body());

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
