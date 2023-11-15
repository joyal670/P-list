package com.property.propertyagent.start_up.auth.activity

import androidx.fragment.app.Fragment
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.start_up.auth.fragment.LoginFragment
import com.shameem.projectstructure.listeners.ActivityListener
import com.property.propertyagent.utils.replaceFragment

class AuthActivity : BaseActivity() , ActivityListener {

    override val layoutId : Int
        get() = R.layout.activity_auth
    override val setToolbar : Boolean
        get() = false
    override val hideStatusBar : Boolean
        get() = true

    override fun fragmentLaunch() {
        replaceFragment(fragment = LoginFragment())
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

    override fun navigateToFragment(fragment : Fragment) {
        replaceFragment(fragment = fragment , addToBackStack = true)
    }
}