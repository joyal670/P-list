package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_rate;

import android.util.Log;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jesna on 6/14/2017.
 */

public class UpdateRateVendorActivityController implements IUpdateRateVendorActivityController{
    private IUpdateRateVendorActivityControllerCallback mCallback;
    private  Call<JsonObject> call;

    public UpdateRateVendorActivityController(IUpdateRateVendorActivityControllerCallback iUpdateRateVendorActivityControllerCallback) {
        this.mCallback= iUpdateRateVendorActivityControllerCallback;
    }

    @Override
    public void getUpdateVendorRateCard(int rateCardType, String token,String rateCardId, String serviceType, String fromLocation, String toLocation,int additional_info, int perishable_det, String minAmount, String perKGAmount, String rateValidity,
                                        String insurancePercentage, String minInsuranceAmt, String duration, int currency) {
        mCallback.showLoadingIndicator();
         ApiService api = RetroClient.getApiService();
        if(rateCardType == 1){
            call = api.submitCargoRateCard(token, serviceType ,fromLocation ,toLocation, additional_info, perishable_det, minAmount, perKGAmount, rateValidity, insurancePercentage, minInsuranceAmt, duration, currency,rateCardId);
        }else{
            call = api.updateCargoRateCard(token, rateCardId, serviceType ,fromLocation ,toLocation, additional_info, perishable_det, minAmount, perKGAmount, rateValidity, insurancePercentage, minInsuranceAmt, duration, currency);
        }

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject responseObject = response.body();
                    String status = responseObject.get(Constants.TAG_CODE).getAsString();

                    if (status.equals(Constants.SUCCESS)) {
                        mCallback.dismissLoadingIndicator();
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                        mCallback.onUpdateVendorRateSuccess(message);
                    } else {
                        mCallback.dismissLoadingIndicator();
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                        mCallback.onUpdateVendorRateFailed(message);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

    }
}
