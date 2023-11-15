package com.ashtechlabs.teleporter.ui.pricing.prcing_trucking;

import android.util.Log;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.pricing.pojo.Price;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTrucking;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTruckingCode;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class PricingTruckService extends BaseActivity implements PricingTruckingController {

    PricingTruckingControllerCallback mCallback;

    public PricingTruckService(PricingTruckingControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onPricingController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<JsonObject> call = api.getPricingTrucking(token);
        Log.d("OUTTTTT","Success");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.showLoadingDialog(false);
                Log.d("INNNN","Success");
                if (response.isSuccessful()) {     Log.d("@@@@@","Success");
                    JsonObject responseObject = response.body();
                    String status = responseObject.get("code").getAsString();
                    if (status.equals("success")) {
                        JsonObject data = responseObject.getAsJsonObject("value");
                        JsonArray mpratecard = data.getAsJsonArray("mpratecard");
                        ArrayList<PriceTrucking> priceTruckings = getPriceTrucking(mpratecard.toString());

                        mCallback.onGetTruckingPricingDetails(priceTruckings);
                    }


                } else {
                    mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("FFAILLLEEE","Success");
                mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);;
                mCallback.showLoadingDialog(false);
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

    private ArrayList<PriceTrucking> getPriceTrucking(String data) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<PriceTrucking>>() {
        }.getType();

        return gson.fromJson(data, listType);

    }
}
