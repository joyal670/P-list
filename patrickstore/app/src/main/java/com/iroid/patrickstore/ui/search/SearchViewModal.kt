package com.iroid.patrickstore.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.categoryProdcut.CategoryProductResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class SearchViewModal : ViewModel() {
    private val liveDataSearchProduct=MutableLiveData<Resource<CategoryProductResponse>>()
    val searchProductLiveData:LiveData<Resource<CategoryProductResponse>> get() = liveDataSearchProduct

    private val liveDataAddToCart = MutableLiveData<Resource<AddToCartResponse>>()
    val addToCartLiveData: LiveData<Resource<AddToCartResponse>> get() = liveDataAddToCart

    fun getSearchProduct(searchText: String,
                         page: String,
                         limit: String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveDataSearchProduct.postValue(Resource.loading(null))
            try {
                repository.getSearchProduct(searchText,"1","10").let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataSearchProduct.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataSearchProduct.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataSearchProduct.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataSearchProduct.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("123", "getProduct: $ex" )
                liveDataSearchProduct.postValue(Resource.noInterNet("", null))
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
