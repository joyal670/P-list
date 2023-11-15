package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.list;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.pojo.Vehicles;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by VIDHU on 12/20/2017.
 */

public interface IListFragmentControllerCallback extends ICommonCallback{

    void onDeleteTruck(String message);

    void onListTruck(ArrayList<Vehicles> vehiclesList);

    void showErrorMessage(String message);
}
