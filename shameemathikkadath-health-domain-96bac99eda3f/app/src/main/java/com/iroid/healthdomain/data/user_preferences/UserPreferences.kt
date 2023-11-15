package com.iroid.healthdomain.data.user_preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(context: Context) {

    private val TAG = "UserPreferences"
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
                name = "my_data_store"
        )
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /*
    Save auth token and retrieve
     */
    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }

    //******************************************************************

    /*
    Save phone number and retrieve
   */

    suspend fun savePhoneNumber(number: String) {
        dataStore.edit { preferences ->
            preferences[KEY_PHONE_NUMBER] = number
        }
    }


    val number: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_PHONE_NUMBER]
        }

    //******************************************************************


    /*
    Save user name and retrieve
     */
    suspend fun saveUser(name: String) {
        dataStore.edit { preferences ->
            preferences[KEY_PHONE_NUMBER] = name
        }
    }
    val hdStatus: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_HSD_SCORE]?.toString()
        }


    suspend fun saveHdStatus(hdStatus: String) {
        dataStore.edit { preferences ->
            preferences[KEY_HSD_SCORE] = hdStatus
        }
    }


    val name: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_NAME]
        }

    //******************************************************************


    /*
       Save user status and retrieve
        */
    suspend fun saveUserStatus(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_STATUS] = value
        }
    }


    val userStatus: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_STATUS]
        }

    val isNotification: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_IS_NOTIFICATION]
        }
    suspend fun saveNotification(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_NOTIFICATION] = value
        }
    }

    val isFollower: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_FOLLOWER]
        }
    suspend fun saveFollower(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_FOLLOWER] = value
        }
    }

    val isLogin: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_LOGIN]
        }
    suspend fun saveLogin(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_LOGIN] = value
        }
    }


    //******************************************************************

    /*
Save User Id and retrieve
*/

    suspend fun saveUserId(number: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = number
        }
    }


    val userId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_ID]
        }


    suspend fun saveUserProfileImage(url: String) {
        dataStore.edit { preferences ->
            preferences[KEY_PROFILE_IMAGE] = url
        }
    }

    val userImage: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_PROFILE_IMAGE]
        }

    suspend fun saveUserProfileScore(url: String) {
        dataStore.edit { preferences ->
            preferences[KEY_SCORE] = url
        }
    }

    val userScore: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_SCORE]
        }


    //******************************************************************

    companion object {
        private val KEY_AUTH = stringPreferencesKey("auth_key")
        private val KEY_PHONE_NUMBER = stringPreferencesKey("phone_number")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_STATUS = booleanPreferencesKey("user_status")
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val KEY_IS_NOTIFICATION = booleanPreferencesKey("key_is_notification")
        private val KEY_HSD_SCORE= stringPreferencesKey("key_hsd_score")
        private val KEY_FOLLOWER= booleanPreferencesKey("key_follower")
        private val KEY_LOGIN= booleanPreferencesKey("key_login")
        private val KEY_PROFILE_IMAGE= stringPreferencesKey("key_profile_image")
        private val KEY_SCORE= stringPreferencesKey("KEY_SCORE")




    }

}
