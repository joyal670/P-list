package com.property.propertyuser.ui.main.sale_and_rent_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivitySaleAndRentBinding
import com.property.propertyuser.databinding.ActivitySideMenuBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.sale_and_rent_details.rent_details.RentDetailsFragment
import com.property.propertyuser.ui.main.sale_and_rent_details.sale_details.SalesDetailsFragment
import com.property.propertyuser.ui.startup.auth.otp.OtpFragment
import com.property.propertyuser.utils.replaceFragment

class SaleAndRentActivity : BaseActivity<ActivitySaleAndRentBinding>(), ActivityListener {
    override fun getViewBinging(): ActivitySaleAndRentBinding = ActivitySaleAndRentBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_sale_and_rent
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        val property_id = intent.getStringExtra("property_id")
        val property_to=intent.getStringExtra("property_to")
        if(property_to=="rent"){
            replaceFragment(fragment = RentDetailsFragment.newInstance(property_id.toString()))
        }
        else{
            replaceFragment(fragment = SalesDetailsFragment.newInstance(property_id.toString()))
        }
    }

    override fun initData() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}