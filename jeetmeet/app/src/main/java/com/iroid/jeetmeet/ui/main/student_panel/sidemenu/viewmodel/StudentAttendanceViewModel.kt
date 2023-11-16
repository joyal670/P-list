package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.attendance.StudentAttendanceResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentAttendanceViewModel : ViewModel() {
    /* variables */
    private val studentAttendanceLiveData = MutableLiveData<Resource<StudentAttendanceResponse>>()

    val studentAttendanceData: LiveData<Resource<StudentAttendanceResponse>>
        get() = studentAttendanceLiveData

    /* Student attendance */
    fun studentAttendance() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentAttendanceLiveData.postValue(Resource.loading(null))
                repository.studentAttendance().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentAttendanceLiveData.postValue(Resource.success(response))
                    } else {
                        studentAttendanceLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentAttendanceLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentAttendanceLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentAttendanceLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}