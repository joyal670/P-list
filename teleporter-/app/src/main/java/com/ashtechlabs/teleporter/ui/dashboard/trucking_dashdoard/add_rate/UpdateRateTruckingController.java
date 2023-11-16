package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.add_rate;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class UpdateRateTruckingController implements IUpdateRateTruckingController {

    UpdateRateTruckingControllerCallback mCallback;
    Call<JsonObject> call;


    public UpdateRateTruckingController(UpdateRateTruckingControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateTruckingRateCard(String token, int vehicleType, int subVehicleType, String fromLocation, String toLocation, String amount, String rateValidity, String insPercent, String insuranceMinAmt, String duration, int currency) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        call = api.submitMoverPackerServiceRateCard(token, vehicleType, subVehicleType, fromLocation, toLocation, amount,rateValidity, insPercent, insuranceMinAmt, duration, currency, "");


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
