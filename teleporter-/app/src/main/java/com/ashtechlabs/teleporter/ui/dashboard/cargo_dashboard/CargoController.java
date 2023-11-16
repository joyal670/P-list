package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.Vendor;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfile;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 24-Jan-17.
 */

public class CargoController implements ICargoController {

    CargoControllerCallback mCallback;


    public CargoController(CargoControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onVendorsDetails() {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        Call<JsonObject> call = api.getJobsWithVendorToken(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    String code = response.body().get("code").getAsString();
                    if (code.equals("fail")) {
                        mCallback.onGetVendorsDetailsFailed(response.body().get("message").getAsString());
                    } else {
                        mCallback.onGetVendorsDetails(getQuotes(response.body().get("vendorservice").getAsJsonArray()));
                        //mCallback.onGetVendorsDetails(response.body());
                    }
                }
                mCallback.dismissLoadingIndicator();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetVendorsDetailsFailed("<<<<");
               // mCallback.onGetVendorsDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }

    @Override
    public void onGetProfileDetails(String token) {

        ApiService api = RetroClient.getApiService();

        Call<VendorProfile> call = api.getVendorProfile(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));

        call.enqueue(new Callback<VendorProfile>() {
            @Override
            public void onResponse(Call<VendorProfile> call, Response<VendorProfile> response) {
                if (response.isSuccessful()) {

                    mCallback.onGetVendorProfileDetails(response.body());

                } else {

                }
            }

            @Override
            public void onFailure(Call<VendorProfile> call, Throwable t) {

            }
        });

    }

    private ArrayList<VendorList> getQuotes(JsonArray quotes) {
//        QuoteBooking quote = new QuoteBooking();
//        for (int i=0;i<quotes.size();i++){
//        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<VendorList>>() {
        }.getType();

        return gson.fromJson(quotes, listType);
    }

}
