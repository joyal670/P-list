package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner

import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.completed.OwnerCompletedFragment
import com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.ongoing.OwnerOngoingFragment
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.ViewPagerAdapter
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.utils.replaceFragment
import com.shameem.projectstructure.listeners.ActivityListener
import kotlinx.android.synthetic.main.activity_owners.*
import kotlinx.android.synthetic.main.toolbar_main.*

class OwnerActivity : BaseActivity(), ActivityListener, FragmentTransInterface {
    override val layoutId: Int
        get() = R.layout.activity_owners
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

        tabs_owner!!.setupWithViewPager(viewPager_owner)
        val adapter = ViewPagerAdapter(
            supportFragmentManager
        ).also {
            it.addFragment(OwnerOngoingFragment(), getString(R.string.Ongoing))
            it.addFragment(OwnerCompletedFragment(), getString(R.string.Completed))
        }
        viewPager_owner.adapter = adapter
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