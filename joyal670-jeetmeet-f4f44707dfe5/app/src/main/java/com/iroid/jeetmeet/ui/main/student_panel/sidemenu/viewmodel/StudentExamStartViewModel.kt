package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.exam_start.StudentExamStartResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentExamStartViewModel : ViewModel() {
    /* variables */
    private val studentExamStartLiveData = MutableLiveData<Resource<StudentExamStartResponse>>()

    val studentExamStartData: LiveData<Resource<StudentExamStartResponse>>
        get() = studentExamStartLiveData

    /* Student exam */
    fun studentExam(exam_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentExamStartLiveData.postValue(Resource.loading(null))
                repository.studentExamStart(exam_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentExamStartLiveData.postValue(Resource.success(response))
                    } else {
                        studentExamStartLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentExamStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentExamStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentExamStartLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}