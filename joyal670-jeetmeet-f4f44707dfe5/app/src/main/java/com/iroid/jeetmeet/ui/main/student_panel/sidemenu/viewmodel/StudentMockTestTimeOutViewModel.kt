package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.mock_test_timeout.StudentMockTestTimeOutRespose
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentMockTestTimeOutViewModel : ViewModel() {
    /* variables */
    private val studentMockTestTimeOutLiveData =
        MutableLiveData<Resource<StudentMockTestTimeOutRespose>>()

    val studentTestTimeOutData: LiveData<Resource<StudentMockTestTimeOutRespose>>
        get() = studentMockTestTimeOutLiveData

    /* Student mock test api */
    fun studentTestTimeOut(exam_id: Int, question: ArrayList<Int>, answer: ArrayList<Int>) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentMockTestTimeOutLiveData.postValue(Resource.loading(null))


                repository.studentMockTestTimeout(exam_id, question, answer).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentMockTestTimeOutLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentMockTestTimeOutLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    } else {
                        studentMockTestTimeOutLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentMockTestTimeOutLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentMockTestTimeOutLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentMockTestTimeOutLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
                Log.e("TAG", "studentExamSave: $ex")
            }
        }
    }
}