package com.property.propertyagent.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object AppPreferences {
    private const val NAME = "property"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)

    }

    fun cleareSharedPreference() {
        preferences.edit().clear().commit()
        isVisited = true
    }

    fun getDefaultSharedPreference(context: Context): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }


    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGGINED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGGINED, value)
        }
    var isVisited: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_VISITED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_VISITED, value)
        }

    var login_type: String
        get() = preferences.getString(ConstantPreference.LOGIN_TYPE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.LOGIN_TYPE, value)
        }
    var token: String
        get() = preferences.getString(ConstantPreference.USER_TOKEN, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_TOKEN, value)
        }

    var user_id: Int
        get() = preferences.getInt(ConstantPreference.USER_ID, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.USER_ID, value)
        }

    var user_lang: String
        get() = preferences.getString(ConstantPreference.USER_LANG, Constants.ENGLISH_LAG).toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_LANG, value)
        }

    var user_name: String
        get() = preferences.getString(ConstantPreference.USER_NAME, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_NAME, value)
        }

    var user_email: String
        get() = preferences.getString(ConstantPreference.USER_EMAIL, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_EMAIL, value)
        }

    var user_phone: String
        get() = preferences.getString(ConstantPreference.USER_PHONE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_PHONE, value)
        }

    var user_country: Int
        get() = preferences.getInt(ConstantPreference.USER_COUNTRY, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.USER_COUNTRY, value)
        }

    var user_state: Int
        get() = preferences.getInt(ConstantPreference.USER_STATE, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.USER_STATE, value)
        }

    var user_city: Int
        get() = preferences.getInt(ConstantPreference.USER_CITY, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.USER_CITY, 0)
        }

    var user_zip_code: Int
        get() = preferences.getInt(ConstantPreference.USER_ZIPCODE, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.USER_ZIPCODE, value)
        }

    var user_latitude: String
        get() = preferences.getString(ConstantPreference.USER_LATITUDE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_LATITUDE, value)
        }

    var user_longitude: String
        get() = preferences.getString(ConstantPreference.USER_LONGITUDE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_LONGITUDE, value)
        }

    var user_address: String
        get() = preferences.getString(ConstantPreference.USER_ADDRESS, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_ADDRESS, value)
        }

    var user_account_number: String
        get() = preferences.getString(ConstantPreference.USER_ACCOUNT_NUMBER, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_ACCOUNT_NUMBER, value)
        }

    var user_branch_name: String
        get() = preferences.getString(ConstantPreference.USER_BRANCH_NAME, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_BRANCH_NAME, value)
        }

    var user_ifsc: String
        get() = preferences.getString(ConstantPreference.USER_IFSC_CODE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_IFSC_CODE, value)
        }

    var user_profileImg: String
        get() = preferences.getString(ConstantPreference.USER_PROFILE_PIC, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_PROFILE_PIC, value)
        }

    var user_id_image: String
        get() = preferences.getString(ConstantPreference.USER_ID_IMAGE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_ID_IMAGE, value)
        }
    var reload_service_status_list_for_cancel_service: Boolean
        get() = preferences.getBoolean(
            ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_CANCEL_SERVICE,
            false
        )
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_CANCEL_SERVICE, value)
        }
    var reload_service_status_list_for_payment: Boolean
        get() = preferences.getBoolean(
            ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_BILL_PAYMENT,
            false
        )
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_SERVICE_STATUS_AFTER_BILL_PAYMENT, value)
        }
    var reload_my_task_pending_list: Boolean
        get() = preferences.getBoolean(ConstantPreference.RELOAD_MY_TASK_PENDING_LIST, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_MY_TASK_PENDING_LIST, value)
        }
    var reload_home_api_for_request_accept: Boolean
        get() = preferences.getBoolean(ConstantPreference.RELOAD_HOME_API_FOR_REQUEST_ACCEPT, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.RELOAD_HOME_API_FOR_REQUEST_ACCEPT, value)
        }

    var clicked_property_id: String
        get() = preferences.getString(ConstantPreference.PROPERTY_ID, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.PROPERTY_ID, value)
        }

    var isProfileChanged: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_PROFILE_CHANGED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_PROFILE_CHANGED, value)
        }

    var tour_id: String
        get() = preferences.getString(ConstantPreference.TOUR_ID, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.TOUR_ID, value)
        }

    var property_type: Int
        get() = preferences.getInt(ConstantPreference.PROPERTY_TYPE, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.PROPERTY_TYPE, value)
        }

    var clicked_phone: String
        get() = preferences.getString(ConstantPreference.CLICKED_PHONE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.CLICKED_PHONE, value)
        }

    var selected_package_id: String
        get() = preferences.getString(ConstantPreference.PACKAGE_ID, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.PACKAGE_ID, value)
        }

    var isConnectionRestored: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_CONNECTION_RESTORED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_CONNECTION_RESTORED, value)
        }

    var clicked_filter_indicator: Boolean
        get() = preferences.getBoolean(ConstantPreference.CLICKED_FILTER, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.CLICKED_FILTER, value)
        }

    var is_completed: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_COMPLETED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_COMPLETED, value)
        }

    var is_user: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_USER, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_USER, value)
        }

    var is_sale: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_SALE, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_SALE, value)
        }

    var is_builder: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_BUILDER, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_BUILDER, value)
        }

    var is_user_property_booked: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_USER_PROPERTY_BOOKED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_USER_PROPERTY_BOOKED, value)
        }

    var clicked_building_id: String
        get() = preferences.getString(ConstantPreference.CLICKED_BUILDING_ID, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.CLICKED_BUILDING_ID, value)
        }
}