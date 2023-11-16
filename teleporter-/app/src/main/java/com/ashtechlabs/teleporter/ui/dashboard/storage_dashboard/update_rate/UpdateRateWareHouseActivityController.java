package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.update_rate;

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

public class UpdateRateWareHouseActivityController implements IUpdateRateWareHouseActivityController{
    private IUpdateRateWareHouseActivityControllerCallback mCallback;
    private  Call<JsonObject> call;

    public UpdateRateWareHouseActivityController(IUpdateRateWareHouseActivityControllerCallback iUpdateRateWareHouseActivityControllerCallback) {
        this.mCallback= iUpdateRateWareHouseActivityControllerCallback;
    }

    @Override
    public void getUpdateWareHouseRateCard(int updateType, String token,String ID,int wareHouseId, String perCBMAmount, String dateFrom, String dateTo,int additionalInfo, int perishableDet,String minInsuranceAmt, String insPercent,String rateValidity, String totalCBMAvailable,int currency) {
        mCallback.showLoadingIndicator();
         ApiService api = RetroClient.getApiService();

        if(updateType == 1) {
            call = api.submitWarehouseServiceRateCard(token, wareHouseId, perCBMAmount, dateFrom, dateTo,additionalInfo, perishableDet,minInsuranceAmt,String.valueOf(insPercent), rateValidity, totalCBMAvailable, currency,ID);
        }else{
            call = api.updateWarehouseServiceRateCard(token,ID,wareHouseId, perCBMAmount, dateFrom, dateTo,additionalInfo, perishableDet,minInsuranceAmt,String.valueOf(insPercent), rateValidity, totalCBMAvailable, currency);
        }

        call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject responseObject = response.body();
                    String status = responseObject.get(Constants.TAG_CODE).getAsString();

                    if (status.equals(Constants.SUCCESS)) {
                        mCallback.dismissLoadingIndicator();
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                        mCallback.onUpdateWareHouseRateSuccess(message);
                    } else {
                        mCallback.dismissLoadingIndicator();
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                        mCallback.onUpdateWareHouseRateFailed(message);
                    }}

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });






    }
}
