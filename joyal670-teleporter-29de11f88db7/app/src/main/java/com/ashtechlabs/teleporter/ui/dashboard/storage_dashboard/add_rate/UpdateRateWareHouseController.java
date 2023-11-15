package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.add_rate;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class UpdateRateWareHouseController implements IUpdateRateWareHouseController {

    UpdateRateWareHouseControllerCallback mCallback;
    Call<JsonObject> call;


    public UpdateRateWareHouseController(UpdateRateWareHouseControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateWareHouseDriver(String token, int warehouseId, String perCBMAmount, String dateFrom, String dateTo,int additionalInfo, int perishableDet,String minInsuranceAmt, String insPercent,String rateValidity, String totalCBMAvailable,int currency) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);



        call = api.submitWarehouseServiceRateCard(token, warehouseId, perCBMAmount, dateFrom, dateTo,additionalInfo,perishableDet,minInsuranceAmt,insPercent,rateValidity,totalCBMAvailable,currency, "");


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {
                    mCallback.onGetUpdateDriver(String.valueOf(response.body()));

                } else {
//                    Toast.makeText(HomeDriverActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.showLoadingDialog(false);
            }
        });

    }
}
