package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.exam_schedule_preview.StudentExamSchedulePreviewResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentExamSchedulePreviewViewModel : ViewModel() {
    /* variables */
    private val studentExamSchedulePreviewLiveData =
        MutableLiveData<Resource<StudentExamSchedulePreviewResponse>>()

    val studentExamSchedulePreviewData: LiveData<Resource<StudentExamSchedulePreviewResponse>>
        get() = studentExamSchedulePreviewLiveData

    /* Student exam */
    fun studentExamSchedule(exam_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentExamSchedulePreviewLiveData.postValue(Resource.loading(null))
                repository.studentExamSchedulePreview(exam_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentExamSchedulePreviewLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentExamSchedulePreviewLiveData.postValue(
                            Resource.error(
                                "Something went wrong",
                                response
                            )
                        )
                    } else {
                        studentExamSchedulePreviewLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentExamSchedulePreviewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentExamSchedulePreviewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentExamSchedulePreviewLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }
}