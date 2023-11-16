package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.list;

/**
 * Created by VIDHU on 12/20/2017.
 */

public interface IListFragmentController {

    void getTruckList(String token);

    void deleteTruck(String token, String id);
}
