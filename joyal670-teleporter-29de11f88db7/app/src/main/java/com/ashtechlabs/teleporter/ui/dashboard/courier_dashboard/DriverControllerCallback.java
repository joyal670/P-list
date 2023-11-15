package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;

/**
 * Created by VIDHU on 10/27/2016.
 */

public interface DriverControllerCallback extends ICommonProgressCallback{

    void onGetDriverDetails(DriverOrder deliveryServices);

    void onGetDriverProfileDetails(DriverProfile profile);

    void onGetDriverDetailsFailed(String message);
}
