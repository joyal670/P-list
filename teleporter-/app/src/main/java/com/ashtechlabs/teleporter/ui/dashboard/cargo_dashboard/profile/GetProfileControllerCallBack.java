package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.profile;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfile;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public interface GetProfileControllerCallBack extends ICommonProgressCallback {
    void onGetVendorProfileDetails(VendorProfile profile);

    void onGetVendorProfileDetailsFailed(String message);
}
