package com.iroid.healthdomain.ui.preference
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.iroid.healthdomain.data.dummyModel.NewContactsModel
import org.json.JSONArray
import org.json.JSONException


object AppPreferences {
    private const val NAME = "HealthDomain"
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



    var isLoaded: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOADED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOADED, value)
        }
    var isLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.KEY_LOGIN, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.KEY_LOGIN, value)
        }

    var key_user_id: String?
        get() = preferences.getString(ConstantPreference.KEY_USER_ID, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.KEY_USER_ID, value)
        }




     fun setArray(tag: String, mArray: ArrayList<String>) {
        val array = JSONArray(mArray)
        val json = array.toString()
        val editor = preferences.edit()
        editor.putString(tag, json)
        editor.apply()
    }

     fun getArray(tag: String): ArrayList<String>? {
        val array = ArrayList<String>()
        val json = preferences.getString(tag,"")
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



    var user_contacts: String?
        get() = preferences.getString(ConstantPreference.CONTACTS_LIST, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.CONTACTS_LIST, value)
        }

    var contacts_loaded  :Boolean
        get() = preferences.getBoolean(ConstantPreference.CONTACTS_LIST_LOADED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.CONTACTS_LIST_LOADED, value)
        }

    var user_new_contacts: String?
        get() = preferences.getString(ConstantPreference.CONTACTS_LIST_NEW, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.CONTACTS_LIST_NEW, value)
        }

    var user_contactList: String?
        get() = preferences.getString(ConstantPreference.CONTACTS_LIST_USER, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.CONTACTS_LIST_USER, value)
        }
    var steps_count: String?
        get() = preferences.getString(ConstantPreference.STEPS_COUNT, "0")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.STEPS_COUNT, value)
        }


}