package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.profile.ParentProfileResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentProfileViewModel : ViewModel() {
    /* variables */
    private val parentProfileLiveData = MutableLiveData<Resource<ParentProfileResponse>>()

    val parentProfileData: LiveData<Resource<ParentProfileResponse>>
        get() = parentProfileLiveData

    /* Parent profile */
    fun parentProfile() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentProfileLiveData.postValue(Resource.loading(null))
                repository.parentProfile().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentProfileLiveData.postValue(Resource.success(response))
                    } else {
                        parentProfileLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentProfileLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentProfileLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentProfileLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}