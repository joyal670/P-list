package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.student_view.ParentStudentViewResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentStudentViewViewModel : ViewModel() {
    /* variables */
    private val parentStudentViewLiveData = MutableLiveData<Resource<ParentStudentViewResponse>>()

    val parentStudentViewData: LiveData<Resource<ParentStudentViewResponse>>
        get() = parentStudentViewLiveData

    /* Parent student view */
    fun parentStudentView(student_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentStudentViewLiveData.postValue(Resource.loading(null))
                repository.parentStudentView(student_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentStudentViewLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        parentStudentViewLiveData.postValue(Resource.error(it.message(), response))
                    } else {
                        parentStudentViewLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentStudentViewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentStudentViewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentStudentViewLiveData.postValue(Resource.error("Something went wrong", null))
                Log.e("TAG", "parentStudentView: $ex")
            }
        }
    }
}