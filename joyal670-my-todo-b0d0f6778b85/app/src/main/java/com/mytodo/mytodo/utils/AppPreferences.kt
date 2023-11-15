package com.mytodo.mytodo.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object AppPreferences {
    private const val NAME = "mytodo"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)

    }
    fun clearSharedPreference(){
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

    var prefIsWelcomeSlider: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_WELCOME_SLIDER, false)
        set(value) = preferences.edit{
            it.putBoolean(ConstantPreference.IS_WELCOME_SLIDER,value)
        }


    var prefIsLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGGINED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGGINED, value)
        }

    var prefIsDashboardTutorial: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_DASHBOARD_TUTORIAL, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_DASHBOARD_TUTORIAL, value)
        }

    var prefIsTaskTutorial: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_TAK_TUTORIAL,false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_TAK_TUTORIAL, value)
        }

    var prefUserID : String
        get() = preferences.getString(ConstantPreference.USER_ID,"").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_ID,value)
        }

    var prefUserDisplayName : String
        get() = preferences.getString(ConstantPreference.USER_DISPLAY_NAME,"").toString()
        set(value) = preferences.edit{
            it.putString(ConstantPreference.USER_DISPLAY_NAME,value)
        }

    var prefUserProfilePic : String
        get() = preferences.getString(ConstantPreference.USER_PROFILE_PIC,"").toString()
        set(value) = preferences.edit{
            it.putString(ConstantPreference.USER_PROFILE_PIC,value)
        }

    var prefUserEmail : String
        get() = preferences.getString(ConstantPreference.USER_EMAIL,"").toString()
        set(value) = preferences.edit{
            it.putString(ConstantPreference.USER_EMAIL,value)
        }


}