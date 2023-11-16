package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.add;

import android.text.TextUtils;
import android.util.Log;

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
 * Created by VIDHU on 12/20/2017.
 */

public class AddFragmentController implements IAddFragmentController {

    IAddFragmentControllerCallback mCallback;

    public AddFragmentController(IAddFragmentControllerCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void addVehicle(String token, int vehicleType, int vehicleSubType, String vehicleNumber, String insuranceNumber, String insuranceExpDate) {

        mCallback.showLoadingDialog(true);
        ApiService api = RetroClient.getApiService();
        File ins = new File(insuranceNumber);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RetroClient.createRequestBody(token));
        params.put("vehicleType", RetroClient.createRequestBody(String.valueOf(vehicleType)));
        params.put("vehiclesubType", RetroClient.createRequestBody(String.valueOf(vehicleSubType)));
        params.put("vehicleNumber", RetroClient.createRequestBody(vehicleNumber));
        params.put("expireInsuranceDate", RetroClient.createRequestBody(insuranceExpDate));


        MultipartBody.Part insurance = null;
        if (!TextUtils.isEmpty(insuranceNumber)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), ins);
            insurance = MultipartBody.Part.createFormData("vehicleInsurance", ins.getName(), requestBody);
        }

        Call<JsonObject> call = api.submitVehicle(params, insurance);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    String code = response.body().get("code").getAsString();
                    if (code.equals("success")) {
                        mCallback.vehicleAdded("Successfully added");
                    } else {
                        mCallback.showErrorMessage(Constants.MESSAGE_SERVER_ERROR);
                    }

                } else {
                    Log.e("ERROR_", response.message());
                    mCallback.showErrorMessage(Constants.MESSAGE_SERVER_ERROR);
                }
                mCallback.showLoadingDialog(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.showLoadingDialog(false);
                Log.e("ERROR", t.getMessage());
                mCallback.showErrorMessage(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
