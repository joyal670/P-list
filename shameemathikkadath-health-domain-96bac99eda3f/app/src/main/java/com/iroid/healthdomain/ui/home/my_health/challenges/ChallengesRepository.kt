package com.iroid.healthdomain.ui.home.my_health.challenges

import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.base.BaseRepository

class ChallengesRepository(
        private val api: ApiServices,
        private val preferences: UserPreferences? = null) : BaseRepository()