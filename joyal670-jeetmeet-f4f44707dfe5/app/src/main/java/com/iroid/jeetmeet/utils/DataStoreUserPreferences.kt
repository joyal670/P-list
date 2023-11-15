package com.iroid.jeetmeet.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class DataStoreUserPreferences(context: Context)
{
    /* String value */
    suspend fun saveValue(context: Context, key: String, value: String) {

        val wrappedKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[wrappedKey] = value
        }

    }

    /* int value */
    suspend fun saveValue(context: Context, key: String, value: Int) {

        val wrappedKey = intPreferencesKey(key)
        context.dataStore.edit {
            it[wrappedKey] = value
        }

    }

    /* boolean value */
    suspend fun saveValue(context: Context, key: String, value: Boolean) {

        val wrappedKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[wrappedKey] = value
        }

    }

    /* string value */
    suspend fun getStringValue(context: Context, key: String, default: String = ""): String
    {
        val wrappedKey = stringPreferencesKey(key)
        val valueFlow: Flow<String> = context.dataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

    /* int value */
    suspend fun getIntValue(context: Context, key: String, default: Int = 0): Int {
        val wrappedKey = intPreferencesKey(key)
        val valueFlow: Flow<Int> = context.dataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

    /* boolean value */
    suspend fun getBooleanValue(context: Context, key: String, default: Boolean = false): Boolean {
        val wrappedKey = booleanPreferencesKey(key)
        val valueFlow: Flow<Boolean> = context.dataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }
}