package com.iroid.jeetmeet.start_up.auth.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.login.StudentLoginResponse
import com.iroid.jeetmeet.modal.student.logout.StudentLogoutResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentLoginViewModel : ViewModel() {
    /* variables */
    private val studentLoginLiveData = MutableLiveData<Resource<StudentLoginResponse>>()
    private val studentLogoutLiveData = MutableLiveData<Resource<StudentLogoutResponse>>()

    val studentLoginData: LiveData<Resource<StudentLoginResponse>>
        get() = studentLoginLiveData

    val studentLogoutData: LiveData<Resource<StudentLogoutResponse>>
        get() = studentLogoutLiveData

    /* Student login */
    fun studentLoginApi(email: String, password: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentLoginLiveData.postValue(Resource.loading(null))
                repository.studentLogin(email, password).let {
                    val response = it.body()
                    if (it.isSuccessful) {
                        studentLoginLiveData.postValue(Resource.success(response))
                    } else {
                        if (it.code() == Constants.REQUEST_BAD_REQUEST) {
                            studentLoginLiveData.postValue(
                                Resource.error(
                                    "The username must be a valid email address",
                                    response
                                )
                            )
                        }
                        if (it.code() == Constants.REQUEST_UNAUTHORIZED) {
                            studentLoginLiveData.postValue(Resource.error("Unauthorized", response))
                        }
                    }

                }
            } catch (ex: UnknownHostException) {
                studentLoginLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentLoginLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentLoginLiveData.postValue(Resource.error("Something went wrong", null))
                Log.e("TAG", "studentLoginApi----: $ex")
            }
        }
    }

    /* student logout */
    fun studentLogoutApi() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentLogoutLiveData.postValue(Resource.loading(null))
                repository.studentLogout().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentLogoutLiveData.postValue(Resource.success(it.body()))
                    }
                    if (response.status == Constants.REQUEST_FAILED) {
                        studentLogoutLiveData.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: java.lang.Exception) {
                studentLogoutLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}