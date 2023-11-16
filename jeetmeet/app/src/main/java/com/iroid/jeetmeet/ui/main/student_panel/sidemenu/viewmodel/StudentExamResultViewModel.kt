package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.exam_result.StudentExamResultResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentExamResultViewModel : ViewModel() {
    /* variables */
    private val studentExamResultLiveData = MutableLiveData<Resource<StudentExamResultResponse>>()

    val studentExamResultData: LiveData<Resource<StudentExamResultResponse>>
        get() = studentExamResultLiveData

    /* Student exam result */
    fun studentExamResult(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentExamResultLiveData.postValue(Resource.loading(null))
                repository.studentExamResult(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentExamResultLiveData.postValue(Resource.success(response))
                    } else {
                        studentExamResultLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentExamResultLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentExamResultLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentExamResultLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}