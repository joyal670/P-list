package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfile;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public interface TruckingGetProfileControllerCallBack extends ICommonProgressCallback{

    void onGetDriverProfileDetails(TruckingProfile profile);

    void onGetDriverProfileDetailsFailed(String message);
}
