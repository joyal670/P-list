package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.list;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo.StorageSpace;

import java.util.ArrayList;

/**
 * Created by VIDHU on 12/20/2017.
 */

public interface IListFragmentControllerCallback extends ICommonCallback{

    void onDeleteWarehouse(String message);

    void onListWarehouse(ArrayList<StorageSpace> storageSpaceList);

    void showErrorMessage(String message);
}
