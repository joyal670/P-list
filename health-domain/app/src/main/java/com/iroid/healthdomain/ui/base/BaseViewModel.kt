package com.iroid.healthdomain.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iroid.healthdomain.data.dummyModel.FitModel

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val isLoading: LiveData<Boolean> = MutableLiveData()
    val stepsLiveData: LiveData<FitModel> = MutableLiveData()

    fun setLoading(load: Boolean) {
        isLoading as MutableLiveData
        isLoading.value = load
    }


    fun setSteps(model: FitModel) {
        stepsLiveData as MutableLiveData
        stepsLiveData.value = model
    }

    suspend fun saveFitStatus(boolean: Boolean) {

    }

}