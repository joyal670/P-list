package com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.activity


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.menu_day.MenuResponse
import com.proteinium.proteiniumdietapp.pojo.placeorder.PlaceOrderResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch


class MenuViewModel() : ViewModel() {
    private val menuResponseLiveData = MutableLiveData<Resource<MenuResponse>>()
    private val placeOrderResponseLiveData = MutableLiveData<Resource<PlaceOrderResponse>>()

    fun placeOrder(userId: Int, date: String, foodId: ArrayList<Int>,meal_plan_subscription_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            menuResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.placeOrder(userId, date, foodId,meal_plan_subscription_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        placeOrderResponseLiveData.postValue(Resource.success(response))
                    } else {
                        placeOrderResponseLiveData.postValue(
                            Resource.dataEmpty(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                menuResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }
        }
    }

    fun fetchMenu(userId: Int, date: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            menuResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getMenuForDay(userId, date).let {
                    val response = it.body()
                    if (response?.status!!) {
                        menuResponseLiveData.postValue(Resource.success(response))
                    } else {
                        menuResponseLiveData.postValue(
                            Resource.dataEmpty(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e("123456", "fetchMenu:$ex" )
                menuResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun getMenuResponse(): LiveData<Resource<MenuResponse>> {
        return menuResponseLiveData
    }

    fun placeOrderResponse(): LiveData<Resource<PlaceOrderResponse>> {
        return placeOrderResponseLiveData
    }
}