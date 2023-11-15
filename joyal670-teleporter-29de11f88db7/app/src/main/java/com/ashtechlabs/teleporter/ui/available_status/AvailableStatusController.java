package com.ashtechlabs.teleporter.ui.available_status;

import android.util.Log;

import com.ashtechlabs.teleporter.TeleporterApp;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VIDHU on 12/18/2017.
 */

public class AvailableStatusController implements IAvailableStatusController {

    IAvailableStatusControllerCallback mCallback;

    public AvailableStatusController(IAvailableStatusControllerCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onGetAvailableStatus(String token, int serviceType) {

        ApiService apiService =
                RetroClient.getApiService();

        Call<JsonObject> call = apiService.getDutyStatus(token, serviceType);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //101 and 117
                if (response.isSuccessful()) {
                    String code = response.body().get("code").getAsString();
                    if (code.equals("success")) {
                        JsonArray value = response.body().get("value").getAsJsonArray();
                        JsonObject jsonObject = value.get(0).getAsJsonObject();
                        mCallback.onAvailableStatusChanged(jsonObject.get("duty_status").getAsInt(),jsonObject.get("MobileNum").getAsString(), "Success");
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG >> ", t.toString());
            }
        });

    }

    @Override
    public void onSetAvailableStatus(String token, final int serviceType, final int dutyStatus) {
        ApiService apiService =
                RetroClient.getApiService();

        Call<JsonObject> call = apiService.setDutyStatus(token, serviceType, dutyStatus);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //101 and 117
                if (response.isSuccessful()) {

//                    "{
//                    ""code"": ""success"",
//                            ""message"": ""Successfully updated""
//                }"
                    String code = response.body().get("code").getAsString();
                    if (code.equals("success")) {
                        mCallback.onAvailableStatusChanged(dutyStatus, response.body().get("message").getAsString());
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG >> ", t.toString());
            }
        });

    }
}
