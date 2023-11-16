package com.ashtechlabs.teleporter.app_prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class GlobalPreferManager {

    private static SharedPreferences preferences;
    private final static String APP_PREFS = "app_prefs";

    public GlobalPreferManager(Context context) {
        preferences = context
                .getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }

    public static void saveArray(String tag, ArrayList<String> mArray) {
        JSONArray array = new JSONArray(mArray);
        String json = array.toString();

        Editor editor = preferences.edit();

        editor.putString(tag, json);
        editor.commit();


    }

    public static ArrayList<String> loadArray(String tag) {
        ArrayList<String> array = new ArrayList<String>();


        String json = preferences.getString(tag, "");

        try {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                array.add(jsonArray.getString(i));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return array;
    }

    public static void initializePreferenceManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static void setString(String key, String value) {
//        Log.e(key, value);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setFloat(String key, float value) {
        Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }


    public static class Keys {

        public static final String IS_LOGIN = "is_login";

        public static final String MOBILE_NUM = "mobile_number";
        public static final String MOBILE_NUM_CARGO = "mobile_number";
        public static final String MOBILE_NUM_COURIER = "mobile_number";
        public static final String MOBILE_NUM_TRUCKING = "mobile_number";
        public static final String MOBILE_NUM_STORAGE = "mobile_number";
        public static final String EMAIL = "email";
        public static final String EMAIL_COURIER = "email_courier";
        public static final String EMAIL_CARGO = "email_cargo";
        public static final String EMAIL_TRUCKING = "email_trucking";
        public static final String EMAIL_STORAGE = "email_storage";
        public static final String PASSWORD = "password";
        public static final String TOKEN_COURIER = "token_driver";
        public static final String TOKEN_TRUCKING = "token_trucking";
        public static final String TOKEN_CARGO = "token_vendor";
        public static final String TOKEN_STORAGE = "token_warehouse";
        public static final String LOGIN_MODE = "login_mode";
        public final static String USER_MODE = "user_mode";
        public final static String USER_NAME = "user_name";
        public final static String USER_PASSWORD = "user_password";

        public static final String USERNAME_COURIER = "courier_username";
        public static final String USERNAME_TRUCKING = "trucking_username";
        public static final String USERNAME_CARGO = "cargo_username";
        public static final String USERNAME_STORAGE = "storage_username";

        public static final String PHONE_COURIER = "courier_phone";
        public static final String PHONE_TRUCKING = "trucking_phone";
        public static final String PHONE_CARGO = "cargo_phone";
        public static final String PHONE_STORAGE = "storage_phone";


        public static final String MOBILE_COURIER = "courier_mobile";
        public static final String MOBILE_TRUCKING = "trucking_mobile";
        public static final String MOBILE_CARGO = "cargo_mobile";
        public static final String MOBILE_STORAGE = "storage_mobile";

        public static final String LOGIN_TYPE_CARGO = "login_type_cargo";
        public static final String LOGIN_TYPE_COURIER = "login_type_courier";
        public static final String LOGIN_TYPE_TRUCKING = "login_type_trucking";
        public static final String LOGIN_TYPE_STORAGE = "login_type_storage";

        public static final String PROFILE_IMAGE_COURIER = "courier_profile_image";
        public static final String PROFILE_IMAGE_TRUCKING = "trucking_profile_image";
        public static final String PROFILE_IMAGE_CARGO = "cargo_profile_image";
        public static final String PROFILE_IMAGE_STORAGE = "storage_profile_image";


    }

}
