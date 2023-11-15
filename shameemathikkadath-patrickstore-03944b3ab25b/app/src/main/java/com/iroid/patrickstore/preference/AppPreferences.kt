
package com.iroid.patrickstore.preference

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

    var jwt_token: String?
    get() = preferences.getString(ConstantPreference.JWT_TOKEN, "")
    set(value) = preferences.edit {
        it.putString(ConstantPreference.JWT_TOKEN, value)
    }

    var first_name: String?
        get() = preferences.getString(ConstantPreference.FIRST_NAME, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.FIRST_NAME, value)
        }

    var last_name: String?
        get() = preferences.getString(ConstantPreference.LAST_NAME, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.LAST_NAME, value)
        }

    var email: String?
        get() = preferences.getString(ConstantPreference.EMAIL, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.EMAIL, value)
        }


    var address: String?
        get() = preferences.getString(ConstantPreference.ADDRESS, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.ADDRESS, value)
        }


    var address_mobile: String?
        get() = preferences.getString(ConstantPreference.ADDRESS_MOBILE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.ADDRESS_MOBILE, value)
        }

    var address_place: String?
        get() = preferences.getString(ConstantPreference.ADDRESS_PLACE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.ADDRESS_PLACE, value)
        }

    var mobile: String?
        get() = preferences.getString(ConstantPreference.MOBILE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.MOBILE, value)
        }
    var image_url: String?
        get() = preferences.getString(ConstantPreference.IMAGE_URL, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.IMAGE_URL, value)
        }

    var order_id: String?
        get() = preferences.getString(ConstantPreference.ORDER_ID, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.ORDER_ID, value)
        }
    var isLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGIN, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGIN, value)
        }
    var addressId: String?
        get() = preferences.getString(ConstantPreference.ADDRESS_ID, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.ADDRESS_ID, value)
        }

    var sellerId: String?
        get() = preferences.getString(ConstantPreference.SELLER_ID, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.SELLER_ID, value)
        }

    var isChallenged: Boolean
        get() = preferences.getBoolean(ConstantPreference.CHALLENGED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.CHALLENGED, value)
        }
    var isFromOrder: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_FROM_ORDER, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_FROM_ORDER, value)
        }

    var lat: String
        get() = preferences.getString(ConstantPreference.LAT,"")!!
        set(value) = preferences.edit {
            it.putString(ConstantPreference.LAT, value)
        }

    var lan: String
        get() = preferences.getString(ConstantPreference.LAN,"")!!
        set(value) = preferences.edit {
            it.putString(ConstantPreference.LAN, value)
        }

    var franchiseId: String
        get() = preferences.getString(ConstantPreference.FRANCHISE_ID,"")!!
        set(value) = preferences.edit {
            it.putString(ConstantPreference.FRANCHISE_ID, value)
        }






    private fun setArray(tag: String, mArray: ArrayList<String?>) {
        val array = JSONArray(mArray)
        val json = array.toString()
        val editor = preferences.edit()
        editor.putString(tag, json)
        editor.apply()
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
