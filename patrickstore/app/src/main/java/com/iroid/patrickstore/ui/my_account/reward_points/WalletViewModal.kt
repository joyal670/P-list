package com.iroid.patrickstore.ui.my_account.reward_points

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_review.AddReviewResponse
import com.iroid.patrickstore.model.cashback.CashbackResponse
import com.iroid.patrickstore.model.delivered_order.DeliveredOrderResponse
import com.iroid.patrickstore.model.edit_review.EditReviewResponse
import com.iroid.patrickstore.model.product_reviews.ProductReviewResponse
import com.iroid.patrickstore.model.reward.RewardResponse
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WalletViewModal : ViewModel() {
    private val liveDataDeliveredOrder=MutableLiveData<Resource<DeliveredOrderResponse>>()
    val deliveredLiveData:LiveData<Resource<DeliveredOrderResponse>> get() = liveDataDeliveredOrder


    private val liveDataReward=MutableLiveData<Resource<RewardResponse>>()
    val rewardLiveData:LiveData<Resource<RewardResponse>> get() = liveDataReward

    private val liveDataAddReview=MutableLiveData<Resource<AddReviewResponse>>()
    val addReviewLiveData:LiveData<Resource<AddReviewResponse>> get() = liveDataAddReview

    private val liveDataProductReview=MutableLiveData<Resource<ProductReviewResponse>>()
    val productReviewLiveData:LiveData<Resource<ProductReviewResponse>> get() = liveDataProductReview

    private val liveDataUpdateReview=MutableLiveData<Resource<EditReviewResponse>>()
    val updateReviewLiveData:LiveData<Resource<EditReviewResponse>> get() = liveDataUpdateReview

    private val liveDataCashBackList=MutableLiveData<Resource<CashbackResponse>>()
    val cashBackLiveData:LiveData<Resource<CashbackResponse>> get() = liveDataCashBackList

    fun getCashbackLiveData(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                repository.getCashBackList().let {
                    Log.e("12345", "getCashbackLiveData:$it ", )
                    liveDataCashBackList.postValue(Resource.loading(null))
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataCashBackList.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataCashBackList.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataCashBackList.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataCashBackList.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){
                Log.e("#12346","$ex")
                liveDataCashBackList.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun getProductReview(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                repository.getProductReview(1,20).let {
                    liveDataProductReview.postValue(Resource.loading(null))
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataProductReview.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataProductReview.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataProductReview.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataProductReview.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){

            }
        }
    }

    fun addReview(title:String,
                  productId:String,
                  rating:Float,
                  review:String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataAddReview.postValue(Resource.loading(null))
                repository.addReview(title, productId, rating, review).let {
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataAddReview.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataAddReview.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataAddReview.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataAddReview.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){
                liveDataAddReview.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun getDeliveredOrder(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataDeliveredOrder.postValue(Resource.loading(null))
                repository.getDeliveredOrders().let {
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataDeliveredOrder.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataDeliveredOrder.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataDeliveredOrder.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataDeliveredOrder.postValue(Resource.noInterNet("", null))
                        }
                    }
                }

            }catch (ex:Exception){
                Log.e("#12346","error $ex")
                liveDataDeliveredOrder.postValue(Resource.noInterNet("", null))
            }
        }
    }
    fun getReward(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataReward.postValue(Resource.loading(null))
                repository.getCustomerReward().let {
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataReward.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataReward.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataReward.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataReward.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){
                liveDataReward.postValue(Resource.noInterNet("", null))
            }
        }

    }

    fun deleteReview(reviewId:String) {
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {

                repository.deleteReview(reviewId).let {
                    liveDataUpdateReview.postValue(Resource.loading(null))
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            getProductReview()
                            liveDataUpdateReview.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataUpdateReview.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataUpdateReview.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataUpdateReview.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){
                Log.e("123456", "deleteReview: $ex")
                liveDataUpdateReview.postValue(Resource.noInterNet("", null))
            }
        }


    }

    fun updateReview(reviewId: String, rating: Float, title: String, review: String) {
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {

                repository.updateReview(reviewId,rating,title,review).let {
                    liveDataUpdateReview.postValue(Resource.loading(null))
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            getProductReview()
                            liveDataUpdateReview.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataUpdateReview.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataUpdateReview.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataUpdateReview.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){
                liveDataUpdateReview.postValue(Resource.noInterNet("", null))
            }
        }


    }
}
