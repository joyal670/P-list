package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.meeting_start.ParentMeetingStartResponse
import com.iroid.jeetmeet.modal.parent.meetings.ParentMeetingsResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentMeetingsViewModel : ViewModel() {
    /* variables */
    private val parentMeetingsLiveData = MutableLiveData<Resource<ParentMeetingsResponse>>()
    private val parentMeetingStartLiveData = MutableLiveData<Resource<ParentMeetingStartResponse>>()

    val parentMeetingsData: LiveData<Resource<ParentMeetingsResponse>>
        get() = parentMeetingsLiveData

    val parentMeetingStartData: LiveData<Resource<ParentMeetingStartResponse>>
        get() = parentMeetingStartLiveData

    /* Parent meetings */
    fun parentMeetings() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentMeetingsLiveData.postValue(Resource.loading(null))
                repository.parentMeetings().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentMeetingsLiveData.postValue(Resource.success(response))
                    } else {
                        parentMeetingsLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentMeetingsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentMeetingsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentMeetingsLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Parent meeting start */
    fun parentMeetingStart(meeting_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentMeetingStartLiveData.postValue(Resource.loading(null))
                repository.parentMeetingStart(meeting_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentMeetingStartLiveData.postValue(Resource.success(response))
                    } else {
                        parentMeetingStartLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentMeetingStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentMeetingStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentMeetingStartLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}