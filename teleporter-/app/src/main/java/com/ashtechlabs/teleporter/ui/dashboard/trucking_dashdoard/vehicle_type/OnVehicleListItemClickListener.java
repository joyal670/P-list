package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type;

import android.view.View;

public interface OnVehicleListItemClickListener {
    void onClick(View view, int position, String name, String vehicleType, String vehicleSubType);
}