package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.list;

/**
 * Created by VIDHU on 12/20/2017.
 */

public interface IListFragmentController {

    void getWarehouseList(String token);

    void deleteWarehouse(String token, String id);
}
