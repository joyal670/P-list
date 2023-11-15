package com.iroid.jeetmeet.ui.main.parent_panel.chat

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.chat_teachers_list.ParentChatTeachersListResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentChatTeachersListViewModel : ViewModel() {

    /* variables */
    private val parentTeacherListLiveData =
        MutableLiveData<Resource<ParentChatTeachersListResponse>>()

    val parentTeacherListData: LiveData<Resource<ParentChatTeachersListResponse>>
        get() = parentTeacherListLiveData


    /* Teacher list */
    fun parentChatTeacherList(search: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentTeacherListLiveData.postValue(Resource.loading(null))
                repository.parentChatTeachersList(search).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentTeacherListLiveData.postValue(Resource.success(response))
                    } else {
                        parentTeacherListLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentTeacherListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentTeacherListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentTeacherListLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

}