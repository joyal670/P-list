package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.leave_assigned.ParentLeaveAssignedResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentLeaveAssignedViewModel : ViewModel() {
    /* variables */
    private val parentLeaveLiveData = MutableLiveData<Resource<ParentLeaveAssignedResponse>>()

    val parentLeaveData: LiveData<Resource<ParentLeaveAssignedResponse>>
        get() = parentLeaveLiveData

    /* Parent leave assigned */
    fun parentLeaveAssigned(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentLeaveLiveData.postValue(Resource.loading(null))
                repository.parentLeaveAssigned(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentLeaveLiveData.postValue(Resource.success(response))
                    } else {
                        parentLeaveLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentLeaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}