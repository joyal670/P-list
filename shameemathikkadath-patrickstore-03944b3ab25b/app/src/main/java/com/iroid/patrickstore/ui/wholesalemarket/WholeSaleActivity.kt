package com.iroid.patrickstore.ui.wholesalemarket

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityWholesalemarketBinding
import com.iroid.patrickstore.ui.seller.adapter.SellerListAdapter
import com.iroid.patrickstore.utils.CommonUtils


class WholeSaleActivity:BaseActivity<WholeSaleViewModal,ActivityWholesalemarketBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_wholesalemarket
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false



    override fun getViewBinding(): ActivityWholesalemarketBinding {
        return ActivityWholesalemarketBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setSupportActionBar(binding.layoutToolbar.toolbar)
        binding.layoutToolbar.tvToolbarTitle.text=getString(R.string.home_wholesale_market)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        CommonUtils.setToolbarMargin(this, binding.layoutToolbar.tvToolbarTitle)
        binding.rvWholeSale.layoutManager = LinearLayoutManager(this)
        binding.rvWholeSale.adapter = SellerListAdapter(this)
    }
}