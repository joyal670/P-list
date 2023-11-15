package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.attend_exam.StudentAttendExamResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentAttendExamViewModel : ViewModel() {
    /* variables */
    private val studentAttendExamLiveData = MutableLiveData<Resource<StudentAttendExamResponse>>()

    val studentAttendData: LiveData<Resource<StudentAttendExamResponse>>
        get() = studentAttendExamLiveData

    /* Student attend exam api */
    fun studentAttendExam(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentAttendExamLiveData.postValue(Resource.loading(null))
                repository.studentAttendExam(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentAttendExamLiveData.postValue(Resource.success(response))
                    } else {
                        studentAttendExamLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentAttendExamLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentAttendExamLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentAttendExamLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}