package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.order_info_detail;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jesna on 6/21/2017.
 */

public class OrderDetailDriverActivityController implements IOrderDetailDriverActivityController{
    IOrderDetailDriverActivityControllerCallback callback;
    Call<JsonObject> call;
    public OrderDetailDriverActivityController(IOrderDetailDriverActivityControllerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onDeliveryAndPickupAddressDriver(String jobid) {
        ApiService api = RetroClient.getApiService();
        callback.showLoadingDialog(true);

        call = api.getDeliveryandPickupAddress(jobid);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //mCallback.onGetUpdateVendor(String.valueOf(response.body()));
                if (response.isSuccessful()) {
                    callback.getDeliveryAndPickupAddressDriver(String.valueOf(response.body()));
                }
                else {
                    callback.getDeliveryAndPickupAddressDriverFailed("Failed");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.showLoadingDialog(false);
            }
        });
    }
}
