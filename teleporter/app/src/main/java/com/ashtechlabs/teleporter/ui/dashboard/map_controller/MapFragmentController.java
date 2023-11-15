package com.ashtechlabs.teleporter.ui.dashboard.map_controller;

import android.util.Log;

import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.TeleporterApp;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 15-Feb-17.
 */

public class MapFragmentController extends BaseActivity implements IMapFragmentController {

    MapFragmentControllerCallback mCallback;


    public MapFragmentController(MapFragmentControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onUpdateDriverLocation(String token,int mode, String latitude, String longitude) {

        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = null;
        mCallback.showLoadingIndicator();
        if(mode== GlobalUtils.MODE_TRUCKING){
            call = api.updateTruckingLocation(token, latitude, longitude);
        }else{
            call = api.updateDriverLocation(token, latitude, longitude);
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    Log.e("RESPONSE >> ", jsonObject.toString());
                    String code = jsonObject.get(Constants.TAG_CODE).getAsString();
                    if (code.equals(Constants.SUCCESS)) {
                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetMapDetails(jsonObject.get(Constants.TAG_MESSAGE).getAsString());

                    } else {
                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetMapDetailsFailed(jsonObject.get(Constants.TAG_MESSAGE).getAsString());
                    }

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetMapDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
            }
        });

    }

    @Override
    public void onUpdateVendorLocation(String token, String latitude, String longitude) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();
        Call<JsonObject> call = api.updateVendorLocation(token, latitude, longitude);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    Log.e("RESPONSE >> ", jsonObject.toString());
                    String code = jsonObject.get(Constants.TAG_CODE).getAsString();
                    if (code.equals(Constants.SUCCESS)) {
                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetMapDetails(jsonObject.get(Constants.TAG_MESSAGE).getAsString());

                    } else {
                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetMapDetailsFailed(jsonObject.get(Constants.TAG_MESSAGE).getAsString());
                    }

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetMapDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
            }
        });

    }

    @Override
    public void getFromLatLng(String locationName) {

        ApiService apiService =
                TeleporterApp.getMapClient().create(ApiService.class);

        Call<JsonObject> call = apiService.getLatLongFromAddress("AIzaSyB-jeq5tV0q137nBMN8-WiibfHgpjVV0Xc", locationName, false);
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

                    mCallback.onGetFromLatLng(new LatLng(jsonArrayLocation.get("lat").getAsDouble(),
                            jsonArrayLocation.get("lng").getAsDouble()));
                    Log.d("LAT", jsonArrayLocation.get("lat").toString());
                    Log.d("LAT", jsonArrayLocation.get("lng").toString());
                } else {
                    Log.e("TAG >> ", "FAILED");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG >> ", t.toString());
            }
        });

    }

    @Override
    public void getToLatLng(String locationName) {
        ApiService apiService =
                TeleporterApp.getMapClient().create(ApiService.class);

        Call<JsonObject> call = apiService.getLatLongFromAddress("AIzaSyB-jeq5tV0q137nBMN8-WiibfHgpjVV0Xc", locationName, false);
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

                    mCallback.onGetToLatLng(new LatLng(jsonArrayLocation.get("lat").getAsDouble(),
                            jsonArrayLocation.get("lng").getAsDouble()));
                    Log.d("LAT", jsonArrayLocation.get("lat").toString());
                    Log.d("LAT", jsonArrayLocation.get("lng").toString());
                } else {
                    Log.e("TAG >> ", "FAILED");
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
