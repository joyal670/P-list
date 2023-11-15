
package com.iroid.emergency.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.json.JSONArray
import org.json.JSONException


object AppPreferences {
    private const val NAME = "PatricKStore"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)

    }
    fun cleareSharedPreference(){
        preferences.edit().clear().commit()
    }
    fun getDefaultSharedPreference(context: Context): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }


    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private fun setArray(tag: String, mArray: ArrayList<String?>) {
        val array = JSONArray(mArray)
        val json = array.toString()
        val editor = preferences.edit()
        editor.putString(tag, json)
        editor.apply()
    }

    var userName: String?
        get() = preferences.getString(ConstantPreference.USER_NAME, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_NAME, value)
        }

    var userMobile: String?
        get() = preferences.getString(ConstantPreference.USER_MOBILE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_MOBILE, value)
        }

    var userLat: String?
        get() = preferences.getString(ConstantPreference.USER_LAT, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_LAT, value)
        }
    var userLan: String?
        get() = preferences.getString(ConstantPreference.USER_LAN, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_LAN, value)
        }

    var userType: String?
        get() = preferences.getString(ConstantPreference.USER_TYPE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_TYPE, value)
        }

    var userToken: String?
        get() = preferences.getString(ConstantPreference.USER_TOKEN, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_TOKEN, value)
        }
    var isLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGIN, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGIN, value)
        }

    var isLocationEnabled: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOCATION_ENABLED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOCATION_ENABLED, value)
        }





    private fun getArray(tag: String): ArrayList<String>? {
        val array = ArrayList<String>()
        val json = preferences.getString(tag, "")
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                array.add(jsonArray.getString(i))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return array
    }


}
