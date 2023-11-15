package com.iroid.jeetmeet.ui.main.student_panel.home.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.dashboard.StudentDashboardResponse
import com.iroid.jeetmeet.modal.student.notice.StudentNoticeResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentDashboardViewModel : ViewModel() {
    /* variables */
    private val studentDashboardLiveData = MutableLiveData<Resource<StudentDashboardResponse>>()
    private val studentNoticeLiveData = MutableLiveData<Resource<StudentNoticeResponse>>()

    val studentDashboardData: LiveData<Resource<StudentDashboardResponse>>
        get() = studentDashboardLiveData

    val studentNoticeData: LiveData<Resource<StudentNoticeResponse>>
        get() = studentNoticeLiveData


    /* Student dashboard */
    fun studentDashboard() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentDashboardLiveData.postValue(Resource.loading(null))
                repository.studentDashboard().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentDashboardLiveData.postValue(Resource.success(response))
                    } else {
                        studentDashboardLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentDashboardLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentDashboardLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentDashboardLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student notice */
    fun studentNotice(notice_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentNoticeLiveData.postValue(Resource.loading(null))
                repository.studentNotice(notice_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentNoticeLiveData.postValue(Resource.success(response))
                    } else {
                        studentNoticeLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentNoticeLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentNoticeLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentNoticeLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

}