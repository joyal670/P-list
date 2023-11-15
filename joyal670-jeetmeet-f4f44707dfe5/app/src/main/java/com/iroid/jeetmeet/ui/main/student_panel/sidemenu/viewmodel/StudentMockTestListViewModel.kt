package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.mock_test_list.StudentMockTestListResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentMockTestListViewModel : ViewModel() {
    /* variables */
    private val studentMockTestListLiveData =
        MutableLiveData<Resource<StudentMockTestListResponse>>()

    val studentMockTestListData: LiveData<Resource<StudentMockTestListResponse>>
        get() = studentMockTestListLiveData


    /* Student mock test list */
    fun studentMockTestList(tdate: String, sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentMockTestListLiveData.postValue(Resource.loading(null))
                repository.studentMockTestList(tdate, sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentMockTestListLiveData.postValue(Resource.success(response))
                    } else {
                        studentMockTestListLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentMockTestListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentMockTestListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentMockTestListLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}