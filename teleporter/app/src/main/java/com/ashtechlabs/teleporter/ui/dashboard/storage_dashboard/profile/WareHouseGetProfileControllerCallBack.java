package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.profile;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfile;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public interface WareHouseGetProfileControllerCallBack extends ICommonProgressCallback{
    void onGetStorageProfileDetails(WareHouseProfile profile);


    void onGetStorageProfileDetailsFailed(String message);
}
