package com.ncomfortsagent.ui.main.sideMenu.myProfile.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.profile.AgentProfileResponse
import com.ncomfortsagent.model.profile_image_remove.AgentProfileImageRemoveResponse
import com.ncomfortsagent.model.profile_update.AgentProfileUpdateResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.UnknownHostException

class ProfileViewModel(@SuppressLint("StaticFieldLeak") private var activity: Activity) :
    ViewModel() {

    private val profileLiveData = MutableLiveData<Resource<AgentProfileResponse>>()
    private val profileImageRemoveLiveData =
        MutableLiveData<Resource<AgentProfileImageRemoveResponse>>()
    private val profileUpdateLiveData = MutableLiveData<Resource<AgentProfileUpdateResponse>>()


    fun getAgentProfileResponse(): LiveData<Resource<AgentProfileResponse>> {
        return profileLiveData
    }
    fun profile() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            profileLiveData.postValue(Resource.loading(null))
            try {
                repository.profile().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        profileLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        profileLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        profileLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        profileLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                profileLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                profileLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                profileLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }


    fun getAgentProfileImageRemoveResponse(): LiveData<Resource<AgentProfileImageRemoveResponse>> {
        return profileImageRemoveLiveData
    }
    fun profileImageRemove() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            profileImageRemoveLiveData.postValue(Resource.loading(null))
            try {
                repository.profileImageRemove().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        profileImageRemoveLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        profileImageRemoveLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        profileImageRemoveLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        profileImageRemoveLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                profileImageRemoveLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                profileImageRemoveLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                profileImageRemoveLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }


    fun getAgentProfileUpdateResponse(): LiveData<Resource<AgentProfileUpdateResponse>> {
        return profileUpdateLiveData
    }
    fun profileUpdate(name: String, email: String, address: String, phone: String, image: File?) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            profileUpdateLiveData.postValue(Resource.loading(null))
            try {

                val params = mapOf(
                    "name" to name.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                    "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "address" to address.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "phone" to phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
                val imageList : ArrayList<MultipartBody.Part> = ArrayList()
                val profileImage: MultipartBody.Part?

                if(image != null)
                {
                    val requestProfileBody : RequestBody = image.asRequestBody("*/*".toMediaTypeOrNull())
                    profileImage = MultipartBody.Part.createFormData("image" , image.name , requestProfileBody)
                    imageList.add(profileImage)
                }


                repository.profileUpdate(params, imageList).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        profileUpdateLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        profileUpdateLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        profileUpdateLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        profileUpdateLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                profileUpdateLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                profileUpdateLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                profileUpdateLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }

}