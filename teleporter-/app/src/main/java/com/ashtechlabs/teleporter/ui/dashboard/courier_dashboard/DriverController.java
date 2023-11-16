package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VIDHU on 10/27/2016.
 */

class DriverController  implements IDriverController {

    DriverControllerCallback mCallback;


    public DriverController(DriverControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onGetJobsWithDriverToken(String token) {

        mCallback.showLoadingIndicator();
        ApiService api = RetroClient.getApiService();
        api.getJobsWithDriverToken(token).enqueue(new Callback<DriverOrder>() {
            @Override
            public void onResponse(Call<DriverOrder> call, Response<DriverOrder> response) {
                if (response.isSuccessful()) {

                    String code = response.body().getCode();
                    if(code.equals("fail")){
                        mCallback.onGetDriverDetailsFailed(response.body().getMessage());
                    }else{
                        mCallback.onGetDriverDetails(response.body());
                    }

                }

                mCallback.dismissLoadingIndicator();
            }

            @Override
            public void onFailure(Call<DriverOrder> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetDriverDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }

    @Override
    public void onGetProfileDetails(String token) {
        ApiService api = RetroClient.getApiService();

        Call<DriverProfile> call = api.getDriverProfile(token);

        call.enqueue(new Callback<DriverProfile>() {
            @Override
            public void onResponse(Call<DriverProfile> call, Response<DriverProfile> response) {
                if (response.isSuccessful()) {

                    mCallback.onGetDriverProfileDetails(response.body());

                } else {
//                    Toast.makeText(HomeDriverActivity.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DriverProfile> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
            }
        });

    }
}
