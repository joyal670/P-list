package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.ViewPagerAdapter
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_page.ServiceFragment
import com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status.StatusFragment
import com.shameem.projectstructure.listeners.ActivityListener
import com.property.propertyagent.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_maintenance_.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class MaintenanceActivity : BaseActivity() , ActivityListener , FragmentTransInterface {
    private var tabInvokeCode : String = ""
    override val layoutId : Int
        get() = R.layout.activity_maintenance_
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = getString(R.string.maintenance)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupViewPager()
    }

    private fun setupViewPager() {
        // tabs setup
        tabsMaintenance!!.setupWithViewPager(vpMaintenance)
        val adapter = ViewPagerAdapter(supportFragmentManager).also {
            it.addFragment(ServiceFragment() , getString(R.string.services))
            it.addFragment(StatusFragment() , getString(R.string.Status))
        }
        vpMaintenance.adapter = adapter
        if (tabInvokeCode == "success") vpMaintenance.currentItem = 1
        else vpMaintenance.currentItem = 0
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
    }

    override fun navigateToFragment(fragment : Fragment) {
        replaceFragment(fragment = fragment , addToBackStack = true)
    }

    override fun setTitleFromFragment(title : String) {
        tvToolbarTitleOwner.text = title
    }

    override fun onActivityResult(requestCode : Int , resultCode : Int , data : Intent?) {
        super.onActivityResult(requestCode , resultCode , data)
        // Check that it is the SecondActivity with an OK result
        if (requestCode == 33) {
            if (resultCode == Activity.RESULT_OK) {
                tabInvokeCode = "success"
                setupViewPager()
            }
        }
    }

}