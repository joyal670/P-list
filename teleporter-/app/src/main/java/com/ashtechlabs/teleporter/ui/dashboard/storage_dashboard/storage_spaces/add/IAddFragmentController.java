package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.add;

/**
 * Created by VIDHU on 12/20/2017.
 */

public interface IAddFragmentController {

    void addWareHouse(String token, String warehouseCapacity, String spaceAvailable, String warehouseLocation, String warehouseName,
                      String insuranceExpDate, String tradeLicenceExpDate, String picOfWarehouse, String tradeLicenseNumber, String insuranceNumber);

}
