package com.iroid.healthdomain.ui.profile_set_up

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.model_class.profile_image.ProfileImageResponse
import com.iroid.healthdomain.data.model_class.user_update.UpdateUserResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileSetUpViewModel(val repository: ProfileSetUpRepository,
                            application: Application) : BaseViewModel(application) {


    val getUpdateUserResponse: LiveData<Resource<UpdateUserResponse>> = MutableLiveData()
    val profileImgResponse: LiveData<Resource<ProfileImageResponse>> = MutableLiveData()


    fun updateUser(accountModel: AccountModel) = viewModelScope.launch {
        getUpdateUserResponse as MutableLiveData
        getUpdateUserResponse.value = Resource.Loading
        getUpdateUserResponse.value = repository.updateUserDetails(accountModel)
    }

    fun uploadImage(image: MultipartBody.Part) = viewModelScope.launch{
        profileImgResponse as MutableLiveData
        profileImgResponse.value = Resource.Loading
        profileImgResponse.value = repository.uploadProfileImage(image)
    }

    suspend fun saveUser(name: String) {
        println("User viewMOdel ${name}")
        repository.saveNumber(name)
    }
}