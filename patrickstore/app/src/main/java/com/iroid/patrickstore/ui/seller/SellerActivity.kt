package com.iroid.patrickstore.ui.seller

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivitySellerBinding
import com.iroid.patrickstore.model.seller.SingleSeller
import com.iroid.patrickstore.ui.seller.adapter.SellerPagerAdapter
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.activity_seller.*

class SellerActivity : BaseActivity<SellerViewModal,ActivitySellerBinding>() {
    override val layoutId: Int
        get() =  R.layout.activity_seller
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.shop)

    }

    private fun getTitles(): List<String>{
        return listOf(
            getString(R.string.food_beverages),
            getString(R.string.mobile_laptop),
            getString(R.string.all))
    }

    override fun getViewBinding(): ActivitySellerBinding {
        return ActivitySellerBinding.inflate(layoutInflater)
    }

    override fun initViews() {


    }

}
