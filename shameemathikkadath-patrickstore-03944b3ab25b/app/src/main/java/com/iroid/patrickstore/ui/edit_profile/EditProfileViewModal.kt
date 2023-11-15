package com.iroid.patrickstore.ui.edit_profile

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.update_profile.ProfileUpdateResponse
import com.iroid.patrickstore.model.update_profile_picture.UpdateProfilePictureResponse
import com.iroid.patrickstore.model.view_profile.ViewProfileResponse
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileViewModal : ViewModel() {

    private val liveDataViewProfile = MutableLiveData<Resource<ViewProfileResponse>>()
    val viewProfileLiveData: LiveData<Resource<ViewProfileResponse>> get() = liveDataViewProfile

    private val liveDataUpdateProfile = MutableLiveData<Resource<ProfileUpdateResponse>>()
    val updateProfileLiveData: LiveData<Resource<ProfileUpdateResponse>> get() = liveDataUpdateProfile

    private val liveDataUpdatePicture = MutableLiveData<Resource<UpdateProfilePictureResponse>>()
    val updatePictureLiveData: LiveData<Resource<UpdateProfilePictureResponse>> get() = liveDataUpdatePicture

    var firstName: MutableLiveData<String>? = null
    var lastName: MutableLiveData<String>? = null
    var emailId: MutableLiveData<String>? = null
    var mobileNumber: MutableLiveData<String>? = null

    var emptyFirstName: MutableLiveData<String>? = null
    var emptyLastName: MutableLiveData<String>? = null
    var emptyEmailId: MutableLiveData<String>? = null
    var emptyMobileNumber: MutableLiveData<String>? = null

    init {
        firstName = MutableLiveData()
        lastName = MutableLiveData()
        emailId = MutableLiveData()
        mobileNumber = MutableLiveData()

        emptyFirstName = MutableLiveData()
        emptyLastName = MutableLiveData()
        emptyEmailId = MutableLiveData()
        emptyMobileNumber = MutableLiveData()

    }

    fun onFirstNameTextChanged(text: CharSequence) {
        firstName?.value = text.toString()
        if (TextUtils.isEmpty(text.trim().toString())) {
            emptyFirstName?.value = FirstNameError
        } else {
            emptyFirstName?.value = null
        }
    }

    fun onLastNameTextChanged(text: CharSequence) {
        lastName?.value = text.toString()
        if (TextUtils.isEmpty(text.trim().toString())) {
            emptyLastName?.value = LastNameError

        } else {
            emptyLastName?.value = null
        }
    }

    fun onEmailIdTextChanged(text: CharSequence) {
        emailId?.value = text.toString()
        if (TextUtils.isEmpty(text.trim().toString())) {
            emptyEmailId?.value = EmailError
        } else {
            emptyEmailId?.value = null
        }
    }

    fun onMobileNumberTextChanged(text: CharSequence) {
        mobileNumber?.value = text.toString()
        if (TextUtils.isEmpty(text.toString())) {
            emptyMobileNumber?.value = MobileError
        } else {
            emptyMobileNumber?.value = null
        }
    }

    private fun isValid(): Boolean {
        return when {
            TextUtils.isEmpty(firstName?.value) -> {
                emptyFirstName?.value = FirstNameError
                false
            }
            TextUtils.isEmpty(lastName?.value) -> {
                emptyLastName?.value = LastNameError
                false
            }
            TextUtils.isEmpty(emailId?.value) -> {
                emptyEmailId?.value = EmailError
                false
            }
            TextUtils.isEmpty(mobileNumber?.value) -> {
                emptyMobileNumber?.value = MobileError
                false
            }
            else -> {
                true
            }
        }
    }

    fun viewProfile() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataViewProfile.postValue(Resource.loading(null))
                repository.viewProfile().let {
                    val response = it.body()

                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataViewProfile.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataViewProfile.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataViewProfile.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataViewProfile.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataViewProfile.postValue(Resource.noInterNet("", null))
            }
        }

    }

    fun updateProfile(profileImageId:String) {
        if (isValid()) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            viewModelScope.launch {
                try {
                    liveDataUpdateProfile.postValue(Resource.loading(null))
                    repository.updateProfile(
                        firstName?.value.toString(),
                        lastName?.value.toString(),
                        emailId?.value.toString(),
                        mobileNumber?.value.toString(),
                        profileImageId
                    ).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataUpdateProfile.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataUpdateProfile.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataUpdateProfile.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataUpdateProfile.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }
                } catch (ex: Exception) {
                    liveDataUpdateProfile.postValue(Resource.noInterNet("", null))
                }

            }

        }
    }

    fun updateProfilePicture(image: File, type: String) {
        if (isValid()) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            viewModelScope.launch {
                try {
                    liveDataUpdatePicture.postValue(Resource.loading(null))

                    val params = mapOf(
                        "type" to type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )

                    val imageList: ArrayList<MultipartBody.Part> = ArrayList()
                    val profileImage: MultipartBody.Part?

                    val requestProfileBody: RequestBody =
                        image.asRequestBody("*/*".toMediaTypeOrNull())
                    profileImage =
                        MultipartBody.Part.createFormData("file", image.name, requestProfileBody)
                    imageList.add(profileImage)


                    repository.updateProfilePicture(params, imageList).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataUpdatePicture.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataUpdatePicture.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataUpdatePicture.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataUpdatePicture.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }
                } catch (ex: Exception) {
                    liveDataUpdatePicture.postValue(Resource.noInterNet("", null))
                }

            }

        }
    }

}
