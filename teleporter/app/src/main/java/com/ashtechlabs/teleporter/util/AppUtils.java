package com.ashtechlabs.teleporter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppUtils {

    private final static String SHARED_PREF= "app_prefs";
    private final static String UserMode = "user_mode";
    private final static String userName = "user_name";
    private final static String userPassword = "user_password";

    private static SharedPreferences User;

    private static Editor edit;

    public AppUtils(Context context) {
        User = context
                .getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
    }


    public String getUserMode() {
        return User.getString(UserMode, "");
    }

    public void setUserMode(String auth) {
        edit = User.edit();
        edit.putString(UserMode, auth);
        edit.apply();
    }

    public String getUserName() {
        return User.getString(userName, "");
    }

    public void setUserName(String auth) {
        edit = User.edit();
        edit.putString(userName, auth);
        edit.apply();
    }

    public String getUserPassword() {
        return User.getString(userPassword, "");
    }

    public void setUserPassword(String auth) {
        edit = User.edit();
        edit.putString(userPassword, auth);
        edit.apply();
    }


}
