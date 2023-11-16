package com.iroid.patrickstore.ui.allcategories

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_address.AddAddressResponse
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.all_categories.AllCategoriesResponse
import com.iroid.patrickstore.model.categoryProdcut.CategoryProductResponse
import com.iroid.patrickstore.model.categoryProdcut.seller.SellerCategoryWiseResponse
import com.iroid.patrickstore.model.order_summary.OrderSummaryResponse
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.shop_details.ShopDetailsViewModal
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.launch

class AllCategoryViewModal : ViewModel() {
    private val liveDataAllCategories= MutableLiveData<Resource<AllCategoriesResponse>>()
    val allCategoriesLiveData: LiveData<Resource<AllCategoriesResponse>> get() = liveDataAllCategories

    private val liveDataAddToCart = MutableLiveData<Resource<AddToCartResponse>>()
    val addToCartLiveData: LiveData<Resource<AddToCartResponse>> get() = liveDataAddToCart

    private val liveDataCategoryProduct=MutableLiveData<Resource<CategoryProductResponse>>()
    val categoryProductLiveData:LiveData<Resource<CategoryProductResponse>> get() = liveDataCategoryProduct

    private val liveDataCategorySeller=MutableLiveData<Resource<SellerCategoryWiseResponse>>()
    val categorySellerLiveData:LiveData<Resource<SellerCategoryWiseResponse>> get() = liveDataCategorySeller

    fun getCategorySeller(categoryId:String,isPerishable:Boolean,sellerId:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveDataCategoryProduct.postValue(Resource.loading(null))
            try {
                repository.getCategorySeller(1,50,categoryId,isPerishable,sellerId).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataCategorySeller.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataCategorySeller.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataCategorySeller.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataCategorySeller.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("123", "getCategorySeller: $ex" )
                liveDataCategorySeller.postValue(Resource.noInterNet("", null))
            }
        }
    }
    fun getCategoryProduct(categoryId:String,isPerishable:Boolean){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveDataCategoryProduct.postValue(Resource.loading(null))
            try {
                repository.getCategoryProduct(1,10,categoryId,isPerishable,AppPreferences.sellerId!!).let {
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
                Log.e("123", "getProduct: $ex" )
                liveDataCategoryProduct.postValue(Resource.noInterNet("", null))
            }
        }
    }
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
