package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces;

import android.view.View;

import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo.StorageSpace;

import java.util.ArrayList;

public interface OnStorageListItemClickListener {
    void onClick(View view, int position, StorageSpace storageSpace);
}