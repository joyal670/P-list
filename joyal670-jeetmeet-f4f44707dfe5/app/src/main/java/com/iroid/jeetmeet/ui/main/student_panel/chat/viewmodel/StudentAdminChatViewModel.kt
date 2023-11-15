package com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.chat_admin.StudentChatAdminResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentAdminChatViewModel : ViewModel() {
    /* variables */
    private val studentAdminLiveData = MutableLiveData<Resource<StudentChatAdminResponse>>()

    val studentAdminData: LiveData<Resource<StudentChatAdminResponse>>
        get() = studentAdminLiveData

    /* Student admin */
    fun studentAdminChat() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentAdminLiveData.postValue(Resource.loading(null))
                repository.studentAdminChat().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentAdminLiveData.postValue(Resource.success(response))
                    } else {
                        studentAdminLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentAdminLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentAdminLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentAdminLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

}