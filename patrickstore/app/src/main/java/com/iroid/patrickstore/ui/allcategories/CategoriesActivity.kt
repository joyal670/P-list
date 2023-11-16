package com.iroid.patrickstore.ui.allcategories

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityCatgeoriesSideBinding
import com.iroid.patrickstore.model.all_categories.AllCategories
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.allcategories.adapter.CategoriesAdapter
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.home.adapter.ProductAdapter
import com.iroid.patrickstore.ui.offer.adapter.*
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.Constants.INTENT_CATEGORY_ID
import com.iroid.patrickstore.utils.Constants.INTENT_CATEGORY_NAME

class CategoriesActivity : BaseActivity<AllCategoryViewModal, ActivityCatgeoriesSideBinding>() {


    override val layoutId: Int
        get() = R.layout.activity_categories
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinding(): ActivityCatgeoriesSideBinding {
        return ActivityCatgeoriesSideBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.tvToolbarTitle.text=intent.getStringExtra(INTENT_CATEGORY_NAME)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setUpObserver()
        viewModel.getCategoryProduct(intent.getStringExtra(INTENT_CATEGORY_ID)!!,true)

    }
    private fun setUpObserver() {

        viewModel.categoryProductLiveData.observe(this, Observer {
            when(it.status){
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
        if(productList!!.isNotEmpty()){
            binding.noData.visibility= View.GONE
            binding.rvCategories.visibility= View.VISIBLE
            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
            binding.rvCategories.layoutAnimation = controller

            binding.rvCategories.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            binding.rvCategories.adapter = ProductAdapter(this, productList!!) { id, qty ->
                functionAddToCart(
                    id,
                    qty
                )
            }
        }else{
            binding.noData.visibility= View.VISIBLE
            binding.rvCategories.visibility= View.GONE
        }

    }
    private fun functionAddToCart(id: String, qty: String) {
        viewModel.addToCart(id, qty)
    }

    private fun showNavigateToCart() {
        val snackBar = Snackbar.make(binding.coordMain, "Item added to cart", Snackbar.LENGTH_LONG)
        snackBar!!.view.setBackgroundColor(
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
