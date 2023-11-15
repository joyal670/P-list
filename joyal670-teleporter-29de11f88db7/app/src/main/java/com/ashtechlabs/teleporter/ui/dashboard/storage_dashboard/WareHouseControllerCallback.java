package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouse;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfile;

/**
 * Created by IROID_ANDROID1 on 23-Jan-17.
 */

public interface WareHouseControllerCallback extends ICommonProgressCallback{
    void onGetWareHouseDetails(WareHouse warehouseservices);

    void onGetWareHouseDetailsFailed(String message);

    void onGetStorageProfileDetails(WareHouseProfile profile);


    void onGetStorageProfileDetailsFailed(String message);
}
