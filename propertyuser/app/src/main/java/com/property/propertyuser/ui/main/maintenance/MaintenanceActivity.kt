package com.property.propertyuser.ui.main.maintenance

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityMaintenanceBinding
import com.property.propertyuser.databinding.ActivityOtpBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.maintenance.adapter.MaintenanceTabAdapter
import com.property.propertyuser.ui.main.my_property.MyPropertyActivity
import kotlinx.android.synthetic.main.activity_maintenance.*

class MaintenanceActivity : BaseActivity<ActivityMaintenanceBinding>(), ActivityListener {
    override fun getViewBinging(): ActivityMaintenanceBinding = ActivityMaintenanceBinding.inflate(layoutInflater)
    private lateinit var tabInvokeCode:String
    private var userPropertyId=""
    private var propertyId=""
    override val layoutId: Int
        get() = R.layout.activity_maintenance
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        tabInvokeCode= intent.getStringExtra("statuscode").toString()
        propertyId=intent.getStringExtra("property_id").toString()
        userPropertyId=intent.getStringExtra("user_property_id").toString()

        setupViewPager()
    }
    private fun setupViewPager() {
        vpMaintenance!!.adapter = MaintenanceTabAdapter(propertyId,userPropertyId,supportFragmentManager, lifecycle)
        TabLayoutMediator(tabsMaintenance!!, vpMaintenance!!, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.services)
                1 -> tab.text = resources.getString(R.string.status)
            }
        }).attach()
        if(tabInvokeCode.equals("success")) vpMaintenance.currentItem=1
        else vpMaintenance.currentItem=0

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
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check that it is the SecondActivity with an OK result
        if (requestCode == 33) {
            if (resultCode == Activity.RESULT_OK) {
                tabInvokeCode="success"
                setupViewPager()
            }
        }
    }

}