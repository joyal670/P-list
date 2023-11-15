package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.google.gson.JsonObject;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface UpdateProfileControllerCallback extends ICommonProgressCallback{
    void onGetProfileDetails(JsonObject customer);

    void onGetProfileDetailsFailed(String message);

}
