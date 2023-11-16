package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.profile;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface UpdateProfileControllerCallback extends ICommonProgressCallback{
    void onGetProfileDetails(JsonObject customer);

    void onGetProfileDetailsFailed(String message);

}
