package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.order_info_detail;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jesna on 6/21/2017.
 */

public class OrderDetailWareHouseActivityController implements IOrderDetailWareHouseActivityController{
    IOrderDetailWareHouseActivityControllerCallback callback;
    Call<JsonObject> call;
    public OrderDetailWareHouseActivityController(IOrderDetailWareHouseActivityControllerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onDeliveryAndPickupAddressWareHouse(String jobid) {
        ApiService api = RetroClient.getApiService();
        callback.showLoadingDialog(true);

        call = api.getDeliveryandPickupAddress(jobid);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //mCallback.onGetUpdateVendor(String.valueOf(response.body()));
                if (response.isSuccessful()) {
                    callback.getDeliveryAndPickupAddressWareHouse(String.valueOf(response.body()));
                }
                else {
                    callback.getDeliveryAndPickupAddressFailedWareHouse("Failed");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.showLoadingDialog(false);
            }
        });
    }
}
