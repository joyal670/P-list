package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.exam_schedule.StudentExamScheduleResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentExamScheduleViewModel : ViewModel() {
    /* variables */
    private val studentExamScheduleLiveData =
        MutableLiveData<Resource<StudentExamScheduleResponse>>()

    val studentExamScheduleData: LiveData<Resource<StudentExamScheduleResponse>>
        get() = studentExamScheduleLiveData

    /* Student exam schedule api */
    fun studentExamSchedule(edate: String, sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentExamScheduleLiveData.postValue(Resource.loading(null))
                repository.studentExamSchedule(edate, sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentExamScheduleLiveData.postValue(Resource.success(response))
                    } else {
                        studentExamScheduleLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentExamScheduleLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentExamScheduleLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentExamScheduleLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}