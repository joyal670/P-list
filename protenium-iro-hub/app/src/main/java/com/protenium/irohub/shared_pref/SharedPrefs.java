package com.protenium.irohub.shared_pref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static SharedPreferences preferences;
    private static final String ISAF_PREFERENCE_FILE_KEY = "com_irohub_preference_key";

    public static void initializeSharedPrefs(Context context) {
        preferences = context.getSharedPreferences(ISAF_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        setPreferences(preferences);
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static void setPreferences(SharedPreferences preferences) {
        SharedPrefs.preferences = preferences;
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String def) {
        return preferences.getString(key, def);
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }


    public static void clearAllPrefs() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


    public static class Keys {
        //General Keys
        public static final String IS_LOGIN = "is_login";
        public static final String CUSTOMER_TOKEN = "customer_token";
        public static final String CUSTOMER_ID = "customer_id";
        public static final String CUSTOMER_EMAIL = "customer_email";


    }
}
