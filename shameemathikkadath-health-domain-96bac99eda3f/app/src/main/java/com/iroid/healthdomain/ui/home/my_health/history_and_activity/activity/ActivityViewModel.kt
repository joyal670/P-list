package com.iroid.healthdomain.ui.home.my_health.history_and_activity.activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.activity_api.UpdateActivityResponse
import com.iroid.healthdomain.data.model_class.updated_steps_data.SendStepsUpdates
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository
import kotlinx.coroutines.launch

class ActivityViewModel(val repository: MyHealthRepository, application: Application) : BaseViewModel(application){

    val updateStepsActivityResponse: LiveData<Resource<UpdateActivityResponse>> = MutableLiveData()

    fun sendSteps(steps: SendStepsUpdates) =
        try {
            viewModelScope.launch {
                updateStepsActivityResponse as MutableLiveData
                updateStepsActivityResponse.value = Resource.Loading

                updateStepsActivityResponse.value = repository.sendStepsUpdated(steps)

            }
        }catch (ex:Exception){
            Log.e("1234656", "sendSteps:$ex " )
            ex.printStackTrace()

        }


//    fun sendSteps(steps: String,date:String) =
//        t
//        viewModelScope.launch {
//        try {
//            updateStepsActivityResponse as MutableLiveData
//            updateStepsActivityResponse.value = Resource.Loading
//
//            updateStepsActivityResponse.value = repository.sendStepsUpdated(steps,date)
//        }catch (ex:Exception){
//            ex.printStackTrace()
//
//        }
//    }

}