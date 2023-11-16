package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingOrder;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfile;

import java.util.ArrayList;

/**
 * Created by VIDHU on 10/27/2016.
 */

public interface TruckingControllerCallback extends ICommonProgressCallback{

    void onGetDriverDetails(ArrayList<TruckingMpService> deliveryServices);

    void onGetDriverProfileDetails(TruckingProfile profile);

    void onGetDriverDetailsFailed(String message);
}
