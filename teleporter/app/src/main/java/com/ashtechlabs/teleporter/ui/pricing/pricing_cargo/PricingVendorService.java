package com.ashtechlabs.teleporter.ui.pricing.pricing_cargo;

import android.util.Log;
import android.widget.Toast;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendorCode;
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
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PricingVendorService implements PricingVendorController {

    PricingVendorControllerCallback mCallback;

    public PricingVendorService(PricingVendorControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onPricingController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<JsonObject> call = api.getPricingVendor(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.showLoadingDialog(false);

                Log.d("@@@@@","Success");

                if (response.isSuccessful()) {
                    JsonObject responseObject = response.body();
                    String status = responseObject.get("code").getAsString();
                    if (status.equals("success")) {
                        JsonObject data = responseObject.getAsJsonObject("value");
                        JsonArray mpratecard = data.getAsJsonArray("freightforwardratecard");
                        ArrayList<PriceVendor> priceTruckings = getPriceTrucking(mpratecard.toString());


                        mCallback.onGetPricingVendor(priceTruckings);
                } }else {

                    mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);
                    }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.showLoadingDialog(false);
                mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);;
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

    private ArrayList<PriceVendor> getPriceTrucking(String data) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<PriceVendor>>() {
        }.getType();

        return gson.fromJson(data, listType);

    }
}
