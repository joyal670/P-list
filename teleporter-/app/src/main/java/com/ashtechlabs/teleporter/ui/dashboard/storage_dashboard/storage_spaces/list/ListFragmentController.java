package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.list;

import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo.StorageSpace;
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
 * Created by VIDHU on 12/20/2017.
 */

public class ListFragmentController implements IListFragmentController {

    IListFragmentControllerCallback mCallback;


    public ListFragmentController(IListFragmentControllerCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void getWarehouseList(String token) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);
        Call<JsonObject> call = api.getWarehouseDetails(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    String code = response.body().get("code").getAsString();
                    if (code.equals("success")) {
                        mCallback.onListWarehouse(getStorageSpace(response.body().getAsJsonArray("warehouse_det")));
                    } else {
                        mCallback.showErrorMessage(Constants.MESSAGE_SERVER_ERROR);
                    }

                    mCallback.showLoadingDialog(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.showErrorMessage(Constants.MESSAGE_SERVER_ERROR);
                mCallback.showLoadingDialog(false);
            }
        });

    }

    @Override
    public void deleteWarehouse(String token, String id) {

    }

    private ArrayList<StorageSpace> getStorageSpace(JsonArray projects) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<StorageSpace>>() {
        }.getType();

        return gson.fromJson(projects, listType);
    }
}
