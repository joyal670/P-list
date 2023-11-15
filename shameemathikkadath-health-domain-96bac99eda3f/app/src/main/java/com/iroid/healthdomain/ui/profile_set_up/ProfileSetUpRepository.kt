package com.iroid.healthdomain.ui.profile_set_up

import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.base.BaseRepository
import okhttp3.MultipartBody

class ProfileSetUpRepository(
        private val api: ApiServices,
        private val preferences: UserPreferences? = null) : BaseRepository() {

    suspend fun updateUserDetails(accountModel: AccountModel) = safeApiCall {

        var name: String = accountModel.name.toString()
        var age: String = accountModel.age.toString()
        var gender: String = accountModel.gender.toString()
        var blood_group: String = accountModel.blood_group.toString()
        var weight: String = accountModel.weight.toString()
        var height: String = accountModel.height.toString()

        api.updateUserProfile(accountModel)

//        api.updateUser(
//                name = name,
//                age = age,
//                gender = gender,
//                bloodGroup = blood_group,
//                weight = weight,
//                height = height)
    }

    suspend fun uploadProfileImage(image: MultipartBody.Part) = safeApiCall {
        api.uploadImage(image)
    }

    suspend fun saveNumber(name: String) {
        println("User repo ${name}")
        preferences?.saveUser(name)
    }

}