package com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.chat_student_teacher_read.StudentTeacherChatReadResponse
import com.iroid.jeetmeet.modal.student.chat_student_teacher_update.StudentTeacherChatUpdateResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentChatTeacherStudentReadViewModel : ViewModel() {
    /* variables */
    private val studentTeacherChatUpdateLiveData =
        MutableLiveData<Resource<StudentTeacherChatUpdateResponse>>()

    private val studentTeacherChatReadLiveData =
        MutableLiveData<Resource<StudentTeacherChatReadResponse>>()

    val studentTeacherChatUpdateData: LiveData<Resource<StudentTeacherChatUpdateResponse>>
        get() = studentTeacherChatUpdateLiveData

    val studentTeacherChatReadData: LiveData<Resource<StudentTeacherChatReadResponse>>
        get() = studentTeacherChatReadLiveData


    /* Student-Teacher chat header update*/
    fun studentTeacherChatUpdate(chat_id: String, message: String) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentTeacherChatUpdateLiveData.postValue(Resource.loading(null))
                repository.studentTeacherChatUpdate(chat_id, message).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentTeacherChatUpdateLiveData.postValue(Resource.success(response))
                    } else {
                        studentTeacherChatUpdateLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentTeacherChatUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentTeacherChatUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentTeacherChatUpdateLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* Student-Teacher chat read */
    fun studentTeacherChatRead(chat_id: String) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentTeacherChatReadLiveData.postValue(Resource.loading(null))
                repository.studentTeacherChatRead(chat_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentTeacherChatReadLiveData.postValue(Resource.success(response))
                    } else {
                        studentTeacherChatReadLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentTeacherChatReadLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentTeacherChatReadLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentTeacherChatReadLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

}