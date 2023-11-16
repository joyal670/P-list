package com.iroid.healthdomain.ui.home.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.model_class.profile_image.ProfileImageResponse
import com.iroid.healthdomain.data.model_class.user_profile.UserModelResponse
import com.iroid.healthdomain.data.model_class.user_update.UpdateUserResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(val repository: ProfileRepository,application: Application) : BaseViewModel(application = application) {

    val userApiResponse: LiveData<Resource<UserModelResponse>> = MutableLiveData()
    val profileImgResponse: LiveData<Resource<ProfileImageResponse>> = MutableLiveData()
    val getUpdateUserResponse: LiveData<Resource<UpdateUserResponse>> = MutableLiveData()

    fun getUserProfile() = viewModelScope.launch {
        userApiResponse as MutableLiveData
        userApiResponse.value = Resource.Loading
        userApiResponse.value = repository.requestUserProfile()
    }

    fun uploadImage(image: MultipartBody.Part) = viewModelScope.launch{
        profileImgResponse as MutableLiveData
        profileImgResponse.value = Resource.Loading
        profileImgResponse.value = repository.uploadProfileImage(image)
    }

    fun updateUser(accountModel: AccountModel) = viewModelScope.launch {
        getUpdateUserResponse as MutableLiveData
        getUpdateUserResponse.value = Resource.Loading
        getUpdateUserResponse.value = repository.updateUserDetails(accountModel)
    }
}