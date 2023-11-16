package com.ashtechlabs.teleporter.ui.login;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface LoginControllerCallback extends ICommonProgressCallback{

    void onGetLoginDetails(JsonObject customer);

    void onLoginFailed(String message);

}
