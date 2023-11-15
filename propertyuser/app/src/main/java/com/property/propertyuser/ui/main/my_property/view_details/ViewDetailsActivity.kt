package com.property.propertyuser.ui.main.my_property.view_details

import androidx.fragment.app.Fragment
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityViewDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.my_property.view_details.booked_view_details.BookedViewDetailsFragment
import com.property.propertyuser.ui.main.my_property.view_details.owned_view_details.OwnedViewDetailsFragment
import com.property.propertyuser.ui.main.my_property.view_details.rental_view_details.RentalViewDetailsFragment
import com.property.propertyuser.utils.replaceFragment

class ViewDetailsActivity : BaseActivity<ActivityViewDetailsBinding>(), ActivityListener {
    override fun getViewBinging(): ActivityViewDetailsBinding =
        ActivityViewDetailsBinding.inflate(layoutInflater)

    private lateinit var viewDetailsType: String
    override val layoutId: Int
        get() = R.layout.activity_view_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        if (viewDetailsType == "rental") {
            val user_property_id = intent.getStringExtra("user_property_id").toString()
            val fragment = RentalViewDetailsFragment.newInstance(user_property_id)
            replaceFragment(fragment = fragment)
        } else if (viewDetailsType == "owned") {
            val user_property_id = intent.getStringExtra("user_property_id").toString()
            val fragment = OwnedViewDetailsFragment.newInstance(user_property_id)
            replaceFragment(fragment = fragment)
        } else {
            val property_id = intent.getStringExtra("property_id")
            val fragment = BookedViewDetailsFragment.newInstance(property_id.toString())
            replaceFragment(fragment = fragment)
        }

    }

    override fun initData() {
        viewDetailsType = intent.getStringExtra("viewDetails").toString()
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
        replaceFragment(fragment = fragment, addToBackStack = true)
    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}