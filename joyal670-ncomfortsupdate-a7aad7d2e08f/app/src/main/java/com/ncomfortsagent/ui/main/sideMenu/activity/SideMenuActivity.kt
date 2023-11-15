package com.ncomfortsagent.ui.main.sideMenu.activity

import androidx.fragment.app.Fragment
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivitySideMenuBinding
import com.ncomfortsagent.listeners.ActivityListener
import com.ncomfortsagent.listeners.FragmentTransInterface
import com.ncomfortsagent.ui.main.sideMenu.changePassword.fragment.ChangePasswordFragment
import com.ncomfortsagent.ui.main.sideMenu.faq.fragment.FaqFragment
import com.ncomfortsagent.ui.main.sideMenu.feedback.fragment.FeedbackFragment
import com.ncomfortsagent.ui.main.sideMenu.legalInformation.LegalInformationFragment
import com.ncomfortsagent.ui.main.sideMenu.notifications.NotificationsFragment
import com.ncomfortsagent.ui.main.sideMenu.privacyPolicy.PrivacyPolicyFragment
import com.ncomfortsagent.utils.Constants.TYPE
import com.ncomfortsagent.utils.EnumFromPage
import com.ncomfortsagent.utils.replaceFragment

class SideMenuActivity : BaseActivity<ActivitySideMenuBinding>(), ActivityListener,
    FragmentTransInterface {
    override val layoutId: Int
        get() = R.layout.activity_side_menu
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivitySideMenuBinding = ActivitySideMenuBinding.inflate(layoutInflater)

    override fun initData() {
        /* toolbar setup */
        setSupportActionBar(binding.tool.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        launchFragment(intent.getStringExtra(TYPE))
    }

    private fun launchFragment(stringExtra: String?)
    {
        when (stringExtra)
        {

            /* change password page */
            EnumFromPage.CHANGE_PASSWORD.name -> {
                replaceFragment(fragment = ChangePasswordFragment())
            }

            /* Legal information page */
            EnumFromPage.LEGAL_INFORMATION.name -> {
                replaceFragment(fragment = LegalInformationFragment())
            }

            /* Privacy policy page */
            EnumFromPage.PRIVACY_POLICY.name -> {
                replaceFragment(fragment = PrivacyPolicyFragment())
            }

            /* Feedback page */
            EnumFromPage.FEEDBACK.name -> {
                replaceFragment(fragment = FeedbackFragment())
            }

            /* FAQ Page*/
            EnumFromPage.FAQ.name -> {
                replaceFragment(fragment = FaqFragment())
            }

            /* Notification Page */
            EnumFromPage.NOTIFICATION.name -> {
                replaceFragment(fragment = NotificationsFragment())
            }
        }

    }

    override fun fragmentLaunch() {

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

    override fun setTitleFromFragment(title: String) {
        binding.tool.tvToolbarTitle.text = title
    }

}