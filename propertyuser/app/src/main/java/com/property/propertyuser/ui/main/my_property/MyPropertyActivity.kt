package com.property.propertyuser.ui.main.my_property

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityMyPropertyBinding
import com.property.propertyuser.databinding.ActivitySplashBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.my_property.adapter.MyPropertyTabAdapter
import kotlinx.android.synthetic.main.activity_my_property.*

class MyPropertyActivity : BaseActivity<ActivityMyPropertyBinding>(), ActivityListener {
    override fun getViewBinging(): ActivityMyPropertyBinding = ActivityMyPropertyBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_my_property
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setupViewPager()
    }

    private fun setupViewPager() {
        vpMyProperties!!.adapter = MyPropertyTabAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tabsProperties!!, vpMyProperties!!, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.rental)
                1 -> tab.text = resources.getString(R.string.owned)
                2 -> tab.text = resources.getString(R.string.booked)
                3 -> tab.text = resources.getString(R.string.scheduled)
            }
        }).attach()
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
        Log.e("TAG", "setTitle: " + title )
    }

}