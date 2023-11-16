package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.exam_result_details.StudentExamResultDetailsResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentExamResultDetailsViewModel : ViewModel() {
    /* variables */
    private val studentExamResultDetailsLiveData =
        MutableLiveData<Resource<StudentExamResultDetailsResponse>>()

    val studentExamResultDetailsData: LiveData<Resource<StudentExamResultDetailsResponse>>
        get() = studentExamResultDetailsLiveData

    /* Student result */
    fun studentResult(exam_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentExamResultDetailsLiveData.postValue(Resource.loading(null))
                repository.studentExamResultDetails(exam_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentExamResultDetailsLiveData.postValue(Resource.success(response))
                    } else {
                        studentExamResultDetailsLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentExamResultDetailsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentExamResultDetailsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                Log.e("TAG", "studentResult: "+ ex )
                studentExamResultDetailsLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }
}