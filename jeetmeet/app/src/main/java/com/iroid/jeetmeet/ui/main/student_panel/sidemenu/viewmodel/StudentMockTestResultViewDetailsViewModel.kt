package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.mock_test_result_view_details.StudentMockTestResultViewDetailsResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentMockTestResultViewDetailsViewModel : ViewModel() {
    /* variables */
    private val studentMockTestResultViewDetailsLiveData =
        MutableLiveData<Resource<StudentMockTestResultViewDetailsResponse>>()

    val studentTestResultViewDetailsData: LiveData<Resource<StudentMockTestResultViewDetailsResponse>>
        get() = studentMockTestResultViewDetailsLiveData

    /* Student mock test result view details api */
    fun studentTestResultViewDetails(mock_id: Int, question_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentMockTestResultViewDetailsLiveData.postValue(Resource.loading(null))
                repository.studentMockTestResultViewDetails(mock_id, question_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentMockTestResultViewDetailsLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentMockTestResultViewDetailsLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    } else {
                        studentMockTestResultViewDetailsLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentMockTestResultViewDetailsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentMockTestResultViewDetailsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (e: JsonSyntaxException) {
                Log.e("TAG", "setupObserver: JsonSyntaxException $e")
                studentMockTestResultViewDetailsLiveData.postValue(
                    Resource.error(
                        "Unable to fetch data",
                        null
                    )
                )
            } catch (ex: Exception) {
                studentMockTestResultViewDetailsLiveData.postValue(
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