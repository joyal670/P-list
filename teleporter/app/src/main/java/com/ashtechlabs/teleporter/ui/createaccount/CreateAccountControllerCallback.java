package com.ashtechlabs.teleporter.ui.createaccount;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface CreateAccountControllerCallback extends ICommonCallback{
    void onGetCreateDetails(JsonObject customer);
    void onGetCreateDetailsFailed(String msg);
}
