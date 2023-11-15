package com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.add_subscription.AddSubscriptionResponse
import com.proteinium.proteiniumdietapp.pojo.meal_plan.MealPlanResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch


class MealPlanViewModel() : ViewModel() {

    private val mealPlanResponseLiveData = MutableLiveData<Resource<MealPlanResponse>>()
    private val mealPlanAddResponseLiveData = MutableLiveData<Resource<AddSubscriptionResponse>>()

    fun fetchMealPlanResponse(mealPlanId: Int, user_id : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            mealPlanResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getMealPlan(mealPlanId,user_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        mealPlanResponseLiveData.postValue(Resource.success(response))
                    } else {
                        mealPlanResponseLiveData.postValue(
                            Resource.dataEmpty(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e("123", "fetchMealPlanResponse: " + ex)
                mealPlanResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun addInitialSubscriptionPlan(
        user_id: Int,
        start_date: String,
        meal_plan_id: Int,
        non_stop_delivery_price: String,
        carbs: String,
        carbs_price: String,
        proteins: String,
        proteins_price: String,
        comments: String,
        duration: Int,
        base_price: String,
        code: String,
        suspend: String,
        enable_modification: Int,
        extrasArrayList: ArrayList<String>
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            mealPlanResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.addInitialSubscriptionPlan(
                    user_id,
                    start_date,
                    meal_plan_id,
                    non_stop_delivery_price,
                    carbs,
                    carbs_price,
                    proteins,
                    proteins_price,
                    comments,
                    duration,
                    base_price,
                    code,
                    suspend,
                    enable_modification,
                    extrasArrayList
                ).let {
                    val response = it.body()
                    if (response?.status!!) {
                        mealPlanAddResponseLiveData.postValue(Resource.success(response))
                    } else {
                        mealPlanAddResponseLiveData.postValue(
                            Resource.dataEmpty(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e("123", "fetchMealPlanResponse: " + ex)
                mealPlanResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }


    fun getMealPlanResponse(): LiveData<Resource<MealPlanResponse>> {
        return mealPlanResponseLiveData
    }

    fun getMealPlanAddResponse(): LiveData<Resource<AddSubscriptionResponse>> {
        return mealPlanAddResponseLiveData
    }
}