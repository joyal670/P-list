package com.iroid.patrickstore.ui.productdetail


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityProductDetailBinding
import com.iroid.patrickstore.model.single_product.SingleData
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.productdetail.adapter.PreviewAdapter
import com.iroid.patrickstore.ui.productdetail.adapter.ProductPagerAdapter
import com.iroid.patrickstore.ui.productdetail.adapter.TagAdapter
import com.iroid.patrickstore.ui.shop_details.ShopDetailsActivity
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.layout_add_buy.*
import kotlinx.android.synthetic.main.layout_product_detail_quantity.*
import kotlinx.android.synthetic.main.layout_soldby.*
import kotlinx.android.synthetic.main.layout_tag.*


class ProductDetailActivity : BaseActivity<ProductDetailViewModal, ActivityProductDetailBinding>(),
    View.OnClickListener {

    private var quantity: Int? = 1
    private lateinit var productId: String
    private var isBuy = false
    private var isWishList = false
    private var sellerId = ""

    override val layoutId: Int
        get() = R.layout.activity_product_detail
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(getString(R.string.dummy_product_name))


        //setPreviewAdapter


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.size_list, android.R.layout.simple_spinner_item
        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerSize.adapter = adapter

        //ProductTab
        binding.layoutQuantity.tvMinus.setOnClickListener {
            quantity = Integer.parseInt(binding.layoutQuantity.tvQuantity.text.toString())
            if (quantity != 1) {
                quantity = quantity!! - 1
                binding.layoutQuantity.tvQuantity.text = quantity.toString()
            }

        }
        binding.layoutQuantity.tvPlus.setOnClickListener {
            quantity = Integer.parseInt(binding.layoutQuantity.tvQuantity.text.toString())
            if (quantity!! < 100) {
                quantity = quantity!! + 1
                binding.layoutQuantity.tvQuantity.text = quantity.toString()
            }
        }

        binding.layoutSoldBy.rt.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, "Sample product")
            startActivity(Intent.createChooser(share, ""))
        }


        //setTagAdapter
        rvTag!!.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.margin_small).toInt()
            )
        )
        rvTag!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTag!!.adapter = TagAdapter()

        btnSeller.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
        btnBuy.setOnClickListener(this)


    }

    private fun getTitleList(): List<String> {
        return listOf(
            getString(R.string.tab_description),
            getString(R.string.tab_reviews)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menue_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cart -> {
            startActivity(Intent(this, CartActivity::class.java))
            true
        }
        R.id.action_call -> {
            true
        }
        R.id.action_profile -> {
            startActivity(Intent(this, MyAccountActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            btnSeller -> {
                val intent = Intent(this, ShopDetailsActivity::class.java)
                intent.putExtra(Constants.INTENT_STORE_ID, sellerId)
                startActivity(intent)
            }
            btnAdd -> {
                isBuy = false
                functionAddToCart()
            }
            btnBuy -> {
                isBuy = true
                functionAddToCart()
            }
            tvPlus -> {
                this.quantity = Integer.parseInt(tvQuantity.text.toString())
                if (this.quantity!! < 100) {
                    this.quantity = quantity!! + 1
                    tvQuantity.text = quantity.toString()
                }

            }
            tvMinus -> {
                this.quantity = Integer.parseInt(tvQuantity.text.toString())
                if (this.quantity != 1) {
                    this.quantity = quantity!! - 1
                    tvQuantity.text = quantity.toString()
                }
            }

        }

    }

    override fun getViewBinding(): ActivityProductDetailBinding {
        return ActivityProductDetailBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        intent.getStringExtra(Constants.INTENT_PRODUCT_ID).let { product_id ->
            viewModel.getProduct(product_id.toString())
            productId = product_id.toString()
        }

        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.addToWishListLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    isWishList = !isWishList
                    updateWishList()
                    Toaster.showToast(
                        this,
                        it.data!!.msg!!,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
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
        }
        viewModel.productLiveData.observe(this, Observer { productResponse ->
            when (productResponse.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setData(productResponse.data!!.data)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        productResponse.message!!,
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


        })
        viewModel.addToCartLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (isBuy) {
                        startActivity(Intent(this, CartActivity::class.java))
                    } else {
                        showNavigateToCart()
                    }

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

    private fun updateWishList() {
        if (isWishList) {
            binding.imgWishList.setImageResource(R.drawable.ic_wish_list_filled)
        } else {
            binding.imgWishList.setImageResource(R.drawable.ic_wish_list_fill)
        }
    }

    private fun setData(data: SingleData) {
        contentMain.visibility = View.VISIBLE
        val avarageKm=  distance(
            AppPreferences.lat.toDouble(),
            AppPreferences.lan.toDouble(),
            data.product.sellerId.lat,
            data.product.sellerId.lng
        )/70

        binding.layoutSoldBy.tvDistance.text="Estimate Time Of Arrival - ${String.format("%.2f", avarageKm)} MINUTES"


        if (data.product.category.isPerishable) {
            binding.ivProdcuType.visibility = View.VISIBLE
            binding.layoutQuantity.llVariant.visibility = View.GONE
            binding.layoutTag.constarintTag.visibility = View.GONE
            binding.tvAvailability.visibility = View.GONE
            binding.inStock.visibility = View.GONE
            binding.ivStock.visibility = View.GONE
        } else {
            binding.ivProdcuType.visibility = View.GONE
//            binding.layoutQuantity.llVariant.visibility = View.VISIBLE
//            binding.layoutTag.constarintTag.visibility = View.VISIBLE
//            binding.tvAvailability.visibility = View.VISIBLE
//            binding.inStock.visibility = View.VISIBLE
//            binding.ivStock.visibility = View.VISIBLE
            binding.layoutQuantity.llVariant.visibility = View.GONE
            binding.layoutTag.constarintTag.visibility = View.GONE
            binding.tvAvailability.visibility = View.GONE
            binding.inStock.visibility = View.GONE
            binding.ivStock.visibility = View.GONE
        }
        rvProductPreview!!.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.margin_small).toInt()
            )
        )
        rvProductPreview!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvProductPreview!!.adapter = PreviewAdapter(this, data.product.imgUrl)

        binding.tvProductName.text = data.product.name
        binding.tvRating.text = "0.0"
        binding.tvReviews.text = "00"
        binding.layoutPrice.tvPrice.text = getString(
            R.string.Rupees_symbol,
            data.product.actualPrice.toString().toDouble().toPrice()
        )
        binding.layoutPrice.tvDiscount.text =
            data.product.displayPrice.toString().toDouble().toPrice()
        binding.layoutPrice.tvPercentage.text =
            data.product.displayPrice.toString().toCovertOffer(data.product.actualPrice.toString())

        binding.layoutSoldBy.tvCompany.text = data.product.sellerId.sellerName
        binding.layoutPrice.linearLayout.visibility = View.GONE
        binding.layoutPrice.linearLayout2.visibility = View.GONE
        binding.layoutPrice.linearLayout3.visibility = View.GONE
        binding.layoutPrice.linearLayout4.visibility = View.GONE
        isWishList = data.product.isWishListProduct
        updateWishList()
        CommonUtils.setImageBase(this, data.product.imgUrl[0].publicUrl, binding.imgProduct)

        binding.imgWishList.setOnClickListener {
            functionAddToWishList(data.product.id)
        }
        val tab = tabsProduct.getTabAt(0)?.orCreateBadge
        tab?.isVisible = true
        tab?.number = 250
        view_pager_products.adapter = ProductPagerAdapter(
            supportFragmentManager, getTitleList(), data.product.description,
            data.similarProducts
        )
        tabsProduct.setupWithViewPager(view_pager_products)
        sellerId = data.product.sellerId.id
        val fragmentManager = supportFragmentManager
        fragmentManager.setFragmentResult(
            Constants.REQUEST_KEY_DES,
            bundleOf(Constants.BUNDLE_KEY_DES to data.product.description)
        )
        fragmentManager.setFragmentResult(
            Constants.REQUEST_KEY_REVIEWS,
            bundleOf(Constants.BUNDLE_KEY_REVIEWS to data.product.reviews)
        )
        for ((index, title) in getTitleList().withIndex()) {
            tabsProduct.getTabAt(index)?.setCustomView(R.layout.layout_shop_tab)
            val tvTitle =
                tabsProduct.getTabAt(index)?.customView?.findViewById(R.id.tvProductName) as TextView
            val tvBadge =
                tabsProduct.getTabAt(index)?.customView?.findViewById(R.id.tvBadge) as TextView
            tvTitle.text = title
            if (index == 1) {
                tvBadge.visibility = View.VISIBLE
                tvBadge.text = "0"
            } else {
                tvBadge.visibility = View.GONE
            }
        }

    }

    private fun functionAddToWishList(id: String) {
        viewModel.addToWishList(id)
    }

    private fun functionAddToCart() {
        viewModel.addToCart(productId, quantity.toString())
    }

    private fun showNavigateToCart() {
        val snackBar = Snackbar.make(binding.llMain, "Item added to cart", Snackbar.LENGTH_LONG)
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

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
            * Math.sin(deg2rad(lat2))
            + (Math.cos(deg2rad(lat1))
            * Math.cos(deg2rad(lat2))
            * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}
