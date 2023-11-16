package com.ncomfortsagent.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object AppPreferences {
    private const val NAME = "ncomforts"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun clearSharedPreference() {
        preferences.edit().clear().apply()
    }

    fun getDefaultSharedPreference(context: Context): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }


    var prefIsLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGGINED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGGINED, value)
        }

    var prefUserToken: String
        get() = preferences.getString(ConstantPreference.USER_TOKEN, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_TOKEN, value)
        }

    var prefProfileImage: String
        get() = preferences.getString(ConstantPreference.PROFILE_IMAGE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.PROFILE_IMAGE, value)
        }

    var prefUserName: String
        get() = preferences.getString(ConstantPreference.USER_IMAGE, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_IMAGE, value)
        }

    var prefUserEmail: String
        get() = preferences.getString(ConstantPreference.USER_EMAIL, "").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_EMAIL, value)
        }

    var prefIsTutorial: Boolean
        get() = preferences.getBoolean(ConstantPreference.TUTORIAL_SCREEN, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.TUTORIAL_SCREEN, value)
        }

    var prefEnquiryLoading: Boolean
        get() = preferences.getBoolean(ConstantPreference.ENQUIRYYlLOADING, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.ENQUIRYYlLOADING, value)
        }

    var user_lang: String
        get() = preferences.getString(ConstantPreference.USER_LANG, Constants.ENGLISH_LAG)
            .toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_LANG, value)
        }

    var agent_id: Int
        get() = preferences.getInt(ConstantPreference.USER_ID, 0)
        set(value) = preferences.edit {
            it.putInt(ConstantPreference.USER_ID, value)
        }
}