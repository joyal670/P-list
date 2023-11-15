package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_track_trace;

import android.util.Log;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jesna on 6/21/2017.
 */

public class DeliveryMileStoneActivityController implements IDeliveryMileStoneActivityController{
IDeliveryMileStoneActivityControllerCallback mCallback;

    public DeliveryMileStoneActivityController(IDeliveryMileStoneActivityControllerCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onTrackTrace(String oderID, String orderStatus, String message) {
        mCallback.showLoadingDialog(true);


            ApiService api = RetroClient.getApiService();
            api.getorderUpdate(oderID,orderStatus, message).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject responseObject = response.body();
                    String status = responseObject.get(Constants.TAG_CODE).getAsString();

                    if (status.equals(Constants.SUCCESS)) {
                        mCallback.showLoadingDialog(false);
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                        //mCallback.onUpdateRateSuccess(message);
                        mCallback.getTrackTrace(message);
                    } else {
                        mCallback.showLoadingDialog(false);
                        String message = responseObject.get(Constants.TAG_MESSAGE).getAsString();
                       // mCallback.onUpdateRateFailed(message);
                        mCallback.getTrackTraceFailed(message);
                    }}

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    mCallback.showLoadingDialog(false);
                }
            });

    }

    @Override
    public void getTrackTrace(String oderID) {
        mCallback.showLoadingDialog(true);
        ApiService api = RetroClient.getApiService();

        api.getTrackingStatus(oderID).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //{"code":"success","order_status":"4"}
                if (response.isSuccessful()) {
                    JsonObject myBookings = response.body();
                    String code = myBookings.get("code").getAsString();
                    Log.e("RESPONSE >> ", myBookings.toString());
                    if (code.equals(Constants.SUCCESS)) {
                        mCallback.showLoadingDialog(false);
                        if(myBookings.has("message")){
                            mCallback.onGetOrderStatus(myBookings.get("order_status").getAsInt(), myBookings.get("message").getAsString());
                        }else{
                            mCallback.onGetOrderStatus(myBookings.get("order_status").getAsInt());
                        }


                    } else {
                        mCallback.showLoadingDialog(false);
                        Log.e("FAIL >> ", myBookings.get("message").getAsString());
                        mCallback.getTrackTraceFailed(myBookings.get("message").getAsString());
                    }

                } else {
                    mCallback.showLoadingDialog(false);
                    mCallback.getTrackTraceFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG >> ", t.toString());
                mCallback.showLoadingDialog(false);
            }
        });

    }
}
