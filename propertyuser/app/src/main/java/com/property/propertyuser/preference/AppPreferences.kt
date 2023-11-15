package com.property.propertyuser.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.facebook.login.LoginManager

object AppPreferences {
    private const val NAME = "property-userapp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
        Log.e("AppPreferences", "init")

    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var chooseLanguage: String?
        get() = preferences.getString(ConstantPreference.LANGUAGE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.LANGUAGE, value!!)
        }
    var isLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGIN, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGIN, value)
        }
    var isFirstTime: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_FIRST_TIME, true)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_FIRST_TIME, value)
        }
    var token: String?
        get() = preferences.getString(ConstantPreference.USER_TOKEN, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_TOKEN, value!!)
        }
    var reload_property_list: Boolean
        get() = preferences.getBoolean(ConstantPreference.RELOAD_PROPERTY_LIST_API, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_PROPERTY_LIST_API, value)
        }
    var reload_booked_property_list: Boolean
        get() = preferences.getBoolean(ConstantPreference.RELOAD_BOOKED_PROPERTY_LIST_API, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_BOOKED_PROPERTY_LIST_API, value)
        }
    var reload_service_status_list_for_payment: Boolean
        get() = preferences.getBoolean(
            ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_BILL_PAYMENT,
            false
        )
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_BILL_PAYMENT, value)
        }
    var reload_service_status_list_for_cancel_service: Boolean
        get() = preferences.getBoolean(
            ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_CANCEL_SERVICE,
            false
        )
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_CANCEL_SERVICE, value)
        }
    var reload_profile_details: Boolean
        get() = preferences.getBoolean(ConstantPreference.RELOAD_PROFILE_DETAILS, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_PROFILE_DETAILS, value)
        }
    var user_name: String?
        get() = preferences.getString(ConstantPreference.USER_NAME, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_NAME, value!!)
        }
    var user_email: String?
        get() = preferences.getString(ConstantPreference.USER_EMAIL, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_EMAIL, value!!)
        }
    var user_phone: String?
        get() = preferences.getString(ConstantPreference.USER_PHONE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_PHONE, value!!)
        }
    var user_profile_image: String?
        get() = preferences.getString(ConstantPreference.USER_PROFILE_IMAGE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_PROFILE_IMAGE, value!!)
        }
    var reload_home: Boolean
        get() = preferences.getBoolean(ConstantPreference.RELOAD_HOME, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_HOME, value)
        }
    var home_lat: String?
        get() = preferences.getString(ConstantPreference.HOME_LAT, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.HOME_LAT, value!!)
        }
    var home_long: String?
        get() = preferences.getString(ConstantPreference.HOME_LONG, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.HOME_LONG, value!!)
        }

    fun logoutClearPreference() {
        AppPreferences.isLogin = false
        AppPreferences.token = ""
        AppPreferences.user_profile_image = ""
        LoginManager.getInstance().logOut()
        preferences.edit().clear()
        preferences.edit().commit()


    }

    var isReferTutorial: Boolean
        get() = preferences.getBoolean(ConstantPreference.is_tutorial, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.is_tutorial, value)
        }

    var isLocationLoaded: Boolean
        get() = preferences.getBoolean(ConstantPreference.HOME_LOCATION, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.HOME_LOCATION, value)
        }

}