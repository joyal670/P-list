package com.iroid.patrickstore.ui.shop_details

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityShopDetailsBinding
import com.iroid.patrickstore.model.seller.SingleSeller
import com.iroid.patrickstore.model.service.service_list.Item
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.preference.AppPreferences.sellerId
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.shop_details.adapter.ShopPagerAdapter
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.Constants.BUNDLE_DEC
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_EMAIL
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_LOCATION
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_PHONE
import kotlinx.android.synthetic.main.activity_shop_details.*
import kotlinx.android.synthetic.main.activity_shop_details.tabLayout

class ShopDetailsActivity : BaseActivity<ShopDetailsViewModal,ActivityShopDetailsBinding>(), View.OnClickListener{


    override val layoutId: Int get() = R.layout.activity_shop_details
    override val setToolbar: Boolean get() = true
    override val hideStatusBar: Boolean get() = false
    private var storeId=""
    private var items: List<Item> =ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.alummoottil_enterprise))

    }

    private fun getTitleList(): List<String>{
        return listOf(
            getString(R.string.products),
            getString(R.string.services),
            getString(R.string.about),
            getString(R.string.reviews)
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
        when(v){

        }
    }

    override fun getViewBinding(): ActivityShopDetailsBinding {
        return ActivityShopDetailsBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setSupportActionBar(binding.layoutToolbar.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.layoutToolbar.tvToolbarTitle.text=getString(R.string.alummoottil_enterprise)
        intent.getStringExtra(Constants.INTENT_STORE_ID).let {store_id->
            storeId= store_id.toString()
            AppPreferences.sellerId=store_id
            sellerId=storeId
            viewModel.getSeller(store_id.toString())

        }

        setUpObserver()


    }
    private fun setUpObserver() {
        viewModel.serviceDetailLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    items=it.data!!.data.items
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                }
            }
        })
        viewModel.sellerLiveData.observe(this, Observer { sellerResponse->
            when(sellerResponse.status){
                Status.SUCCESS->{
                    dismissProgress()
                    setData(sellerResponse.data!!.data)
                }
                Status.LOADING->{
                    showProgress()
                }
                Status.ERROR->{
                    dismissProgress()
                    Toaster.showToast(this,sellerResponse.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissProgress()
                    if(this.isConnected){
                        Toaster.showToast(this, SOMETHING_WENT_WRONG, Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this, NO_INTERNET_MESSAGE, Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }

                }


            }


        })
    }

    private fun setData(singleSeller: SingleSeller) {
        val tab  = tabLayout.getTabAt(0)?.orCreateBadge
        tab?.isVisible = true
        tab?.number = 250
        val bundle = Bundle()
        bundle.putString(BUNDLE_KEY_LOCATION,singleSeller.addressDetails.location)
        bundle.putString(BUNDLE_KEY_EMAIL,singleSeller.email)
        bundle.putString(BUNDLE_KEY_PHONE,singleSeller.mobile)
        bundle.putString(BUNDLE_DEC,singleSeller.description)
        viewPager.adapter = ShopPagerAdapter(supportFragmentManager, getTitleList(),singleSeller.sellingCategory,bundle,storeId,items)
        tabLayout.setupWithViewPager(viewPager)
        for ((index, title) in getTitleList().withIndex()){
            tabLayout.getTabAt(index)?.setCustomView(R.layout.layout_shop_tab)
            val tvTitle = tabLayout.getTabAt(index)?.customView?.findViewById(R.id.tvProductName) as TextView
            val tvBadge = tabLayout.getTabAt(index)?.customView?.findViewById(R.id.tvBadge) as TextView
            tvTitle.text = title
            if (index == 0) {
                tvBadge.visibility
                View.VISIBLE
                tvBadge.text = "234"
            } else{
                tvBadge.visibility = View.GONE
            }
        }
        binding.tvProductName.text=singleSeller.sellerName
        binding.layoutToolbar.tvToolbarTitle.text=singleSeller.sellerName
//        if(singleSeller.sellerBannerImageId.publicUrl.isNotEmpty()){
//            CommonUtils.setImageBase(this,singleSeller.sellerBannerImageId.publicUrl,binding.ivShopBanner)
//        }
        if (singleSeller.profileImageId.publicUrl.isNotEmpty()){
            CommonUtils.setImageBase(this,singleSeller.profileImageId.publicUrl,binding.ivShopPic)
        }

    }
}
