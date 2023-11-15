package com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.chat_student_teachers_list.StudentChatTeachersListResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentChatTeachersListViewModel : ViewModel() {
    /* variables */
    private val studentTeacherListLiveData =
        MutableLiveData<Resource<StudentChatTeachersListResponse>>()

    val studentTeacherListData: LiveData<Resource<StudentChatTeachersListResponse>>
        get() = studentTeacherListLiveData

    /* Teacher list */
    fun studentChatTeacherList(search: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentTeacherListLiveData.postValue(Resource.loading(null))
                repository.studentChatTeachersList(search).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentTeacherListLiveData.postValue(Resource.success(response))
                    } else {
                        studentTeacherListLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentTeacherListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentTeacherListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentTeacherListLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

}