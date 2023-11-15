package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.mock_test_save.StudentMockTestSaveResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentMockTestSaveViewModel : ViewModel() {
    /* variables */
    private val studentMockTestSaveLiveData =
        MutableLiveData<Resource<StudentMockTestSaveResponse>>()

    val studentTestSaveData: LiveData<Resource<StudentMockTestSaveResponse>>
        get() = studentMockTestSaveLiveData

    /* Student mock test api */
    fun studentTestSave(exam_id: Int, question: ArrayList<Int>, answer: ArrayList<Int>) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentMockTestSaveLiveData.postValue(Resource.loading(null))


                repository.studentMockTestSave(exam_id, question, answer).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentMockTestSaveLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentMockTestSaveLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    } else {
                        studentMockTestSaveLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentMockTestSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentMockTestSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentMockTestSaveLiveData.postValue(Resource.error("Something went wrong", null))
                Log.e("TAG", "studentExamSave: " + ex)
            }
        }
    }
}