package com.property.propertyagent.agent_panel.ui.main.home.myclient.users

import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.completed.UserCompletedFragment
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.ongoing.UserOngoingFragment
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.utils.replaceFragment
import com.shameem.projectstructure.listeners.ActivityListener
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.toolbar_main.*

class UsersActivity : BaseActivity(), ActivityListener, FragmentTransInterface {

    override val layoutId: Int
        get() = R.layout.activity_users
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tabs_user!!.setupWithViewPager(viewPager_user)
        val adapter =
            ViewPagerAdapter(
                supportFragmentManager
            )
        adapter.addFragment(UserOngoingFragment(), getString(R.string.Ongoing))
        adapter.addFragment(UserCompletedFragment(), getString(R.string.Completed))
        viewPager_user.adapter = adapter

    }

    override fun initData() {
        fragmentLaunch()
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
        tvToolbarTitle.text = title
    }
}