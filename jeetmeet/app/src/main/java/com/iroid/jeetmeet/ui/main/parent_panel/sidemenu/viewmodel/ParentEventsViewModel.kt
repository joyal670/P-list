package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.events.ParentEventsResponse
import com.iroid.jeetmeet.modal.parent.events.new.ParentEventsNewResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentEventsViewModel : ViewModel() {
    /* variables */
    private val parentEventsLiveData = MutableLiveData<Resource<ParentEventsNewResponse>>()

    val parentEventsData: LiveData<Resource<ParentEventsNewResponse>>
        get() = parentEventsLiveData

    /* Parent events */
    fun parentEvents(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentEventsLiveData.postValue(Resource.loading(null))
                repository.parentEvents(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentEventsLiveData.postValue(Resource.success(response))
                    } else {
                        parentEventsLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentEventsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentEventsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentEventsLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}