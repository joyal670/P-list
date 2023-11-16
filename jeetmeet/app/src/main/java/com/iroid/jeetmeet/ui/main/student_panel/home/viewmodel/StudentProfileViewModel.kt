package com.iroid.jeetmeet.ui.main.student_panel.home.viewmodel

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.edit_name.StudentEditNameResponse
import com.iroid.jeetmeet.modal.student.edit_profile_image.StudentEditProfileImageResponse
import com.iroid.jeetmeet.modal.student.profile.StudentProfileResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.EOFException
import java.io.File
import java.io.IOException
import java.net.UnknownHostException

class StudentProfileViewModel(private val context: Context) : ViewModel() {
    /* variables */
    private val studentProfileLiveData = MutableLiveData<Resource<StudentProfileResponse>>()
    private val studentNameEditLiveData = MutableLiveData<Resource<StudentEditNameResponse>>()
    private val studentImageEditLiveData =
        MutableLiveData<Resource<StudentEditProfileImageResponse>>()

    val studentProfileData: LiveData<Resource<StudentProfileResponse>>
        get() = studentProfileLiveData

    val studentEditNameData: LiveData<Resource<StudentEditNameResponse>>
        get() = studentNameEditLiveData

    val studentEditImageData: LiveData<Resource<StudentEditProfileImageResponse>>
        get() = studentImageEditLiveData

    /* Student profile */
    fun studentProfileApi() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentProfileLiveData.postValue(Resource.loading(null))
                repository.studentProfile().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentProfileLiveData.postValue(Resource.success(response))
                    } else {
                        studentProfileLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentProfileLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentProfileLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentProfileLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student name edit */
    fun studentNameEditApi(first_name: String, middle_name: String, last_name: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentNameEditLiveData.postValue(Resource.loading(null))
                repository.studentNameEdit(first_name, middle_name, last_name).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentNameEditLiveData.postValue(Resource.success(response))
                    } else {
                        studentNameEditLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentNameEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentNameEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentNameEditLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student profile image edit */
    fun studentProfileImageEditApi(profile_image: File) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        studentImageEditLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {

            val imageList: ArrayList<MultipartBody.Part> = ArrayList()
            var ProfileImage: MultipartBody.Part? = null
            var compressedFileProfileImage: File? = null

            try {

                if (profile_image != null) {
                    try {
                        compressedFileProfileImage = profile_image.let {
                            Compressor.compress(context, it, Dispatchers.IO)
                        }
                    } catch (e: EOFException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val requestProfileBody: RequestBody =
                        compressedFileProfileImage!!.asRequestBody("*/*".toMediaTypeOrNull())
                    ProfileImage = MultipartBody.Part.createFormData(
                        "profile_image",
                        compressedFileProfileImage.name,
                        requestProfileBody
                    )
                    imageList.add(ProfileImage)

                } else {

                }
                repository.studentProfilePhotoEdit(imageList).let {
                    if (it.isSuccessful) {
                        studentImageEditLiveData.postValue(Resource.success(it.body()))
                    } else {
                        studentImageEditLiveData.postValue(Resource.error(it.message(), null))
                    }
                }

            } catch (ex: UnknownHostException) {
                studentImageEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentImageEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentImageEditLiveData.postValue(Resource.error("Something went wrong", null))
            }


        }
    }
}