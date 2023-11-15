package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.online_class.StudentOnlineClassResponse
import com.iroid.jeetmeet.modal.student.online_class_start.StudentOnlineClassStartResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentOnlineClassViewModel : ViewModel() {
    /* variables */
    private val studentOnlineClassLiveData = MutableLiveData<Resource<StudentOnlineClassResponse>>()
    private val studentOnlineClassStartLiveData =
        MutableLiveData<Resource<StudentOnlineClassStartResponse>>()

    val studentOnlineClassData: LiveData<Resource<StudentOnlineClassResponse>>
        get() = studentOnlineClassLiveData

    val studentOnlineClassStartData: LiveData<Resource<StudentOnlineClassStartResponse>>
        get() = studentOnlineClassStartLiveData

    /* Student online class */
    fun studentOnlineClass() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentOnlineClassLiveData.postValue(Resource.loading(null))
                repository.studentOnlineClass().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentOnlineClassLiveData.postValue(Resource.success(response))
                    } else {
                        studentOnlineClassLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentOnlineClassLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentOnlineClassLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentOnlineClassLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student online class start*/
    fun studentOnlineClassStart(id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentOnlineClassStartLiveData.postValue(Resource.loading(null))
                repository.studentOnlineClassStart(id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentOnlineClassStartLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentOnlineClassStartLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    } else {
                        studentOnlineClassStartLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentOnlineClassStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentOnlineClassStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentOnlineClassStartLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }
}