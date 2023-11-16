package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.chat_teacher.ParentTeacherChatResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentTeacherChatViewModel : ViewModel() {
    /* variables */
    private val parentTeacherLiveData = MutableLiveData<Resource<ParentTeacherChatResponse>>()

    val parentTeacherData: LiveData<Resource<ParentTeacherChatResponse>>
        get() = parentTeacherLiveData

    /* Student admin */
    fun parentTeacherChat() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentTeacherLiveData.postValue(Resource.loading(null))
                repository.parentTeacherChat().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentTeacherLiveData.postValue(Resource.success(response))
                    } else {
                        parentTeacherLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentTeacherLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentTeacherLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentTeacherLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

}