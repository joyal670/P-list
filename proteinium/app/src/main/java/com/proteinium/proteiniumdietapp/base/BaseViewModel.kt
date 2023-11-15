package com.proteinium.proteiniumdietapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    val isLoading: LiveData<Boolean> = MutableLiveData()

    fun setLoading(load: Boolean) {
        isLoading as MutableLiveData
        isLoading.value = load
    }
}