package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.time_table.ParentTimeTableResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentTimeTableViewModel : ViewModel() {
    /* variables */
    private val parentTimeTableLiveData = MutableLiveData<Resource<ParentTimeTableResponse>>()

    val parentTimeTableData: LiveData<Resource<ParentTimeTableResponse>>
        get() = parentTimeTableLiveData

    /* Parent time table */
    fun parentTimeTable(student_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentTimeTableLiveData.postValue(Resource.loading(null))
                repository.parentTimeTable(student_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentTimeTableLiveData.postValue(Resource.success(response))
                    } else {
                        parentTimeTableLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentTimeTableLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentTimeTableLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentTimeTableLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}