package com.ashtechlabs.teleporter.ui.pricing.pricing_ware_house;

import android.util.Log;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PricingWareHouse;
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

public class PricingWareHouseService extends BaseActivity implements PricingWareHouseController {

    PricingWarehouseControllerCallback mCallback;

    public PricingWareHouseService(PricingWarehouseControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onPricingController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<JsonObject> call = api.getPricingWareHouse(token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {

                    String code = response.body().get("code").getAsString();

                    if (code.equals("success")) {
                        JsonObject value = response.body().getAsJsonObject("value");
                        mCallback.onGetPricingWareHouseDetails(getWarehousePricingList(value.getAsJsonArray("warehousingratecard")));
                    } else if (code.equals("fail")) {
                        mCallback.onGetPricingFailed(response.body().get("message").getAsString());
                    } else {
                        mCallback.onGetPricingFailed(Constants.MESSAGE_SERVER_ERROR);
                    }

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

    private ArrayList<PricingWareHouse> getWarehousePricingList(JsonArray warehousingratecard) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<PricingWareHouse>>() {
        }.getType();
        return gson.fromJson(warehousingratecard, listType);
    }
}
