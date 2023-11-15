package com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.chat_student_teacher.StudentChatTeacherResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentTeacherChatViewModel : ViewModel() {
    /* variables */
    private val studentTeacherLiveData = MutableLiveData<Resource<StudentChatTeacherResponse>>()

    val studentTeacherData: LiveData<Resource<StudentChatTeacherResponse>>
        get() = studentTeacherLiveData

    /* Student admin */
    fun studentAdminChat() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentTeacherLiveData.postValue(Resource.loading(null))
                repository.studentTeacherChat().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentTeacherLiveData.postValue(Resource.success(response))
                    } else {
                        studentTeacherLiveData.postValue(Resource.error(it.message(), response))
                    }
                }
            } catch (ex: UnknownHostException) {
                studentTeacherLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentTeacherLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentTeacherLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

}