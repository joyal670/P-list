package com.iroid.jeetmeet.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object AppPreferences {
    private const val NAME = "jeetmeet"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)


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


    var prefIsLogin: Boolean
        get() = preferences.getBoolean(ConstantPreference.IS_LOGGINED, false)
        set(value) = preferences.edit {
            it.putBoolean(ConstantPreference.IS_LOGGINED, value)
        }

    var prefUserToken:String
        get() = preferences.getString(ConstantPreference.USER_TOKEN,"").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.USER_TOKEN,value)
        }

    var prefLoginType : String
        get() = preferences.getString(ConstantPreference.LOGIN_TYPE,"").toString()
        set(value) = preferences.edit{
            it.putString(ConstantPreference.LOGIN_TYPE,value)
        }

    var prefIsStudentTutorial : Boolean
        get() = preferences.getBoolean(ConstantPreference.TURORIAL_STUDENT, false)
        set(value) = preferences.edit{
            it.putBoolean(ConstantPreference.TURORIAL_STUDENT,value)
        }

    var prefStudentImg : String
        get() = preferences.getString(ConstantPreference.STUDENT_IMG,"").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.STUDENT_IMG,value)
        }

    var prefStudentName : String
        get() = preferences.getString(ConstantPreference.STUDENT_NAME,"").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.STUDENT_NAME,value)
        }

    var prefStudentFirstName : String
        get() = preferences.getString(ConstantPreference.STUDENT_FIRST_NAME,"").toString()
        set(value) = preferences.edit {
            it.putString(ConstantPreference.STUDENT_FIRST_NAME,value)
        }
}