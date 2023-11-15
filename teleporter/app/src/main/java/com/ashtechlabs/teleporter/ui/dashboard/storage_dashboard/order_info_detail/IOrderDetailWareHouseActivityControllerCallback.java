package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.order_info_detail;

import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by Jesna on 6/21/2017.
 */

public interface IOrderDetailWareHouseActivityControllerCallback extends ICommonCallback {
    void getDeliveryAndPickupAddressWareHouse(String Address);
    void getDeliveryAndPickupAddressFailedWareHouse(String Failed);
}
