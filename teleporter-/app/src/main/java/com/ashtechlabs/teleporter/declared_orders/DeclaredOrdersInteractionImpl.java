package com.ashtechlabs.teleporter.declared_orders;

import android.util.Log;

import com.ashtechlabs.teleporter.TeleporterApp;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.DeclaredOrders;
import com.ashtechlabs.teleporter.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VIDHU on 1/19/2017.
 */

public class DeclaredOrdersInteractionImpl implements DeclaredOrdersInteraction {

    @Override
    public void getDeclaredOrders(int mode, String token, final OnDeclaredOrdersListener listener) {
        listener.showProgress();
        ApiService apiService =
                RetroClient.getApiService();
        Call<JsonObject> call = null;
        if (mode == 0) {
            call = apiService.getDriverDeclaredOrders(token, GlobalPreferManager.getString(GlobalPreferManager.Keys.MOBILE_COURIER, ""));
        } else if (mode == 1) {
            call = apiService.getTruckingDeclaredOrders(token, GlobalPreferManager.getString(GlobalPreferManager.Keys.MOBILE_TRUCKING, ""));
        } else if (mode == 2) {
            call = apiService.getFreightForwardDeclaredOrders(token, GlobalPreferManager.getString(GlobalPreferManager.Keys.MOBILE_CARGO, ""));
        } else {
            call = apiService.getWareHouseDeclaredOrders(token, GlobalPreferManager.getString(GlobalPreferManager.Keys.MOBILE_STORAGE, ""));
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    JsonObject myBookings = response.body();
                    String code = myBookings.get("code").getAsString();
                    Log.e("RESPONSE >>## ", myBookings.toString());
                    if (code.equals(Constants.SUCCESS)) {

                        listener.dismissProgress();
                        JsonObject value = myBookings.getAsJsonObject("value");
                        ArrayList<DeclaredOrders> orders = getMyBookingData(value);
                        listener.onSuccess(orders);

                    } else {
                        listener.dismissProgress();
//                        Log.e("FAIL >> ", myBookings.get("value").getAsString());
                        listener.onFail("Invalid Token");
                    }

                } else {
                    listener.dismissProgress();
                    listener.onFail(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG >> ", t.toString());
            }
        });
    }


    private ArrayList<DeclaredOrders> getMyBookingData(JsonObject value) {
        ArrayList<DeclaredOrders> declaredOrders = new ArrayList<>();
        if (value.has("deliveryservice")) {
            JsonArray deliveryService = value.getAsJsonArray("deliveryservice");
            for (int i = 0; i < deliveryService.size(); i++) {
                JsonObject jsonObject = deliveryService.get(i).getAsJsonObject();
                DeclaredOrders orders = new DeclaredOrders();
                orders.setServiceType(Constants.SERVICE_COURIER);
                orders.setId(jsonObject.get("ID").getAsString());
                orders.setItemType(jsonObject.get("ItemType").getAsInt());
                orders.setFromLocation(jsonObject.get("FromLocation").getAsString());
                orders.setToLocation(jsonObject.get("ToLocation").getAsString());
                orders.setDate(jsonObject.get("Date").getAsString());
                orders.setWeight(jsonObject.get("Weight").getAsString());
                declaredOrders.add(orders);
            }
        }
        if (value.has("mpservice")) {
            JsonArray mpService = value.getAsJsonArray("mpservice");
            for (int i = 0; i < mpService.size(); i++) {
                JsonObject jsonObject = mpService.get(i).getAsJsonObject();
                DeclaredOrders orders = new DeclaredOrders();
                orders.setServiceType(Constants.SERVICE_TRUCKING);
                orders.setId(jsonObject.get("ID").getAsString());
                orders.setVehicleType(jsonObject.get("VehicleType").getAsString());
                orders.setSubVehicleType(jsonObject.get("SubVehicleType").getAsString());
                orders.setFromLocation(jsonObject.get("FromLocation").getAsString());
                orders.setToLocation(jsonObject.get("ToLocation").getAsString());
                orders.setDate(jsonObject.get("Date").getAsString());
                orders.setWeight(jsonObject.get("Weight").getAsString());
                declaredOrders.add(orders);
            }
        }
        if (value.has("freightforward")) {
            JsonArray freightForwardService = value.getAsJsonArray("freightforward");
            for (int i = 0; i < freightForwardService.size(); i++) {
                JsonObject jsonObject = freightForwardService.get(i).getAsJsonObject();
                DeclaredOrders orders = new DeclaredOrders();
                orders.setServiceType(Constants.SERVICE_CARGO);
                orders.setId(jsonObject.get("ID").getAsString());
                orders.setCommodityType(jsonObject.get("CommodityType").getAsInt());
                orders.setFromLocation(jsonObject.get("FromLocation").getAsString());
                orders.setToLocation(jsonObject.get("ToLocation").getAsString());
                orders.setDate(jsonObject.get("Date").getAsString());
                orders.setWeight(jsonObject.get("Weight").getAsString());
                orders.setCbm(jsonObject.get("CBM").getAsString());
                orders.setServiceMode(jsonObject.get("service_mode").getAsInt());

                try {
                    orders.setAdditionalInfo(jsonObject.get("additional_info").getAsInt());
                } catch (NumberFormatException e) {
                    orders.setAdditionalInfo(-1);
                }
                try {
                    orders.setPerishableData(jsonObject.get("perishable_det").getAsInt());
                } catch (NumberFormatException e) {
                    orders.setPerishableData(0);
                }
                orders.setAddInsurance(jsonObject.get("AddInsurance").getAsInt());
                declaredOrders.add(orders);
            }
        }
        if (value.has("warehouseorder")) {
            JsonArray warehousingService = value.getAsJsonArray("warehouseorder");

            for (int i = 0; i < warehousingService.size(); i++) {
                JsonObject jsonObject = warehousingService.get(i).getAsJsonObject();
                DeclaredOrders orders = new DeclaredOrders();
                orders.setServiceType(Constants.SERVICE_STORAGE);
                orders.setId(jsonObject.get("ID").getAsString());
                orders.setCommodityType(jsonObject.get("CommodityType").getAsInt());
                orders.setLocation(jsonObject.get("Location").getAsString());
                orders.setFromDate(jsonObject.get("FromDate").getAsString());
                orders.setToDate(jsonObject.get("ToDate").getAsString());
                orders.setDate(jsonObject.get("Date").getAsString());
                orders.setWeight(jsonObject.get("Weight").getAsString());
                orders.setCbm(jsonObject.get("CBM").getAsString());
                try {
                    orders.setAdditionalInfo(jsonObject.get("additional_info").getAsInt());
                } catch (NumberFormatException e) {
                    orders.setAdditionalInfo(-1);
                }
                try {
                    orders.setPerishableData(jsonObject.get("perishable_det").getAsInt());
                } catch (NumberFormatException e) {
                    orders.setPerishableData(0);
                }

                declaredOrders.add(orders);
            }
        }
        return declaredOrders;
    }

}
