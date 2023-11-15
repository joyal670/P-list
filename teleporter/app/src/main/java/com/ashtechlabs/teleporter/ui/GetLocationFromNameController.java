package com.ashtechlabs.teleporter.ui;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.TeleporterApp;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.ui.splash.IGetLocationFromNameController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class GetLocationFromNameController extends BaseActivity implements IGetLocationFromNameController {

    IGetLocationFromNameCallBack mCallback;

    public GetLocationFromNameController(IGetLocationFromNameCallBack callback) {
        mCallback = callback;
    }


    @Override
    public void getLatLongFromPlace(String locationName) {

        ApiService apiService =
                TeleporterApp.getMapClient().create(ApiService.class);


        Call<JsonObject> call = apiService.getLatLongFromAddress("AIzaSyCLim2cPziQUWCjGHzdGy6E1CNGctNimvM", "Kurnool", false);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //101 and 117
                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    JsonArray jsonArrayResults = jsonObject.getAsJsonArray("results");
                    JsonObject jsonObjectResults = jsonArrayResults.get(0).getAsJsonObject();
                    JsonObject jsonArrayGeometry = jsonObjectResults.getAsJsonObject("geometry");
                    JsonObject jsonArrayLocation = jsonArrayGeometry.getAsJsonObject("location");

                    mCallback.getLatLong(new LatLng((jsonArrayLocation.get("lat").getAsDouble()), jsonArrayLocation.get("lng").getAsDouble()));

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG >> ", t.toString());
            }
        });

    }
}
