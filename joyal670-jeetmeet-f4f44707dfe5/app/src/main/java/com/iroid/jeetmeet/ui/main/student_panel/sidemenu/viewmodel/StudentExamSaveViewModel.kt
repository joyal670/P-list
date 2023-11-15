package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.exam_save.StudentExamSaveResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentExamSaveViewModel : ViewModel() {
    /* variables */
    private val studentExamSaveLiveData = MutableLiveData<Resource<StudentExamSaveResponse>>()

    val studentExamSaveData: LiveData<Resource<StudentExamSaveResponse>>
        get() = studentExamSaveLiveData

    /* Student exam api */
    fun studentExamSave(exam_id: Int, question: ArrayList<Int>, answer: ArrayList<String>) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentExamSaveLiveData.postValue(Resource.loading(null))


                repository.studentExamSave(exam_id, question, answer).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentExamSaveLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentExamSaveLiveData.postValue(Resource.error(it.message(), response))
                    } else {
                        studentExamSaveLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentExamSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentExamSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentExamSaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}