package com.iroid.healthdomain.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iroid.healthdomain.ui.cms.CmsViewModel
import com.iroid.healthdomain.ui.cms.CmsRepository
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.home.mainActivity.HomeViewModel
import com.iroid.healthdomain.ui.home.mainActivity.all_contacts.AllContactsViewModel
import com.iroid.healthdomain.ui.home.mainActivity.person.PersonViewModel
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository
import com.iroid.healthdomain.ui.home.my_health.MyHealthViewModel
import com.iroid.healthdomain.ui.home.my_health.challenges.ChallengesRepository
import com.iroid.healthdomain.ui.home.my_health.challenges.ChallengesViewModel
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.activity.ActivityViewModel
import com.iroid.healthdomain.ui.home.notification.NotificationViewModel
import com.iroid.healthdomain.ui.home.profile.ProfileRepository
import com.iroid.healthdomain.ui.home.profile.ProfileViewModel
import com.iroid.healthdomain.ui.login.LoginRepository
import com.iroid.healthdomain.ui.login.LoginViewModel
import com.iroid.healthdomain.ui.profile_set_up.ProfileSetUpRepository
import com.iroid.healthdomain.ui.profile_set_up.ProfileSetUpViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val repository: BaseRepository,
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                    repository as HomeRepository, application
            ) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                    repository as LoginRepository, application
            ) as T

            modelClass.isAssignableFrom(ProfileSetUpViewModel::class.java) -> ProfileSetUpViewModel(
                    repository as ProfileSetUpRepository, application
            ) as T

            modelClass.isAssignableFrom(MyHealthViewModel::class.java) -> MyHealthViewModel(
                    repository as MyHealthRepository, application
            ) as T


            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                    repository as ProfileRepository, application
            ) as T


            modelClass.isAssignableFrom(ChallengesViewModel::class.java) -> ChallengesViewModel(
                    repository as ChallengesRepository, application
            ) as T

            modelClass.isAssignableFrom(AllContactsViewModel::class.java) -> AllContactsViewModel(
                    repository as HomeRepository, application
            ) as T


            modelClass.isAssignableFrom(ActivityViewModel::class.java) -> ActivityViewModel(
                    repository as MyHealthRepository, application
            ) as T

            modelClass.isAssignableFrom(PersonViewModel::class.java) -> PersonViewModel(
                repository as HomeRepository, application
            ) as T

            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> NotificationViewModel(
                repository as HomeRepository, application
            ) as T
            modelClass.isAssignableFrom(CmsViewModel::class.java) ->CmsViewModel(
                repository as CmsRepository, application
            ) as T

            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }

}