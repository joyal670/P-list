package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingOrder;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfile;
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
 * Created by VIDHU on 10/27/2016.
 */

class TruckingController implements ITruckingController {

    TruckingControllerCallback mCallback;


    public TruckingController(TruckingControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onGetJobsWithDriverToken(String token) {

        mCallback.showLoadingIndicator();
        ApiService api = RetroClient.getApiService();
        api.getJobsWithTruckingToken(token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {


                    String code = response.body().get("code").getAsString();
                    if (code.equals("fail")) {
                        mCallback.onGetDriverDetailsFailed(response.body().get("message").getAsString());
                    } else {
                        mCallback.onGetDriverDetails(getQuotes(response.body().get("mpservice").getAsJsonArray()));
                        //mCallback.onGetVendorsDetails(response.body());
                    }

                }

                mCallback.dismissLoadingIndicator();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetDriverDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }

    @Override
    public void onGetProfileDetails(String token) {
        ApiService api = RetroClient.getApiService();

        Call<TruckingProfile> call = api.getTruckingProfile(token);

        call.enqueue(new Callback<TruckingProfile>() {
            @Override
            public void onResponse(Call<TruckingProfile> call, Response<TruckingProfile> response) {
                if (response.isSuccessful()) {

                    mCallback.onGetDriverProfileDetails(response.body());

                } else {
//                    Toast.makeText(HomeDriverActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TruckingProfile> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
            }
        });



    }

    private ArrayList<TruckingMpService> getQuotes(JsonArray quotes) {
//        QuoteBooking quote = new QuoteBooking();
//        for (int i=0;i<quotes.size();i++){
//        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<TruckingMpService>>() {
        }.getType();

        return gson.fromJson(quotes, listType);
    }
}
