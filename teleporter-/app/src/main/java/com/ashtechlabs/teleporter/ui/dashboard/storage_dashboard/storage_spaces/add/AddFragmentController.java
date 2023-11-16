package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.add;

import android.text.TextUtils;
import android.util.Log;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
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
 * Created by VIDHU on 12/20/2017.
 */

public class AddFragmentController implements IAddFragmentController {

    IAddFragmentControllerCallback mCallback;

    public AddFragmentController(IAddFragmentControllerCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void addWareHouse(String token, String warehouseCapacity, String spaceAvailable, String warehouseLocation, String warehouseName, String insuranceExpDate, String tradeLicenceExpDate,
                             String picOfWarehouse, String tradeLicenseNumber, String insuranceNumber) {

        mCallback.showLoadingDialog(true);
        ApiService api = RetroClient.getApiService();

        File lic = new File(tradeLicenseNumber);
        File ins = new File(insuranceNumber);
        File ware = new File(picOfWarehouse);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RetroClient.createRequestBody(token));
        params.put("warehouseCapacity", RetroClient.createRequestBody(warehouseCapacity));
        params.put("spaceAvailable", RetroClient.createRequestBody(spaceAvailable));
        params.put("warehouseLocation", RetroClient.createRequestBody(warehouseLocation));
        params.put("warehouse_name", RetroClient.createRequestBody(warehouseName));
        params.put("insurance_exp_date", RetroClient.createRequestBody(insuranceExpDate));
        params.put("trade_license_expiry_date", RetroClient.createRequestBody(tradeLicenceExpDate));
        Log.d("D/OkHttp:", warehouseName + "..." + insuranceExpDate + "...." + tradeLicenceExpDate);
        MultipartBody.Part tradeLicense = null;
        if (!TextUtils.isEmpty(tradeLicenseNumber)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), lic);
            tradeLicense = MultipartBody.Part.createFormData("tradeLicenseNumber", lic.getName(), requestBody);
            Log.d("D/OkHttp:", tradeLicense + "");
        }


        MultipartBody.Part insurance = null;
        if (!TextUtils.isEmpty(insuranceNumber)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ins);
            insurance = MultipartBody.Part.createFormData("insuranceNumber", ins.getName(), requestBody);
            Log.d("D/OkHttp:", insurance + "");

        }
        MultipartBody.Part warehousePic = null;
        if (!TextUtils.isEmpty(picOfWarehouse)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ware);
            warehousePic = MultipartBody.Part.createFormData("picOfWarehouse", ware.getName(), requestBody);
            Log.d("D/OkHttp:", warehousePic + "");

        }

        Call<JsonObject> call = api.submitWarehouse(params, tradeLicense, insurance, warehousePic);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    mCallback.warehouseAdded("Successfully added");
                }

                mCallback.showLoadingDialog(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("D/OkHttp:", "Failure");
                mCallback.showLoadingDialog(false);
            }
        });
    }
}
