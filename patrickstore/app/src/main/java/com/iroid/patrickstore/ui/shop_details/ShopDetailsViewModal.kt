package com.iroid.patrickstore.ui.shop_details

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_address.AddAddressResponse
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.all_categories.AllCategoriesResponse
import com.iroid.patrickstore.model.categoryProdcut.CategoryProductResponse
import com.iroid.patrickstore.model.categoryProdcut.seller.SellerCategoryWiseResponse
import com.iroid.patrickstore.model.seller.SingleSellerResponse
import com.iroid.patrickstore.model.service.service_list.ServicelistResponse
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ShopDetailsViewModal : ViewModel() {
    private val liveDataSeller = MutableLiveData<Resource<SingleSellerResponse>>()
    val sellerLiveData: LiveData<Resource<SingleSellerResponse>> get() = liveDataSeller

    private val liveDataCategoryProduct=MutableLiveData<Resource<CategoryProductResponse>>()
    val categoryProductLiveData:LiveData<Resource<CategoryProductResponse>> get() = liveDataCategoryProduct

    private val liveDataServiceDetail = MutableLiveData<Resource<ServicelistResponse>>()
    val serviceDetailLiveData: LiveData<Resource<ServicelistResponse>> get() = liveDataServiceDetail

    private val liveDataAddToCart = MutableLiveData<Resource<AddToCartResponse>>()
    val addToCartLiveData: LiveData<Resource<AddToCartResponse>> get() = liveDataAddToCart

    private val liveDataAllCategories= MutableLiveData<Resource<AllCategoriesResponse>>()
    val allCategoriesLiveData: LiveData<Resource<AllCategoriesResponse>> get() = liveDataAllCategories

    fun getAllCategories(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataAllCategories.postValue(Resource.loading(null))
                repository.getProductCategories().let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataAllCategories.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataAllCategories.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataAllCategories.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataAllCategories.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){
                liveDataAllCategories.postValue(Resource.noInterNet("", null))

            }
        }
    }

    fun getCategoryProduct(categoryId:String,isPerishable:Boolean,sellerId:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveDataCategoryProduct.postValue(Resource.loading(null))
            try {
                repository.getCategoryProduct(1,10,categoryId,isPerishable,sellerId).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataCategoryProduct.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataCategoryProduct.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataCategoryProduct.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataCategoryProduct.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataSeller.postValue(Resource.noInterNet("", null))
            }
        }
    }



    fun getSeller(store_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveDataSeller.postValue(Resource.loading(null))
            try {
                coroutineScope {

                    val serviceListDeferred = async { repository.getSellerService(1,10,store_id) }
                    val storeDeferred = async {repository.getSellerSingle(store_id) }
                    val serviceApi=serviceListDeferred.await()
                    val storeApi=storeDeferred.await()


                    val responseService=serviceApi.body()
                    val responseStore=storeApi.body()


                    when(responseService!!.statusCode){
                        Constants.REQUEST_OK -> {
                            liveDataServiceDetail.postValue(Resource.success(responseService))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataServiceDetail.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataServiceDetail.postValue(Resource.error(responseService.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataServiceDetail.postValue(Resource.noInterNet("", null))
                        }
                    }
                    when (responseStore!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataSeller.postValue(Resource.success(responseStore))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataSeller.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataSeller.postValue(Resource.error(responseService!!.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataSeller.postValue(Resource.noInterNet("", null))
                        }
                    }
                }




            } catch (ex: Exception) {
                liveDataSeller.postValue(Resource.noInterNet("", null))
            }
        }

    }
    fun addToCart(product: String, quantity: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataAddToCart.postValue(Resource.loading(null))
                repository.addToCart(product, quantity).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataAddToCart.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataAddToCart.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataAddToCart.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataAddToCart.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataAddToCart.postValue(Resource.noInterNet("", null))
            }
        }
    }

}
