package com.ashtechlabs.teleporter.ui.signup.registrationwarehouse;

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

public class RegisterWareHouseServiceCopy extends BaseActivity implements RegisterController {

    RegisterControllerCallback mCallback;

    Call<JsonObject> call;

    public RegisterWareHouseServiceCopy(RegisterControllerCallback callback) {
        mCallback = callback;
    }

//    @Override
//    public void onRegisterController(String storageType, String companyName, String address, String contactNum, String insuranceExpDate, String path1, String path2, String path3) {
//
//        ApiService api = RetroClient.getApiService();
//        mCallback.showLoadingIndicator();
//
//        File lic = new File(path2);
//        File ins = new File(path3);
//        File ware = new File(path1);
//
////        RequestBody storage = RequestBody.create(MediaType.parse("text/plain"), storageType);
////        RequestBody company = RequestBody.create(MediaType.parse("text/plain"), companyName);
////        RequestBody Address = RequestBody.create(MediaType.parse("text/plain"), address);
////        RequestBody contact = RequestBody.create(MediaType.parse("text/plain"), contactNum);
////        RequestBody insurance = RequestBody.create(MediaType.parse("text/plain"), insuranceExpDate);
////
////        //---------------------------------------------------------------------------------------------------------
////        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
////        MultipartBody.Part fileToUploadlicense = MultipartBody.Part.createFormData("tradeLicenseNumber", lic.getName(), requestBody);
////
////        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), ins);
////        MultipartBody.Part fileToUploadinsurance = MultipartBody.Part.createFormData("insuranceNumber", ins.getName(), requestBody1);
////
////        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), ware);
////        MultipartBody.Part fileToUploadwarehouse = MultipartBody.Part.createFormData("picOfWarehouse", ware.getName(), requestBody2);
//        //----------------------------------------------------------------------------------------------------------
//
//        Map<String, RequestBody> params = new HashMap<>();
//        params.put("storageType", RetroClient.createRequestBody(storageType));
//        params.put("companyName", RetroClient.createRequestBody(companyName));
//        params.put("registratedAddress", RetroClient.createRequestBody(address));
//        params.put("contactNumber", RetroClient.createRequestBody(contactNum));
//        params.put("expireInsuranceDate", RetroClient.createRequestBody(insuranceExpDate));
//
//        MultipartBody.Part tradeLicenseNumber = null;
//        if (!TextUtils.isEmpty(path1)) {
//            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
//            tradeLicenseNumber = MultipartBody.Part.createFormData("tradeLicenseNumber", lic.getName(), requestBody);
//        }
//
//        MultipartBody.Part insuranceNumber = null;
//        if (!TextUtils.isEmpty(path2)) {
//            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ins);
//            insuranceNumber = MultipartBody.Part.createFormData("insuranceNumber", ins.getName(), requestBody);
//        }
//        MultipartBody.Part picOfWarehouse = null;
//        if (!TextUtils.isEmpty(path3)) {
//            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ware);
//            picOfWarehouse = MultipartBody.Part.createFormData("picOfWarehouse", ware.getName(), requestBody);
//        }
//
//        call = api.registerWarehouse(params, tradeLicenseNumber, insuranceNumber, picOfWarehouse);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//
//                    mCallback.onGetRegisterDetails(response.body());
//                    mCallback.dismissLoadingIndicator();
//                } else {
//                    mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
//                    mCallback.dismissLoadingIndicator();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                mCallback.onRegisterFailed(Constants.MESSAGE_SERVER_ERROR);
//                mCallback.dismissLoadingIndicator();
//            }
//        });
//    }

    @Override
    public void onRegisterController(String companyName, String address, String contactNum,String email, String path1) {

    }
}