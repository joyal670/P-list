package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.add;

import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by VIDHU on 12/20/2017.
 */

public interface IAddFragmentControllerCallback extends ICommonCallback{
    void vehicleAdded(String message);
    void showErrorMessage(String message);
}
