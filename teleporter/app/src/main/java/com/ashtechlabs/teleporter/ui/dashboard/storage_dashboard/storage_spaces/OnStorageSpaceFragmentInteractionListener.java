package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces;

import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo.StorageSpace;

public interface OnStorageSpaceFragmentInteractionListener {
    // TODO: Update argument type and name
    void setToolbarTitle(String toolbarTitle);

    void replaceFragment(String fragment_tag);

    void showProgressIndicator(boolean show);

    void showFab(boolean show);

    void showDetailFragment(StorageSpace storageSpace);
}