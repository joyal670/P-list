package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.add_rate;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class UpdateRateCourierController implements IUpdateRateCourierController {

    UpdateRateCourierControllerCallback mCallback;
    Call<JsonObject> call;


    public UpdateRateCourierController(UpdateRateCourierControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateDriver(String token, String deliveryType, String fromLocation, String toLocation, String minAmount, String perKGAmount,String rateValidity, String duration, int currency) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();
            call = api.submitDeliveryServiceRateCard(token , Integer.valueOf(deliveryType), fromLocation, toLocation, minAmount, perKGAmount,rateValidity, duration, currency,"");


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.dismissLoadingIndicator();
                if (response.isSuccessful()) {
                    mCallback.onGetUpdateDriver(String.valueOf(response.body()));

                } else {
//                    Toast.makeText(HomeDriverActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
            }
        });

    }
}
