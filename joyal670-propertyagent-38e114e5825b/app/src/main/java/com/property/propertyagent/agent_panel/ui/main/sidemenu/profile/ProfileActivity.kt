package com.property.propertyagent.agent_panel.ui.main.sidemenu.profile

import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.agent_panel.ui.main.sidemenu.feedback.FeedbckFragment
import com.property.propertyagent.agent_panel.ui.main.sidemenu.inhandcash.CashInHandFragment
import com.property.propertyagent.agent_panel.ui.main.home.mytask.MyTaskCalenderViewFragment
import com.property.propertyagent.agent_panel.ui.main.sidemenu.qrscanner.QrScannerFragment
import com.property.propertyagent.agent_panel.ui.main.sidemenu.myearning.MyEarningsFragment
import com.property.propertyagent.agent_panel.ui.main.sidemenu.notification.NotificationFragment
import com.property.propertyagent.utils.EnumFromPage.*
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.changePassword.ChangePasswordFragment
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.myproperty.MyPropertyFragment
import com.property.propertyagent.base.BaseActivity
import com.shameem.projectstructure.listeners.ActivityListener
import com.property.propertyagent.utils.Constants.TYPE
import com.property.propertyagent.utils.replaceFragment
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbar

class ProfileActivity : BaseActivity() , ActivityListener , FragmentTransInterface {
    override val layoutId : Int
        get() = R.layout.activity_profile
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        launchFragment(intent.getStringExtra(TYPE))
    }

    private fun launchFragment(stringExtra : String?) {
        when (stringExtra) {
            // profile fragment
            PROFILE.name -> {
                replaceFragment(fragment = ProfileFragment())
            }
            // my properties fragment
            MYPROFERTIES.name -> {
                replaceFragment(fragment = MyPropertyFragment())
            }
            // my earning fragment
            MYEARNIBNGS.name -> {
                replaceFragment(fragment = MyEarningsFragment())
            }
            // inhand cash fragment
            INHANDCASH.name -> {
                replaceFragment(fragment = CashInHandFragment())
            }
            // change password fragment
            CHANGEPASSWORD.name -> {
                replaceFragment(fragment = ChangePasswordFragment())
            }
            // feedback fragment
            FEEDBACK.name -> {
                replaceFragment(fragment = FeedbckFragment())
            }
            // notification fragment
            NOTIFICATION.name -> {
                replaceFragment(fragment = NotificationFragment())
            }
            // qr fragment
            QRREADER.name -> {
                replaceFragment(fragment = QrScannerFragment())
            }
            // my task fragment
            MYTASKCALENDER.name -> {
                replaceFragment(fragment = MyTaskCalenderViewFragment())
            }
        }
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
        tvToolbarTitle.text = title
    }
}