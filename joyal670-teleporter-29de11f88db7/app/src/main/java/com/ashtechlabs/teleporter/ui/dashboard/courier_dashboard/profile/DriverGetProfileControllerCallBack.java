package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.profile;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public interface DriverGetProfileControllerCallBack extends ICommonProgressCallback{

    void onGetDriverProfileDetails(DriverProfile profile);

    void onGetDriverProfileDetailsFailed(String message);
}
