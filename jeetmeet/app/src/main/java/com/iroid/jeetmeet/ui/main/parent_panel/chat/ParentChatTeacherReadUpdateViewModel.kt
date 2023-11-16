package com.iroid.jeetmeet.ui.main.parent_panel.chat

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.chat_teacher_message_read.ParentChatTeacherMessageReadResponse
import com.iroid.jeetmeet.modal.parent.chat_teacher_message_update.ParentChatTeacherMessageUpdateResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentChatTeacherReadUpdateViewModel : ViewModel() {

    /* variables */
    private val parentChatTeacherUpdateLiveData =
        MutableLiveData<Resource<ParentChatTeacherMessageUpdateResponse>>()

    private val parentChatTeacherReadLiveData =
        MutableLiveData<Resource<ParentChatTeacherMessageReadResponse>>()

    val parentChatTeacherUpdateData: LiveData<Resource<ParentChatTeacherMessageUpdateResponse>>
        get() = parentChatTeacherUpdateLiveData

    val parentChatTeacherReadData: LiveData<Resource<ParentChatTeacherMessageReadResponse>>
        get() = parentChatTeacherReadLiveData


    /* Parent-Teacher chat header update*/
    fun parentChatTeacherMessageUpdate(chat_id: String, message: String) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentChatTeacherUpdateLiveData.postValue(Resource.loading(null))
                repository.parentChatTeacherMessageUpdate(chat_id, message).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentChatTeacherUpdateLiveData.postValue(Resource.success(response))
                    } else {
                        parentChatTeacherUpdateLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentChatTeacherUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentChatTeacherUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentChatTeacherUpdateLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* Parent-Teacher chat read*/
    fun parentChatTeacherMessageRead(chat_id: String) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentChatTeacherReadLiveData.postValue(Resource.loading(null))
                repository.parentChatTeacherMessageRead(chat_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentChatTeacherReadLiveData.postValue(Resource.success(response))
                    } else {
                        parentChatTeacherReadLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentChatTeacherReadLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentChatTeacherReadLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentChatTeacherReadLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

}