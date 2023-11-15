package com.proteinium.proteiniumdietapp.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object AppPreferences {
    private const val NAME = "proteinium"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
        Log.e("AppPreferences","init")

    }
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var chooseLanguage: String?
        get() = preferences.getString(ConstantPreference.LANGUAGE, "en")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.LANGUAGE, value!!)
        }
    var isLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGIN,false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGIN,value)
        }
    var isCalendar: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_CALENDER,false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_CALENDER,value)
        }
    var isPlanActive: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_PLAN_ACTIVE,false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_PLAN_ACTIVE,value)
        }
    var isPlan: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_PLAN,false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_PLAN,value)
        }
    var isNotificationRead: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_NOTIFICATION_READ,false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_NOTIFICATION_READ,value)
        }
    var isNotification: Int
        get() = preferences.getInt(ConstantPreference.IS_NOTIFICATION,0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.IS_NOTIFICATION,value)
        }
    var isPushNotification: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_NOTIFICATION,true)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_NOTIFICATION,value)
        }
    var isLanguageSelected: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LANGUAGE_SELECTED,false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LANGUAGE_SELECTED,value)
        }
    var token: String?
        get() = preferences.getString(ConstantPreference.USER_TOKEN, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_TOKEN, value!!)
        }
    var user_id: Int?
        get() = preferences.getInt(ConstantPreference.USER_ID, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.USER_ID, value!!)
        }
    var user_phone: String?
        get() = preferences.getString(ConstantPreference.USER_PHONE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_PHONE, value!!)
        }
    var user_email: String?
        get() = preferences.getString(ConstantPreference.USER_EMAIL, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_EMAIL, value!!)
        }


    fun logoutClearPreference() {
        AppPreferences.isLogin = false
        AppPreferences.isLanguageSelected=false
        AppPreferences.token = ""
        preferences.edit().clear()
        preferences.edit().commit()
    }
}