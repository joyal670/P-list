package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.add_rate;

import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class UpdateRateVendorsController implements IUpdateRateVendorsController {

    UpdateRateVendorsControllerCallback mCallback;
    Call<JsonObject> call;

    public UpdateRateVendorsController(UpdateRateVendorsControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateVendor(String token, String serviceType, String fromLocation, String toLocation,int additional_info, int perishable_det, String minAmount, String perKGAmount, String rateValidity,String insurancePercentage, String minInsuranceAmt, String duration, int currency) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        call = api.submitCargoRateCard(token, serviceType, fromLocation, toLocation,additional_info, perishable_det, minAmount, perKGAmount,rateValidity,insurancePercentage, minInsuranceAmt, duration,currency,"");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {
                    mCallback.onGetUpdateVendor(String.valueOf(response.body()));

                } else {

                    mCallback.showMessage(Constants.MESSAGE_NO_NETWORK);
//                    Toast.makeText(HomeDriverActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.showMessage(Constants.MESSAGE_SERVER_ERROR);
                mCallback.showLoadingDialog(false);
            }
        });

    }
}
