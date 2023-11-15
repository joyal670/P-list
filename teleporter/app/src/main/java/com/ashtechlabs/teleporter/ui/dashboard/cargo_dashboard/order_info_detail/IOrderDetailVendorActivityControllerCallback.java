package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.order_info_detail;

import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by Jesna on 6/21/2017.
 */

public interface IOrderDetailVendorActivityControllerCallback extends ICommonCallback {
    void getDeliveryAndPickupAddress(String Address);
    void getDeliveryAndPickupAddressFailed(String Failed);
}
