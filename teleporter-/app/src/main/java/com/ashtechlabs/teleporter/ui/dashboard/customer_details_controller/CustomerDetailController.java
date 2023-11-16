package com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller;

import android.util.Log;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class CustomerDetailController implements ICustomerDetailController {

    CustomerDetailControllerCallback mCallback;


    public CustomerDetailController(CustomerDetailControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onCustomerController(String token, String orderid, String ordertype) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        Call<JsonObject> call = api.getCustomerDetail(token, orderid, ordertype);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    Log.e("RESPONSE >> ", jsonObject.toString());
                    String code = jsonObject.get(Constants.TAG_CODE).getAsString();
                    if (code.equals(Constants.SUCCESS)) {

                        JsonObject value = jsonObject.getAsJsonObject(Constants.TAG_VALUE);
                        String name = value.get(Constants.TAG_CUSTOMER_NAME).getAsString();
                        String phone = value.get(Constants.TAG_MOBILE_NUMBER).getAsString();
                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetCustomerDetails(name,phone);

                    }else{
                        mCallback.dismissLoadingIndicator();
                        mCallback.onGetCustomerDetailsFailed(jsonObject.get(Constants.TAG_MESSAGE).getAsString());

                    }

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetCustomerDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetCustomerDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }


}
