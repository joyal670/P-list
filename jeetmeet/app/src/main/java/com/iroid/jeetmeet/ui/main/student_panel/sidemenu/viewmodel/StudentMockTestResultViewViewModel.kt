package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.mock_test_result_view.StudentMockTestResultViewResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentMockTestResultViewViewModel : ViewModel() {
    /* variables */
    private val studentMockTestResultViewLiveData =
        MutableLiveData<Resource<StudentMockTestResultViewResponse>>()

    val studentTestResultViewData: LiveData<Resource<StudentMockTestResultViewResponse>>
        get() = studentMockTestResultViewLiveData

    /* Student mock test result view api */
    fun studentTestResultView(mock_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentMockTestResultViewLiveData.postValue(Resource.loading(null))


                repository.studentMockTestResultView(mock_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentMockTestResultViewLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentMockTestResultViewLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    } else {
                        studentMockTestResultViewLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentMockTestResultViewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentMockTestResultViewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentMockTestResultViewLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
                Log.e("TAG", "studentExamSave: $ex")
            }
        }
    }
}