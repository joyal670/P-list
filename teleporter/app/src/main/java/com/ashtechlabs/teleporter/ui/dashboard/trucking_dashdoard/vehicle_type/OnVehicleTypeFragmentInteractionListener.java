package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type;

public interface OnVehicleTypeFragmentInteractionListener {
    // TODO: Update argument type and name
    void setToolbarTitle(String toolbarTitle);

    void replaceFragment(String fragment_tag);

    void showProgressIndicator(boolean show);

    void showFab(boolean show);

    void onSelectVehicle(int vehicleType,String truckName, int vehicleSubType, String subTruckName);
}