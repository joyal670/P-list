package com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.chat_student_admin_read.StudentChatAdminStudentReadResponse
import com.iroid.jeetmeet.modal.student.chat_student_admin_update.StudentChatAdminStudentUpdateResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentChatAdminStudentReadViewModel : ViewModel() {
    /* variables */
    private val studentChatAdminStudentReadLiveData =
        MutableLiveData<Resource<StudentChatAdminStudentReadResponse>>()
    private val studentChatAdminStudentUpdateLiveData =
        MutableLiveData<Resource<StudentChatAdminStudentUpdateResponse>>()

    val studentChatAdminStudentReadData: LiveData<Resource<StudentChatAdminStudentReadResponse>>
        get() = studentChatAdminStudentReadLiveData

    val studentChatAdminStudentUpdateData: LiveData<Resource<StudentChatAdminStudentUpdateResponse>>
        get() = studentChatAdminStudentUpdateLiveData


    /* Student admin chat read */
    fun studentChatAdminStudentRead(chat_id: String) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentChatAdminStudentReadLiveData.postValue(Resource.loading(null))
                repository.studentChatAdminStudentRead(chat_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentChatAdminStudentReadLiveData.postValue(Resource.success(response))
                    } else {
                        studentChatAdminStudentReadLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentChatAdminStudentReadLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentChatAdminStudentReadLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentChatAdminStudentReadLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }

    }

    /* Student admin chat header update*/
    fun studentChatAdminStudentUpdate(chat_id: String, message: String) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentChatAdminStudentUpdateLiveData.postValue(Resource.loading(null))
                repository.studentChatAdminStudentUpdate(chat_id, message).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentChatAdminStudentUpdateLiveData.postValue(Resource.success(response))
                    } else {
                        studentChatAdminStudentUpdateLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentChatAdminStudentUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentChatAdminStudentUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentChatAdminStudentUpdateLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
                Log.e("TAG", "studentChatAdminStudentUpdate: $ex")
            }
        }

    }

}