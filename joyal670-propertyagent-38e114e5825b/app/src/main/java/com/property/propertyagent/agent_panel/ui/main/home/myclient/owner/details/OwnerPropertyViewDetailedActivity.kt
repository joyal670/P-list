package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.details

import android.util.Log
import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.utils.replaceFragment
import com.shameem.projectstructure.listeners.ActivityListener
import kotlinx.android.synthetic.main.toolbar_main.*

class OwnerPropertyViewDetailedActivity : BaseActivity(), ActivityListener, FragmentTransInterface {
    private var passedTourId = ""
    private var passedType = ""
    override val layoutId: Int
        get() = R.layout.activity_owner_details
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
        replaceFragment(
            fragment = OwnerPropertyViewDetailedFragment.newInstance(
                passedTourId,
                passedType
            )
        )
    }

    override fun initData() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        passedTourId = intent.getStringExtra("tour_id").toString()
        passedType = intent.getStringExtra("type").toString()
        Log.e("check id", passedTourId)
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