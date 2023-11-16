package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.mock_test_preview.StudentMockTestPreviewResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentMockTestPreviewViewModel : ViewModel() {
    /* variables */
    private val studentMockTestPreviewLiveData =
        MutableLiveData<Resource<StudentMockTestPreviewResponse>>()

    val studentMockTestPreviewData: LiveData<Resource<StudentMockTestPreviewResponse>>
        get() = studentMockTestPreviewLiveData


    /* Student diaries */
    fun studentDiaries(test_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentMockTestPreviewLiveData.postValue(Resource.loading(null))
                repository.studentMockTestPreview(test_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentMockTestPreviewLiveData.postValue(Resource.success(response))
                    } else {
                        studentMockTestPreviewLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentMockTestPreviewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentMockTestPreviewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentMockTestPreviewLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }
}