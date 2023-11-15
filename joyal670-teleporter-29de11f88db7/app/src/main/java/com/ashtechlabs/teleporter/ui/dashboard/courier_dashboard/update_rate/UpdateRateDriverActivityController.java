package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.update_rate;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * Created by Jesna on 6/14/2017.
 */

public class UpdateRateDriverActivityController implements IUpdateRateDriverActivityController{
    private IUpdateRateDriverActivityControllerCallback mCallback;
    Call<JsonObject> call;

    public UpdateRateDriverActivityController(IUpdateRateDriverActivityControllerCallback iUpdateRateDriverActivityControllerCallback) {
        this.mCallback= iUpdateRateDriverActivityControllerCallback;
    }

    @Override
    public void getUpdateRateCard(int updateType, String token, int deliveryType, String fromLocation, String toLocation, String minAmount, String perKGAmount, String rateValidity, String duration, int currency, String ID) {
        mCallback.showLoadingIndicator();

            ApiService api = RetroClient.getApiService();

            if(updateType==1){
                call = api.submitDeliveryServiceRateCard(token, deliveryType, fromLocation, toLocation, minAmount, perKGAmount,rateValidity, duration, currency, ID);

            }else{
                call = api.updateDeliveryServiceRateCard(token,ID, deliveryType, fromLocation, toLocation, minAmount, perKGAmount,rateValidity, duration,currency);
            }


            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject responseObject = response.body();
                    String status = responseObject.get(Constants.TAG_CODE).getAsString();

                    if (status.equals(Constants.SUCCESS)) {
                        mCallback.dismissLoadingIndicator();
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                        mCallback.onUpdateRateSuccess(message);
                    } else {
                        mCallback.dismissLoadingIndicator();
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                        mCallback.onUpdateRateFailed(message);
                    }}

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
    }
}
