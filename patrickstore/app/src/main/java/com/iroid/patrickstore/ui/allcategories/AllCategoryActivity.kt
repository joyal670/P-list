package com.iroid.patrickstore.ui.allcategories

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityCategoriesBinding
import com.iroid.patrickstore.model.all_categories.AllCategories
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.model.seller.SingleSeller
import com.iroid.patrickstore.ui.allcategories.adapter.CategoriesAdapter
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.home.adapter.ProductAdapter
import com.iroid.patrickstore.ui.home.adapter.ShopAdapter
import com.iroid.patrickstore.utils.*


class AllCategoryActivity : BaseActivity<AllCategoryViewModal, ActivityCategoriesBinding>() {
    private var type = false

    override val layoutId: Int
        get() = R.layout.activity_categories
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CommonUtils.setToolbarMargin(this, binding.layoutToolbar.tvToolbarTitle)

    }

    override fun getViewBinding(): ActivityCategoriesBinding {
        return ActivityCategoriesBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpObserver()
        viewModel.getAllCategories()
    }

    private fun setUpObserver() {
        viewModel.allCategoriesLiveData.observe(this, Observer { allCategoryResponse ->
            when (allCategoryResponse.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setUpProductCategoriesAdapter(allCategoryResponse.data!!.data)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()

                }
                Status.ERROR -> {
                    dismissProgress()
                }
            }
        })
        viewModel.categoryProductLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setProductAdapter(it.data?.data?.items)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {

                    }
                }
            }
        })
        viewModel.categorySellerLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setSellerAdapter(it.data?.data?.items)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {

                    }
                }
            }
        })
        viewModel.addToCartLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    showNavigateToCart()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }


    private fun setProductAdapter(productList: List<Product>?) {
        if (productList!!.isNotEmpty()) {
            binding.noData.visibility = View.GONE
            binding.rvProduct.visibility = View.VISIBLE
            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
            binding.rvProduct.layoutAnimation = controller

            binding.rvProduct.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            binding.rvProduct.adapter = ProductAdapter(this, productList) { id, qty ->
                functionAddToCart(
                    id,
                    qty
                )
            }
        } else {
            binding.noData.visibility = View.VISIBLE
            binding.rvProduct.visibility = View.GONE
        }


    }

    private fun setUpProductCategoriesAdapter(categories: List<AllCategories>) {
        if(categories.isNotEmpty()){
            if(categories[0].children.isNotEmpty()){
                binding.layoutToolbar.tvToolbarTitle.text = categories[0].children[0].name
                binding.tvCategories.text=categories[0].children[0].name
                categories[0].expanded=true
                callApi(categories[0].children[0]._id,categories[0].children[0].isPerishable)
                categories[0].children[0].isChecked=true
            }else{
                binding.layoutToolbar.tvToolbarTitle.text = categories[0].name
                binding.tvCategories.text=categories[0].name
                categories[0].expanded=true
                callApi(categories[0]._id,categories[0].isPerishable)
                categories[0].isChecked=true
            }

            binding.rvCategory.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvCategory.adapter =
                CategoriesAdapter(this, categories) { id, isPerishable,name ->
                    binding.tvCategories.text=name
                    binding.layoutToolbar.tvToolbarTitle.text =name
                    callApi(id, isPerishable)
                }
        }

    }

    private fun setSellerAdapter(sellerList: List<SingleSeller>?) {
        if(sellerList.isNullOrEmpty()){
            binding.noData.visibility=View.VISIBLE
            binding.rvProduct.visibility=View.GONE
        }else{
            binding.noData.visibility=View.GONE
            binding.rvProduct.visibility=View.VISIBLE
            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
            binding.rvProduct.layoutAnimation = controller
            binding.rvProduct.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            binding.rvProduct.adapter = ShopAdapter(this, sellerList!!)
        }

    }

    private fun callApi(id: String, type: Boolean) {
        this.type = type
        if (type) {
            viewModel.getCategorySeller(id, type,"")
        } else {
            viewModel.getCategoryProduct(id, type)
        }

    }

    private fun functionAddToCart(id: String, qty: String) {
        viewModel.addToCart(id, qty)
    }

    private fun showNavigateToCart() {
        val snackBar = Snackbar.make(binding.coordMain, "Item added to cart", Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        snackBar.setTextColor(Color.WHITE)
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setAction("GO TO CART") {
            this.startActivity(Intent(this, CartActivity::class.java))
        }.show()
    }
}
