package com.iroid.jeetmeet.ui.main.parent_panel.home.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.side_menu.ParentSideMenuResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentSideMenuViewModel : ViewModel() {
    /* variables */
    private val parentSideMenuLiveData = MutableLiveData<Resource<ParentSideMenuResponse>>()

    val parentSideMenuData: LiveData<Resource<ParentSideMenuResponse>>
        get() = parentSideMenuLiveData

    /* Parent side menu */
    fun parentSideMenu() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentSideMenuLiveData.postValue(Resource.loading(null))
                repository.parentSideMenu().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentSideMenuLiveData.postValue(Resource.success(response))
                    } else {
                        parentSideMenuLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentSideMenuLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentSideMenuLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentSideMenuLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}