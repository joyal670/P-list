package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.details

import android.content.Intent
import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.home.HomeActivity
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.utils.AppPreferences.is_user_property_booked
import com.property.propertyagent.utils.replaceFragment
import com.shameem.projectstructure.listeners.ActivityListener
import kotlinx.android.synthetic.main.toolbar_main.*

class UsersPropertyViewDetailedActivity : BaseActivity(), ActivityListener, FragmentTransInterface {
    private var passedId = ""
    override val layoutId: Int
        get() = R.layout.activity_users_details
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        replaceFragment(fragment = UserPropertyViewDetailedFragment.newInstance(passedId))
    }

    override fun initData() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        passedId = intent.getStringExtra("tour_id").toString()
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (is_user_property_booked) {
            is_user_property_booked = false
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}