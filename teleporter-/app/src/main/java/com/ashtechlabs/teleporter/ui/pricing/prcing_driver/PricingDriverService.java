package com.ashtechlabs.teleporter.ui.pricing.prcing_driver;

import android.util.Log;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.pricing.pojo.Price;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class PricingDriverService extends BaseActivity implements PricingDriverController {

    PricingDriverControllerCallback mCallback;

    public PricingDriverService(PricingDriverControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onPricingController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<Price> call = api.getUploadedRates(token);

        call.enqueue(new Callback<Price>() {
            @Override
            public void onResponse(Call<Price> call, Response<Price> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {
                    Log.d("@@@@@", "Success");
                    mCallback.onGetPricingDetails(response.body().getPricing());

                } else {

                    mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<Price> call, Throwable t) {
                mCallback.showLoadingDialog(false);
                mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }

    @Override
    public void onDeleteRateCard(String token, String id) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<JsonObject> call = api.deleteRateCard(token,id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {
                    Log.d("@@@@@", "Success");
                    mCallback.onRateCardDeleted("RateCard Deleted Successfully");
                } else {
                    mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.showLoadingDialog(false);
                mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
