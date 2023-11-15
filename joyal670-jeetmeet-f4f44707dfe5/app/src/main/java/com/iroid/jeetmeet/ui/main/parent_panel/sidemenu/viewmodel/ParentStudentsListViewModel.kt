package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentStudentsListViewModel : ViewModel() {
    /* variables */
    private val parentStudentsListLiveData = MutableLiveData<Resource<ParentStudentsListResponse>>()

    val parentStudentsListData: LiveData<Resource<ParentStudentsListResponse>>
        get() = parentStudentsListLiveData

    /* Students list */
    fun parentStudentsList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentStudentsListLiveData.postValue(Resource.loading(null))
                repository.parentStudentsList().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentStudentsListLiveData.postValue(Resource.success(response))
                    } else {
                        parentStudentsListLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentStudentsListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentStudentsListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentStudentsListLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}