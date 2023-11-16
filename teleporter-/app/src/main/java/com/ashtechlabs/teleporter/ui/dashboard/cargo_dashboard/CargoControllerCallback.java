package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.Vendor;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfile;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 24-Jan-17.
 */

public interface CargoControllerCallback extends ICommonProgressCallback{

    void onGetVendorsDetails(ArrayList<VendorList> services);

    void onGetVendorProfileDetails(VendorProfile profile);

    void onGetVendorsDetailsFailed(String message);
}
